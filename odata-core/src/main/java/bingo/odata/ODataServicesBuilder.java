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
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmEntityTypeBuilder;
import bingo.meta.edm.EdmFullQualifiedName;
import bingo.meta.edm.EdmNavigationPropertyBuilder;
import bingo.meta.edm.EdmSchema;
import bingo.meta.edm.EdmSchemaBuilder;

public class ODataServicesBuilder {

	protected String                                                       name                 = ODataConstants.Defaults.DATA_SERVICE_NAME;
	protected ODataVersion                                                 version;
	protected List<EdmSchemaBuilder>                                       schemas              = new ArrayList<EdmSchemaBuilder>();
	protected Map<EdmEntityTypeBuilder,EdmFullQualifiedName>               baseTypes            = new HashMap<EdmEntityTypeBuilder, EdmFullQualifiedName>();
	protected Map<EdmSchemaBuilder,List<EdmEntityTypeBuilder>> 			   entityTypes 		    = new HashMap<EdmSchemaBuilder, List<EdmEntityTypeBuilder>>();
	protected Map<EdmEntityTypeBuilder,List<EdmNavigationPropertyBuilder>> navigationProperties = new HashMap<EdmEntityTypeBuilder, List<EdmNavigationPropertyBuilder>>();
	
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
	
	public Map<EdmEntityTypeBuilder,EdmFullQualifiedName> getBaseTypes() {
		return baseTypes;
	}

	public ODataServicesBuilder addBaseType(EdmEntityTypeBuilder entityType,String baseTypeName){
		baseTypes.put(entityType,new EdmFullQualifiedName(baseTypeName));
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
	
	public EdmSchemaBuilder findSchema(String namespace){
		for(EdmSchemaBuilder schema : schemas){
			if(Strings.isEmpty(namespace) && Strings.isEmpty(schema.getNamespace())){
				return schema;
			}
			
			if(namespace.equals(schema.getNamespace())){
				return schema;
			}
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

	public ODataServices build(){
		List<EdmSchema> buildedSchemas = new ArrayList<EdmSchema>();
		
		for(EdmSchemaBuilder schemaBuilder : schemas){
			buildedSchemas.add(build(schemaBuilder));
		}
		
		return new ODataServices(name, version, Enumerables.of(buildedSchemas));
	}
	
	protected EdmSchema build(EdmSchemaBuilder schema){
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
		
		return schema.build();
	}
	
	protected EdmEntityType build(EdmEntityTypeBuilder entityType){
		
		for(EdmNavigationPropertyBuilder navProperty : getEntityTypeNavigationProperties(entityType)){
			
			EdmFullQualifiedName fqName = new EdmFullQualifiedName(navProperty.getRelationshipFullQualifiedName());
			EdmAssociation relationship = findAssociation(fqName);
			
			if(null == relationship){
				throw new ODataException("relation '" + fqName.getFqName() + "' not found");
			}
			
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
}