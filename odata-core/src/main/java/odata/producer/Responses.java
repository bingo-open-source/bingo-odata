package odata.producer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import odata.OCollection;
import odata.OComplexObject;
import odata.OEntity;
import odata.OEntityId;
import odata.OObject;
import odata.OProperty;
import odata.edm.EdmEntitySet;
import odata.edm.EdmMultiplicity;


/**
 * A static factory to create immutable {@link EntitiesResponse}, {@link EntityResponse}, {@link PropertyResponse}, {@link EntityIdResponse},
 * {@link ComplexObjectResponse}, or {@link CollectionResponse} instances.
 */
public class Responses {

    private Responses() {
    }

    /**
     * Creates a new <code>EntitiesResponse</code> instance.
     *
     * @param entities  the OData entities, if any
     * @param entitySet  the entity-set
     * @param inlineCount  the inline-count value, if necessary
     * @param skipToken  the continuation-token, if necessary
     * @return a new <code>EntitiesResponse</code> instance
     */
    public static EntitiesResponse entities(final List<OEntity> entities, final EdmEntitySet entitySet, final Integer inlineCount, final String skipToken) {
        return new EntitiesResponse() {

            
            public List<OEntity> getEntities() {
                return entities;
            }

            
            public EdmEntitySet getEntitySet() {
                return entitySet;
            }

            
            public Integer getInlineCount() {
                return inlineCount;
            }

            
            public String getSkipToken() {
                return skipToken;
            }
        };
    }

    public static CountResponse count(final long count) {
        return new CountResponse() {
            
            public long getCount() {
                return count;
            }
        };
    }

    /**
     * Creates a new <code>EntityResponse</code> instance.
     *
     * @param entity  the OData entity
     * @return a new <code>EntityResponse</code> instance
     */
    public static EntityResponse entity(final OEntity entity) {
        return new EntityResponse() {
            
            public OEntity getEntity() {
                return entity;
            }
        };
    }

    /**
     * Creates a new <code>PropertyResponse</code> instance.
     *
     * @param property  the property value
     * @return a new <code>PropertyResponse</code> instance
     */
    public static PropertyResponse property(final OProperty<?> property) {
        return new PropertyResponse() {
            
            public OProperty<?> getProperty() {
                return property;
            }
        };
    }

    /**
     * Creates a new <code>EntityIdResponse</code> instance for payloads with a cardinality of {@link EdmMultiplicity#ONE}.
     *
     * @param entityId  the payload entity
     * @return a new <code>EntityIdResponse</code> instance
     */
    public static <T extends OEntityId> EntityIdResponse singleId(T entityId) {
        final List<OEntityId> entities = new ArrayList<OEntityId>();
        entities.add(entityId);

        return new EntityIdResponse() {
            
            public EdmMultiplicity getMultiplicity() {
                return EdmMultiplicity.ONE;
            }

            
            public Collection<OEntityId> getEntities() {
                return entities;
            }
        };
    }

    /**
     * Creates a new <code>EntityIdResponse</code> instance for payloads with a cardinality of {@link EdmMultiplicity#MANY}.
     *
     * @param entityIds  the payload entities
     * @return a new <code>EntityIdResponse</code> instance
     */
    public static <T extends OEntityId> EntityIdResponse multipleIds(Iterable<T> entityIds) {
        final List<OEntityId> entities = new ArrayList<OEntityId>();
        for (T entityId : entityIds)
            entities.add(entityId);

        return new EntityIdResponse() {
            
            public EdmMultiplicity getMultiplicity() {
                return EdmMultiplicity.MANY;
            }

            
            public Collection<OEntityId> getEntities() {
                return entities;
            }
        };
    }

    /**
     * Creates a new <code>ComplexObjectResponse</code> instance.
     *
     * @param complexObject  the complex object
     * @return a new <code>ComplexObjectResponse</code> instance
     */
    public static ComplexObjectResponse complexObject(final OComplexObject complexObject) {
        return new ComplexObjectResponse() {
            
            public OComplexObject getObject() {
                return complexObject;
            }
        };
    }

    /**
     * Creates a new <code>CollectionResponse</code> instance.
     *
     * @param collection  the collection
     * @return a new <code>ComplexObjectResponse</code> instance
     */
    public static <T extends OObject> CollectionResponse<?> collection(final OCollection<T> collection) {
        return new CollectionResponse<T>() {
            
            public OCollection<T> getCollection() {
                return collection;
            }
        };
    }

}
