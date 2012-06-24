package odata.producer.inmemory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import odata.OAtomStreamEntity;
import odata.OCollection;
import odata.OCollections;
import odata.OComplexObjects;
import odata.OEntities;
import odata.OEntity;
import odata.OEntityId;
import odata.OEntityKey;
import odata.OFunctionParameter;
import odata.OLink;
import odata.OLinks;
import odata.OObject;
import odata.OProperties;
import odata.OProperty;
import odata.OSimpleObjects;
import odata.edm.EdmCollectionType;
import odata.edm.EdmComplexType;
import odata.edm.EdmDataServices;
import odata.edm.EdmDecorator;
import odata.edm.EdmEntitySet;
import odata.edm.EdmEntityType;
import odata.edm.EdmFunctionImport;
import odata.edm.EdmMultiplicity;
import odata.edm.EdmNavigationProperty;
import odata.edm.EdmProperty;
import odata.edm.EdmSimpleType;
import odata.edm.EdmStructuralType;
import odata.edm.EdmType;
import odata.edm.EdmProperty.CollectionKind;
import odata.expression.BoolCommonExpression;
import odata.expression.EntitySimpleProperty;
import odata.expression.Expression;
import odata.expression.OrderByExpression;
import odata.expression.OrderByExpression.Direction;
import odata.producer.BaseResponse;
import odata.producer.CountResponse;
import odata.producer.EntitiesResponse;
import odata.producer.EntityIdResponse;
import odata.producer.EntityQueryInfo;
import odata.producer.EntityResponse;
import odata.producer.InlineCount;
import odata.producer.ODataProducer;
import odata.producer.QueryInfo;
import odata.producer.Responses;
import odata.producer.edm.MetadataProducer;
import odata.producer.exceptions.NotFoundException;
import odata.producer.exceptions.NotImplementedException;

import bingo.lang.Func;
import bingo.lang.Func1;
import bingo.lang.Predicate;
import bingo.lang.Strings;
import bingo.lang.enumerable.IterableEnumerable;

/**
 * An in-memory implementation of an ODATA Producer.  Uses the standard Java bean
 * and property model to access information within entities.
 */
public class InMemoryProducer implements ODataProducer {

    private static final String                           ID_PROPNAME               = "EntityId";

    private final String                                  namespace;
    private final String                                  containerName;
    private final int                                     maxResults;
    private final Map<String, InMemoryEntityInfo<?>>      eis                       = new HashMap<String, InMemoryEntityInfo<?>>();
    private final Map<String, InMemoryComplexTypeInfo<?>> complexTypes              = new HashMap<String, InMemoryComplexTypeInfo<?>>();
    private EdmDataServices                               metadata;
    private final EdmDecorator                            decorator;
    private final MetadataProducer                        metadataProducer;
    private final InMemoryTypeMapping                     typeMapping;

    private boolean                                       includeNullPropertyValues = true;

    private static final int                              DEFAULT_MAX_RESULTS       = 100;

    /**
     * Creates a new instance of an in-memory POJO producer.
     *
     * @param namespace  the namespace of the schema registrations
     */
    public InMemoryProducer(String namespace) {
        this(namespace, DEFAULT_MAX_RESULTS);
    }

    /**
     * Creates a new instance of an in-memory POJO producer.
     *
     * @param namespace  the namespace of the schema registrations
     * @param maxResults  the maximum number of entities to return in a single call
     */
    public InMemoryProducer(String namespace, int maxResults) {
        this(namespace, null, maxResults, null, null);
    }

    /**
     * Creates a new instance of an in-memory POJO producer.
     *
     * @param namespace  the namespace of the schema registrations
     * @param containerName  the container name for generated metadata
     * @param maxResults  the maximum number of entities to return in a single call
     * @param decorator  a decorator to use for edm customizations
     * @param typeMapping  optional mapping between java types and edm types, null for default
     */
    public InMemoryProducer(String namespace, String containerName, int maxResults, EdmDecorator decorator, InMemoryTypeMapping typeMapping) {
        this.namespace = namespace;
        this.containerName = Strings.isNotEmpty(containerName) ? containerName : "Container";
        this.maxResults = maxResults;
        this.decorator = decorator;
        this.metadataProducer = new MetadataProducer(this, decorator);
        this.typeMapping = typeMapping == null ? InMemoryTypeMapping.DEFAULT : typeMapping;
    }

    
    public EdmDataServices getMetadata() {
        if (metadata == null) {
            metadata = newEdmGenerator(namespace, typeMapping, ID_PROPNAME, eis, complexTypes).generateEdm(decorator).build();
        }
        return metadata;
    }

    protected InMemoryEdmGenerator newEdmGenerator(String namespace, InMemoryTypeMapping typeMapping, String idPropName, Map<String, InMemoryEntityInfo<?>> eis,
            Map<String, InMemoryComplexTypeInfo<?>> complexTypesInfo) {
        return new InMemoryEdmGenerator(namespace, containerName, typeMapping, ID_PROPNAME, eis, complexTypesInfo);
    }

    
    public MetadataProducer getMetadataProducer() {
        return metadataProducer;
    }

    
    public void close() {

    }

    public void setIncludeNullPropertyValues(boolean value) {
        this.includeNullPropertyValues = value;
    }

    /**
     * register a POJO class as an EdmComplexType.
     * 
     * @param complexTypeClass    The POJO Class
     * @param typeName            The name of the EdmComplexType
     */
    public <TEntity> void registerComplexType(Class<TEntity> complexTypeClass, String typeName) {
        registerComplexType(complexTypeClass, typeName, new EnumsAsStringsPropertyModelDelegate(new BeanBasedPropertyModel(complexTypeClass)));
    }

    public <TEntity> void registerComplexType(Class<TEntity> complexTypeClass, String typeName, PropertyModel propertyModel) {
        InMemoryComplexTypeInfo<TEntity> i = new InMemoryComplexTypeInfo<TEntity>();
        i.typeName = (null == typeName) ? complexTypeClass.getSimpleName() : typeName;
        i.entityClass = complexTypeClass;
        i.propertyModel = propertyModel;

        complexTypes.put(i.typeName, i);
        metadata = null;
    }

    /**
     * Registers a new entity based on a POJO, with support for composite keys.
     *
     * @param entityClass  the class of the entities that are to be stored in the set
     * @param entitySetName  the alias the set will be known by; this is what is used in the OData url
     * @param get  a function to iterate over the elements in the set
     * @param keys  one or more keys for the entity
     */
    public <TEntity> void register(Class<TEntity> entityClass, String entitySetName, Func<Iterable<TEntity>> get, String... keys) {
        register(entityClass, entitySetName, entitySetName, get, keys);
    }

    /**
     * Registers a new entity based on a POJO, with support for composite keys.
     *
     * @param entityClass  the class of the entities that are to be stored in the set
     * @param entitySetName  the alias the set will be known by; this is what is used in the OData url
     * @param entityTypeName  type name of the entity
     * @param get  a function to iterate over the elements in the set
     * @param keys  one or more keys for the entity
     */
    public <TEntity> void register(Class<TEntity> entityClass, String entitySetName, String entityTypeName, Func<Iterable<TEntity>> get, String... keys) {
        PropertyModel model = new BeanBasedPropertyModel(entityClass);
        model = new EnumsAsStringsPropertyModelDelegate(model);
        register(entityClass, model, entitySetName, entityTypeName, get, keys);
    }

    /**
     * Registers a new entity set based on a POJO type using the default property model.
     */
    public <TEntity, TKey> void register(Class<TEntity> entityClass, Class<TKey> keyClass, String entitySetName, Func<Iterable<TEntity>> get, Func1<TEntity, TKey> id) {
        PropertyModel model = new BeanBasedPropertyModel(entityClass);
        model = new EnumsAsStringsPropertyModelDelegate(model);
        model = new EntityIdFunctionPropertyModelDelegate<TEntity, TKey>(model, ID_PROPNAME, keyClass, id);
        register(entityClass, model, entitySetName, get, ID_PROPNAME);
    }

    /**
     * Registers a new entity set based on a POJO type and a property model.
     *
     * @param entityClass  the class of the entities that are to be stored in the set
     * @param propertyModel a way to get/set properties on the POJO
     * @param entitySetName  the alias the set will be known by; this is what is used in the ODATA URL
     * @param get  a function to iterate over the elements in the set
     * @param keys  one or more keys for the entity
     */
    public <TEntity, TKey> void register(Class<TEntity> entityClass, PropertyModel propertyModel, String entitySetName, Func<Iterable<TEntity>> get, String... keys) {
        register(entityClass, propertyModel, entitySetName, entitySetName, get, keys);
    }

    public <TEntity> void register(final Class<TEntity> entityClass, final PropertyModel propertyModel, final String entitySetName, final String entityTypeName, final Func<Iterable<TEntity>> get,
            final String... keys) {

        InMemoryEntityInfo<TEntity> ei = new InMemoryEntityInfo<TEntity>();
        ei.entitySetName = entitySetName;
        ei.entityTypeName = entityTypeName;
        ei.properties = propertyModel;
        ei.get = get;
        ei.keys = keys;
        ei.entityClass = entityClass;
        ei.hasStream = OAtomStreamEntity.class.isAssignableFrom(entityClass);

        ei.id = new Func1<Object, HashMap<String, Object>>() {
            
            public HashMap<String, Object> apply(Object input) {
                HashMap<String, Object> values = new HashMap<String, Object>();
                for (String key : keys) {
                    values.put(key, eis.get(entitySetName).properties.getPropertyValue(input, key));
                }
                return values;
            }
        };

        eis.put(entitySetName, ei);
        metadata = null;
    }

    private InMemoryComplexTypeInfo<?> findComplexTypeInfoForClass(Class<?> clazz) {
        for (InMemoryComplexTypeInfo<?> typeInfo : this.complexTypes.values()) {
            if (typeInfo.entityClass.equals(clazz)) {
                return typeInfo;
            }
        }

        return null;
    }

    /**
     * transforms a POJO into a list of OProperties based on a given EdmStructuralType.
     * 
     * @param obj             the POJO to transform
     * @param propertyModel   the PropertyModel to use to access POJO class structure and values.
     * @param structuralType  the EdmStructuralType 
     * @param properties      put properties into this list.
     */
    @SuppressWarnings("unchecked")
    protected void addPropertiesFromObject(Object obj, PropertyModel propertyModel, EdmStructuralType structuralType, List<OProperty<?>> properties) {
        for (Iterator<EdmProperty> it = structuralType.getProperties().iterator(); it.hasNext();) {
            EdmProperty property = it.next();
            Object value = propertyModel.getPropertyValue(obj, property.getName());
            if (value == null && !this.includeNullPropertyValues) {
                // this is not permitted by the spec but makes debugging wide entity types
                // much easier.
                continue;
            }

            if (property.getCollectionKind() == CollectionKind.NONE) {
                if (property.getType().isSimple()) {
                    properties.add(OProperties.simple(property.getName(), (EdmSimpleType) property.getType(), value));
                } else {
                    // complex. 
                    if (value == null) {
                        properties.add(OProperties.complex(property.getName(), (EdmComplexType) property.getType(), null));
                    } else {
                        Class<?> propType = propertyModel.getPropertyType(property.getName());
                        InMemoryComplexTypeInfo<?> typeInfo = findComplexTypeInfoForClass(propType);
                        if (null == typeInfo) {
                            continue;
                        }
                        List<OProperty<?>> cprops = new ArrayList<OProperty<?>>();
                        addPropertiesFromObject(value, typeInfo.propertyModel, (EdmComplexType) property.getType(), cprops);
                        properties.add(OProperties.complex(property.getName(), (EdmComplexType) property.getType(), cprops));
                    }
                }
            } else {
                // collection.
                Iterable<?> values = propertyModel.getCollectionValue(obj, property.getName());
                OCollection.Builder<OObject> b = OCollections.newBuilder(property.getType());
                if (values != null) {
                    Class<?> propType = propertyModel.getCollectionElementType(property.getName());
                    InMemoryComplexTypeInfo<?> typeInfo = property.getType().isSimple() ? null : findComplexTypeInfoForClass(propType);
                    if ((!property.getType().isSimple()) && null == typeInfo) {
                        continue;
                    }
                    for (Object v : values) {
                        if (property.getType().isSimple()) {
                            b.add(OSimpleObjects.create((EdmSimpleType) property.getType(), v));
                        } else {
                            List<OProperty<?>> cprops = new ArrayList<OProperty<?>>();
                            addPropertiesFromObject(v, typeInfo.propertyModel, (EdmComplexType) property.getType(), cprops);
                            b.add(OComplexObjects.create((EdmComplexType) property.getType(), cprops));
                        }
                    }
                }
                properties.add(OProperties.collection(property.getName(),
                // hmmmh...is something is wrong here if I have to create a new EdmCollectionType?
                            new EdmCollectionType(CollectionKind.Collection, property.getType()),
                            b.build()));
            }
        }
    }

    protected OEntity toOEntity(EdmEntitySet ees, Object obj, List<EntitySimpleProperty> expand) {
        InMemoryEntityInfo<?> ei = eis.get(ees.getName());
        final List<OLink> links = new ArrayList<OLink>();
        final List<OProperty<?>> properties = new ArrayList<OProperty<?>>();

        Map<String, Object> keyKVPair = new HashMap<String, Object>();
        for (String key : ei.keys) {
            Object keyValue = ei.properties.getPropertyValue(obj, key);
            keyKVPair.put(key, keyValue);
        }

        addPropertiesFromObject(obj, ei.properties, ees.getType(), properties);

        if (expand != null && !expand.isEmpty()) {
            EdmEntityType edmEntityType = ees.getType();

            HashMap<String, List<EntitySimpleProperty>> expandedProps = new HashMap<String, List<EntitySimpleProperty>>();

            //process all the expanded properties and add them to map
            for (final EntitySimpleProperty propPath : expand) {
                String[] props = propPath.getPropertyName().split("/", 2);
                String prop = props[0];
                String remainingPropPath = props.length > 1 ? props[1] : null;
                //if link is already set to be expanded, add other remaining prop path to the list
                if (expandedProps.containsKey(prop)) {
                    if (remainingPropPath != null) {
                        List<EntitySimpleProperty> remainingPropPaths = expandedProps.get(prop);
                        remainingPropPaths.add(Expression.simpleProperty(remainingPropPath));
                    }
                } else {
                    List<EntitySimpleProperty> remainingPropPaths = new ArrayList<EntitySimpleProperty>();
                    if (remainingPropPath != null)
                        remainingPropPaths.add(Expression.simpleProperty(remainingPropPath));
                    expandedProps.put(prop, remainingPropPaths);
                }
            }

            for (final String prop : expandedProps.keySet()) {
                List<EntitySimpleProperty> remainingPropPath = expandedProps.get(prop);

                EdmNavigationProperty edmNavProperty = edmEntityType.findNavigationProperty(prop);

                if (edmNavProperty == null)
                    continue;

                if (edmNavProperty.getToRole().getMultiplicity() == EdmMultiplicity.MANY) {
                    List<OEntity> relatedEntities = new ArrayList<OEntity>();
                    Iterable<?> values = ei.properties.getCollectionValue(obj, prop);
                    if (values != null) {
                        EdmEntitySet relEntitySet = null;

                        for (final Object entity : values) {
                            if (relEntitySet == null) {
                                InMemoryEntityInfo<?> oei = IterableEnumerable.of(eis.values()).firstOrNull(new Predicate<InMemoryEntityInfo<?>>() {
                                    
                                    public boolean apply(InMemoryEntityInfo<?> input) {
                                        return entity.getClass().equals(input.entityClass);
                                    }
                                });
                                relEntitySet = getMetadata().getEdmEntitySet(oei.entitySetName);
                            }

                            relatedEntities.add(toOEntity(relEntitySet, entity, remainingPropPath));
                        }
                    }
                    // relation and href will be filled in later for atom or json
                    links.add(OLinks.relatedEntitiesInline(null, edmNavProperty.getName(), null, relatedEntities));
                } else {
                    final Object entity = ei.properties.getPropertyValue(obj, prop);
                    OEntity relatedEntity = null;

                    if (entity != null) {
                        InMemoryEntityInfo<?> oei = IterableEnumerable.of(eis.values()).firstOrNull(new Predicate<InMemoryEntityInfo<?>>() {
                            
                            public boolean apply(InMemoryEntityInfo<?> input) {
                                return entity.getClass().equals(input.entityClass);
                            }
                        });

                        EdmEntitySet relEntitySet = getMetadata().getEdmEntitySet(oei.entitySetName);

                        relatedEntity = toOEntity(relEntitySet, entity, remainingPropPath);
                    }
                    links.add(OLinks.relatedEntityInline(null, edmNavProperty.getName(), null, relatedEntity));
                }
            }
        }

        // for every navigation propety that we didn' expand we must place an deferred
        // OLink if the nav prop is selected
        for (final EdmNavigationProperty ep : ees.getType().getNavigationProperties()) {
            // if $select is ever supported, check here and only include nave props
            // that are selected
            boolean expanded = IterableEnumerable.of(links).any(new Predicate<OLink>() {
                
                public boolean apply(OLink t) {
                    return t.getTitle().equals(ep.getName());
                }
            });

            if (!expanded) {
                // defer
                if (ep.getToRole().getMultiplicity() == EdmMultiplicity.MANY) {
                    links.add(OLinks.relatedEntities(null, ep.getName(), null));
                } else {
                    links.add(OLinks.relatedEntity(null, ep.getName(), null));
                }
            }
        }

        return OEntities.create(ees, OEntityKey.create(keyKVPair), properties, links, obj);
    }

    private static Predicate<Object> filterToPredicate(final BoolCommonExpression filter, final PropertyModel properties) {
        return new Predicate<Object>() {
            public boolean apply(Object input) {
                return InMemoryEvaluation.evaluate(filter, input, properties);
            }
        };
    }

    
    public EntitiesResponse getEntities(String entitySetName, final QueryInfo queryInfo) {
        final EdmEntitySet ees = getMetadata().getEdmEntitySet(entitySetName);
        final InMemoryEntityInfo<?> ei = eis.get(entitySetName);

        IterableEnumerable<Object> objects = IterableEnumerable.of(ei.get.apply()).cast(Object.class);

        // evaluate filter
        if (queryInfo != null && queryInfo.filter != null) {
            objects = objects.where(filterToPredicate(queryInfo.filter, ei.properties));
        }

        // compute inlineCount, must be done after evaluateing filter
        Integer inlineCount = null;
        if (queryInfo != null && queryInfo.inlineCount == InlineCount.ALLPAGES) {
            objects = IterableEnumerable.of(objects.toList()); // materialize up front, since we're about to count
            inlineCount = objects.size();
        }

        // evaluate ordering
        if (queryInfo != null && queryInfo.orderBy != null) {
            objects = orderBy(objects, queryInfo.orderBy, ei.properties);
        }

        // work with oentities
        IterableEnumerable<OEntity> entities = objects.select(new Func1<Object, OEntity>() {
            public OEntity apply(Object input) {
                return toOEntity(ees, input, queryInfo != null ? queryInfo.expand : null);
            }
        });

        // skip records by $skipToken
        if (queryInfo != null && queryInfo.skipToken != null) {
            final Boolean[] skipping = new Boolean[] { true };
            entities = entities.skipWhile(new Predicate<OEntity>() {
                public boolean apply(OEntity input) {
                    if (skipping[0]) {
                        String inputKey = input.getEntityKey().toKeyString();
                        if (queryInfo.skipToken.equals(inputKey))
                            skipping[0] = false;
                        return true;
                    }
                    return false;
                }
            });
        }

        // skip records by $skip amount
        if (queryInfo != null && queryInfo.skip != null) {
            entities = entities.skip(queryInfo.skip);
        }

        // evaluate limit
        int limit = this.maxResults;
        if (queryInfo != null && queryInfo.top != null && queryInfo.top < limit) {
            limit = queryInfo.top;
        }
        entities = entities.take(limit + 1);

        // materialize OEntities
        List<OEntity> entitiesList = entities.toList();

        // determine skipToken if necessary
        String skipToken = null;
        if (entitiesList.size() > limit) {
            entitiesList = IterableEnumerable.of(entitiesList).take(limit).toList();
            skipToken = entitiesList.size() == 0 ? null : IterableEnumerable.of(entitiesList).last().getEntityKey().toKeyString();
        }

        return Responses.entities(entitiesList, ees, inlineCount, skipToken);

    }

    
    public CountResponse getEntitiesCount(String entitySetName, final QueryInfo queryInfo) {
        final EdmEntitySet ees = getMetadata().getEdmEntitySet(entitySetName);
        final InMemoryEntityInfo<?> ei = eis.get(entitySetName);

        IterableEnumerable<Object> objects = IterableEnumerable.of(ei.get.apply()).cast(Object.class);

        // evaluate filter
        if (queryInfo != null && queryInfo.filter != null) {
            objects = objects.where(filterToPredicate(queryInfo.filter, ei.properties));
        }

        // inlineCount is not applicable to $count queries
        if (queryInfo != null && queryInfo.inlineCount == InlineCount.ALLPAGES) {
            throw new UnsupportedOperationException("$inlinecount cannot be applied to the resource segment '$count'");
        }

        // ignore ordering for count

        // work with oentities.
        IterableEnumerable<OEntity> entities = objects.select(new Func1<Object, OEntity>() {
            public OEntity apply(Object input) {
                return toOEntity(ees, input, queryInfo != null ? queryInfo.expand : null);
            }
        });

        // skipToken is not applicable to $count queries
        if (queryInfo != null && queryInfo.skipToken != null) {
            throw new UnsupportedOperationException("Skip tokens can only be provided for requests that return collections of entities.");
        }

        // skip records by $skip amount
        // http://services.odata.org/Northwind/Northwind.svc/Customers/$count/?$skip=5
        if (queryInfo != null && queryInfo.skip != null) {
            entities = entities.skip(queryInfo.skip);
        }

        // evaluate $top.  maxResults is not applicable to $count but $top is.
        // http://services.odata.org/Northwind/Northwind.svc/Customers/$count/?$top=55
        int limit = Integer.MAX_VALUE;
        if (queryInfo != null && queryInfo.top != null && queryInfo.top < limit) {
            limit = queryInfo.top;
        }
        entities = entities.take(limit);

        return Responses.count(entities.size());
    }

    private IterableEnumerable<Object> orderBy(IterableEnumerable<Object> iter, List<OrderByExpression> orderBys, final PropertyModel properties) {
        for (final OrderByExpression orderBy : IterableEnumerable.of(orderBys).reverse())
            iter = iter.orderBy(new Comparator<Object>() {
                @SuppressWarnings("unchecked")
                public int compare(Object o1, Object o2) {
                    Comparable lhs = (Comparable) InMemoryEvaluation.evaluate(orderBy.getExpression(), o1, properties);
                    Comparable rhs = (Comparable) InMemoryEvaluation.evaluate(orderBy.getExpression(), o2, properties);
                    return (orderBy.getDirection() == Direction.ASCENDING ? 1 : -1) * lhs.compareTo(rhs);
                }
            });
        return iter;
    }

    
    public EntityResponse getEntity(String entitySetName, final OEntityKey entityKey, EntityQueryInfo queryInfo) {
        final Object rt = getEntity(entitySetName, entityKey);
        if (rt == null)
            throw new NotFoundException();

        final EdmEntitySet ees = getMetadata().getEdmEntitySet(entitySetName);
        OEntity oe = toOEntity(ees, rt, queryInfo.expand);

        return Responses.entity(oe);
    }

    
    public void mergeEntity(String entitySetName, OEntity entity) {
        throw new NotImplementedException();
    }

    
    public void updateEntity(String entitySetName, OEntity entity) {
        throw new NotImplementedException();
    }

    
    public void deleteEntity(String entitySetName, OEntityKey entityKey) {
        throw new NotImplementedException();
    }

    
    public EntityResponse createEntity(String entitySetName, OEntity entity) {
        throw new NotImplementedException();
    }

    
    public EntityResponse createEntity(String entitySetName, OEntityKey entityKey, String navProp, OEntity entity) {
        throw new NotImplementedException();
    }

    
    public BaseResponse getNavProperty(String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
        EdmEntitySet edmEntitySet = getMetadata().getEdmEntitySet(entitySetName); // throws NotFoundException
        // currently only properties are supported
        EdmProperty edmProperty = edmEntitySet.getType().findProperty(navProp);
        if (edmProperty == null)
            throw new NotFoundException("Property " + navProp + " is not found");
        // currently only simple types are supported
        EdmType edmType = edmProperty.getType();
        if (!edmType.isSimple())
            throw new NotImplementedException("Only simple types are supported. Property type is '" + edmType.getFullyQualifiedTypeName() + "'");

        // get property value...
        InMemoryEntityInfo<?> entityInfo = eis.get(entitySetName);
        Object target = getEntity(entitySetName, entityKey);
        Object propertyValue = entityInfo.properties.getPropertyValue(target, navProp);
        // ... and create OProperty
        OProperty<?> property = OProperties.simple(navProp, (EdmSimpleType<?>) edmType, propertyValue);

        return Responses.property(property);
    }

    
    public CountResponse getNavPropertyCount(String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
        throw new NotImplementedException();
    }

    
    public EntityIdResponse getLinks(OEntityId sourceEntity, String targetNavProp) {
        throw new NotImplementedException();
    }

    
    public void createLink(OEntityId sourceEntity, String targetNavProp, OEntityId targetEntity) {
        throw new NotImplementedException();
    }

    
    public void updateLink(OEntityId sourceEntity, String targetNavProp, OEntityKey oldTargetEntityKey, OEntityId newTargetEntity) {
        throw new NotImplementedException();
    }

    
    public void deleteLink(OEntityId sourceEntity, String targetNavProp, OEntityKey targetEntityKey) {
        throw new NotImplementedException();
    }

    
    public BaseResponse callFunction(EdmFunctionImport name, java.util.Map<String, OFunctionParameter> params, QueryInfo queryInfo) {
        throw new NotImplementedException();
    }

    @SuppressWarnings("unchecked")
    private Object getEntity(String entitySetName, final OEntityKey entityKey) {
        final InMemoryEntityInfo<?> ei = eis.get(entitySetName);

        final String[] keyList = ei.keys;

        Iterable<Object> iter = (Iterable<Object>) ei.get.apply();

        final Object rt = IterableEnumerable.of(iter).firstOrNull(new Predicate<Object>() {
            public boolean apply(Object input) {
                HashMap<String, Object> idObjectMap = ei.id.apply(input);

                if (keyList.length == 1) {
                    Object idValue = entityKey.asSingleValue();
                    return idObjectMap.get(keyList[0]).equals(idValue);
                } else if (keyList.length > 1) {
                    for (String key : keyList) {
                        Object curValue = null;
                        Iterator<OProperty<?>> keyProps = entityKey.asComplexProperties().iterator();
                        while (keyProps.hasNext()) {
                            OProperty<?> keyProp = keyProps.next();
                            if (keyProp.getName().equalsIgnoreCase(key)) {
                                curValue = keyProp.getValue();
                            }
                        }
                        if (curValue == null) {
                            return false;
                        } else if (!idObjectMap.get(key).equals(curValue)) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        return rt;
    }
}
