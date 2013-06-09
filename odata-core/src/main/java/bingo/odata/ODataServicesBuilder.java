/**
 * file created at 2013-6-5
 */
package bingo.odata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bingo.lang.Enumerables;
import bingo.lang.Strings;
import bingo.meta.edm.EdmAssociation;
import bingo.meta.edm.EdmComplexType;
import bingo.meta.edm.EdmComplexTypeBuilder;
import bingo.meta.edm.EdmComplexTypeRef;
import bingo.meta.edm.EdmEntityContainer;
import bingo.meta.edm.EdmEntityContainerBuilder;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmEntityTypeBuilder;
import bingo.meta.edm.EdmEntityTypeRef;
import bingo.meta.edm.EdmFullQualifiedName;
import bingo.meta.edm.EdmFunctionImportBuilder;
import bingo.meta.edm.EdmNavigationPropertyBuilder;
import bingo.meta.edm.EdmParameterBuilder;
import bingo.meta.edm.EdmPropertyBuilder;
import bingo.meta.edm.EdmSchema;
import bingo.meta.edm.EdmSchemaBuilder;
import bingo.meta.edm.EdmStructualTypeBuilder;
import bingo.meta.edm.EdmType;
import bingo.meta.edm.EdmTypeRef;
import bingo.meta.edm.EdmUnresolvedTypeRef;

public class ODataServicesBuilder {

	protected String                                                        name                 = ODataConstants.Defaults.DATA_SERVICE_NAME;
	protected ODataVersion                                                  version;
	protected List<EdmSchemaBuilder>                                        schemas              = new ArrayList<EdmSchemaBuilder>();
	protected Map<EdmStructualTypeBuilder,EdmFullQualifiedName>             baseTypes            = new HashMap<EdmStructualTypeBuilder, EdmFullQualifiedName>();
	protected Map<EdmSchemaBuilder,List<EdmEntityTypeBuilder>> 			    entityTypes 		 = new HashMap<EdmSchemaBuilder, List<EdmEntityTypeBuilder>>();
	protected Map<EdmSchemaBuilder,List<EdmComplexTypeBuilder>> 		    complexTypes 		 = new HashMap<EdmSchemaBuilder, List<EdmComplexTypeBuilder>>();
	protected Map<EdmStructualTypeBuilder,List<EdmPropertyBuilder>>         properties           = new HashMap<EdmStructualTypeBuilder, List<EdmPropertyBuilder>>();
	protected Map<EdmEntityTypeBuilder,List<EdmNavigationPropertyBuilder>>  navigationProperties = new HashMap<EdmEntityTypeBuilder, List<EdmNavigationPropertyBuilder>>();
	protected Map<EdmSchemaBuilder,List<EdmEntityContainerBuilder>>         entityContainers     = new HashMap<EdmSchemaBuilder, List<EdmEntityContainerBuilder>>();
	protected Map<EdmEntityContainerBuilder,List<EdmFunctionImportBuilder>> functionImports      = new HashMap<EdmEntityContainerBuilder, List<EdmFunctionImportBuilder>>();
	protected Map<Object,List<EdmParameterBuilder>>                     parameters               = new HashMap<Object,List<EdmParameterBuilder>>(); 
	
	public ODataServicesBuilder(){
		
	}

	public String getName() {
		return name;
	}

	public ODataServicesBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public ODataVersion getVersion() {
		return version;
	}

	public ODataServicesBuilder setVersion(ODataVersion version) {
		this.version = version;
		return this;
	}
	
	public List<EdmSchemaBuilder> getSchemas() {
		return schemas;
	}

	public ODataServicesBuilder addSchema(EdmSchemaBuilder schema){
		schemas.add(schema);
		return this;
	}
	
	public Map<EdmStructualTypeBuilder,EdmFullQualifiedName> getBaseTypes() {
		return baseTypes;
	}

	public ODataServicesBuilder addBaseType(EdmStructualTypeBuilder type,String baseTypeName){
		baseTypes.put(type,new EdmFullQualifiedName(baseTypeName));
		return this;
	}
	
	public Map<EdmSchemaBuilder,List<EdmEntityTypeBuilder>> getEntityTypes() {
		return entityTypes;
	}

	public List<EdmEntityTypeBuilder> getSchemaEntityTypes(EdmSchemaBuilder schema){
		List<EdmEntityTypeBuilder> list = entityTypes.get(schema);
		
		if(null == list){
			list = new ArrayList<EdmEntityTypeBuilder>();
			entityTypes.put(schema,list);
		}
		
		return list;
	}
	
	public ODataServicesBuilder addEntityType(EdmSchemaBuilder schema,EdmEntityTypeBuilder entityType){
		getSchemaEntityTypes(schema).add(entityType);
		return this;
	}
	
	public Map<EdmSchemaBuilder,List<EdmComplexTypeBuilder>> getComplexTypes() {
		return complexTypes;
	}

	public List<EdmComplexTypeBuilder> getSchemaComplexTypes(EdmSchemaBuilder schema){
		List<EdmComplexTypeBuilder> list = complexTypes.get(schema);
		
		if(null == list){
			list = new ArrayList<EdmComplexTypeBuilder>();
			complexTypes.put(schema,list);
		}
		
		return list;
	}
	
	public ODataServicesBuilder addComplexType(EdmSchemaBuilder schema,EdmComplexTypeBuilder complexType){
		getSchemaComplexTypes(schema).add(complexType);
		return this;
	}	
	
	public Map<EdmStructualTypeBuilder, List<EdmPropertyBuilder>> getProperties() {
		return properties;
	}
	
	public List<EdmPropertyBuilder> getStructualTypeProperties(EdmStructualTypeBuilder type){
		List<EdmPropertyBuilder> list = properties.get(type);
		
		if(null == list){
			list = new ArrayList<EdmPropertyBuilder>();
			properties.put(type,list);
		}
		
		return list;
	}
	
	public void addProperty(EdmStructualTypeBuilder type,EdmPropertyBuilder property){
		getStructualTypeProperties(type).add(property);
	}

	public Map<EdmEntityTypeBuilder, List<EdmNavigationPropertyBuilder>> getNavigationProperties() {
		return navigationProperties;
	}
	
	public List<EdmNavigationPropertyBuilder> getEntityTypeNavigationProperties(EdmEntityTypeBuilder entityType){
		List<EdmNavigationPropertyBuilder> list = navigationProperties.get(entityType);
		
		if(null == list){
			list = new ArrayList<EdmNavigationPropertyBuilder>();
			navigationProperties.put(entityType,list);
		}
		
		return list;
	}
	
	public ODataServicesBuilder addNavigationProperty(EdmEntityTypeBuilder entityType,EdmNavigationPropertyBuilder navProp){
		getEntityTypeNavigationProperties(entityType).add(navProp);
		return this;
	}
	
	public Map<EdmSchemaBuilder, List<EdmEntityContainerBuilder>> getEntityContainers() {
		return entityContainers;
	}

	public List<EdmEntityContainerBuilder> getSchemaEntityContainers(EdmSchemaBuilder schema){
		List<EdmEntityContainerBuilder> list = entityContainers.get(schema);
		
		if(null == list){
			list = new ArrayList<EdmEntityContainerBuilder>();
			entityContainers.put(schema,list);
		}
		
		return list;
	}
	
	public void addEntityContainer(EdmSchemaBuilder schema,EdmEntityContainerBuilder entityContainer){
		getSchemaEntityContainers(schema).add(entityContainer);
	}
	
	public Map<EdmEntityContainerBuilder, List<EdmFunctionImportBuilder>> getFunctionImports() {
		return functionImports;
	}
	
	public List<EdmFunctionImportBuilder> getContainerFunctionImports(EdmEntityContainerBuilder container){
		List<EdmFunctionImportBuilder> list = functionImports.get(container);
		
		if(null == list){
			list = new ArrayList<EdmFunctionImportBuilder>();
			functionImports.put(container,list);
		}
		
		return list;
	}
	
	public void addFunctionImport(EdmEntityContainerBuilder container,EdmFunctionImportBuilder functionImport){
		getContainerFunctionImports(container).add(functionImport);
	}
	
	public Map<Object,List<EdmParameterBuilder>> getParameters() {
		return parameters;
	}

	public List<EdmParameterBuilder> getObjectParameters(Object object){
		List<EdmParameterBuilder> list = parameters.get(object);
		
		if(null == list){
			list = new ArrayList<EdmParameterBuilder>();
			parameters.put(object,list);
		}
		
		return list;
	}
	
	public void addParameter(Object object,EdmParameterBuilder parameter){
		getObjectParameters(object).add(parameter);
	}	
	
	public EdmSchemaBuilder findSchema(String namespace){
		for(EdmSchemaBuilder schema : schemas){
			if(Strings.isEmpty(namespace) && Strings.isEmpty(schema.getNamespace())){
				return schema;
			}
			
			if(namespace.equals(schema.getNamespace())){
				return schema;
			}
		}
		
		if(Strings.isEmpty(namespace) && schemas.size() == 1){
			return schemas.get(0);
		}
		
		return null;
	}
	
	public EdmAssociation findAssociation(EdmFullQualifiedName fqName){
		EdmSchemaBuilder schema = findSchema(fqName.getNamespace());
		return null == schema ? null : schema.findAssociation(fqName.getName());
	}
	
	public EdmEntityType findEntityType(EdmFullQualifiedName fqName){
		EdmSchemaBuilder schema = findSchema(fqName.getNamespace());
		return null == schema ? null : schema.findEntityType(fqName.getName());
	}
	
	public EdmComplexType findComplexType(EdmFullQualifiedName fqName){
		EdmSchemaBuilder schema = findSchema(fqName.getNamespace());
		return null == schema ? null : schema.findComplexType(fqName.getName());
	}	

	public ODataServices build(){
		List<EdmSchema> buildedSchemas = new ArrayList<EdmSchema>();
		
		for(EdmSchemaBuilder schemaBuilder : schemas){
			buildedSchemas.add(build(schemaBuilder));
		}
		
		return new ODataServices(name, version, Enumerables.of(buildedSchemas));
	}
	
	protected EdmSchema build(EdmSchemaBuilder schema){
		//complex types
		List<EdmComplexTypeBuilder> delayedComplexTypes = new ArrayList<EdmComplexTypeBuilder>();
		
		for(EdmComplexTypeBuilder complexType : getSchemaComplexTypes(schema)){
			if(baseTypes.containsKey(complexType)){
				delayedComplexTypes.add(complexType);
			}else{
				schema.addComplexType(build(complexType));
			}
		}
		
		if(!delayedComplexTypes.isEmpty()){
			while(true){
				EdmComplexTypeBuilder remove = null;
				
				for(EdmComplexTypeBuilder complexType : delayedComplexTypes){
					EdmFullQualifiedName baseTypeName = baseTypes.get(complexType);
					EdmComplexType baseType = findComplexType(baseTypeName);

					if(null != baseType){
						complexType.setBaseType(baseType);
						remove = complexType;
						
						schema.addComplexType(build(complexType));
						
						break;
					}
				}
				
				if(null == remove){
					throw new ODataException("invalid odata services metadata, some base types may be incorrect");
				}
				
				delayedComplexTypes.remove(remove);
				
				if(delayedComplexTypes.isEmpty()){
					break;
				}
			}
		}
		
		//entity types
		
		List<EdmEntityTypeBuilder> delayedEntityTypes = new ArrayList<EdmEntityTypeBuilder>();
		
		for(EdmEntityTypeBuilder entityType : getSchemaEntityTypes(schema)){
			if(baseTypes.containsKey(entityType)){
				delayedEntityTypes.add(entityType);
			}else{
				schema.addEntityType(build(entityType));
			}
		}
		
		if(!delayedEntityTypes.isEmpty()){
			while(true){
				EdmEntityTypeBuilder remove = null;
				
				for(EdmEntityTypeBuilder entityType : delayedEntityTypes){
					EdmFullQualifiedName baseTypeName = baseTypes.get(entityType);
					EdmEntityType baseType = findEntityType(baseTypeName);

					if(null != baseType){
						entityType.setBaseType(baseType);
						remove = entityType;
						
						schema.addEntityType(build(entityType));
						
						break;
					}
				}
				
				if(null == remove){
					throw new ODataException("invalid odata services metadata, some base types may be incorrect");
				}
				
				delayedEntityTypes.remove(remove);
				
				if(delayedEntityTypes.isEmpty()){
					break;
				}
			}
		}
		
		//entity containers
		buildSchemaContainers(schema);
		
		return schema.build();
	}
	
	protected EdmComplexType build(EdmComplexTypeBuilder complexType){
		builProperties(complexType);
		return complexType.build();
	}
	
	protected void builProperties(EdmStructualTypeBuilder structualType){
		List<EdmPropertyBuilder> properties = getStructualTypeProperties(structualType);
		
		for(EdmPropertyBuilder property : properties){
			EdmType type = property.getType();
			
			if(type instanceof EdmUnresolvedTypeRef){
				property.setType(resolve((EdmUnresolvedTypeRef)type));
			}
			
			structualType.addProperty(property.build());
		}
	}
	
	protected EdmEntityType build(EdmEntityTypeBuilder entityType){
		builProperties(entityType);
		
		for(EdmNavigationPropertyBuilder navProperty : getEntityTypeNavigationProperties(entityType)){
			
			EdmFullQualifiedName fqName = new EdmFullQualifiedName(navProperty.getRelationshipFullQualifiedName());
			EdmAssociation relationship = findAssociation(fqName);
			
			if(null == relationship){
				throw new ODataException("relation '" + fqName.getFqName() + "' not found");
			}
			
			navProperty.setRelationship(relationship);
			
			if(relationship.getEnd1().getRole().equals(navProperty.getFromRoleName())){
				navProperty.setFromRole(relationship.getEnd1());
				navProperty.setToRole(relationship.getEnd2());
			}else{
				navProperty.setFromRole(relationship.getEnd2());
				navProperty.setToRole(relationship.getEnd1());				
			}
			
			entityType.addNavigationProperty(navProperty.build());
		}
		
		return entityType.build();
	}
	
	protected void buildSchemaContainers(EdmSchemaBuilder schema){
		for(EdmEntityContainerBuilder container : getSchemaEntityContainers(schema)){
			schema.addEntityContainer(build(container));
		}
	}
	
	protected EdmEntityContainer build(EdmEntityContainerBuilder container){
		for(EdmFunctionImportBuilder functionImport : getContainerFunctionImports(container)){
			
			functionImport.setReturnType(resolve(functionImport.getReturnType()));
			
			for(EdmParameterBuilder parameter : getObjectParameters(functionImport)){
				parameter.setType(resolve(parameter.getType()));
				functionImport.addParameter(parameter.build());
			}
			
			container.addFunctionImport(functionImport.build());
		}
		
		return container.build();
	}
	
	protected EdmType resolve(EdmType type){
		if(null == type){
			return null;
		}
		
		if(type instanceof EdmUnresolvedTypeRef){
			return resolve((EdmUnresolvedTypeRef)type);
		}
		
		return type;
	}
	
	protected EdmTypeRef resolve(EdmUnresolvedTypeRef typeRef){
		EdmFullQualifiedName fqName = new EdmFullQualifiedName(typeRef.getFullQualifiedName());
		
		EdmSchemaBuilder schema = findSchema(fqName.getNamespace());
		
		for(EdmEntityTypeBuilder entityType : getSchemaEntityTypes(schema)){
			if(entityType.getName().equals(fqName.getName())){
				return new EdmEntityTypeRef(typeRef.getName(),typeRef.getFullQualifiedName());
			}
		}
		
		for(EdmComplexTypeBuilder complexType : getSchemaComplexTypes(schema)){
			if(complexType.getName().equals(fqName.getName())){
				return new EdmComplexTypeRef(typeRef.getName(),typeRef.getFullQualifiedName());
			}
		}
		
		throw new ODataException("can not resolve type '" + typeRef.getFullQualifiedName() + "'");
	}
}