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
	protected Map<Object,EdmFullQualifiedName>                             baseTypes            = new HashMap<Object, EdmFullQualifiedName>();
	protected Map<EdmSchemaBuilder,List<EdmEntityTypeBuilder>> 			   entityTypes 		    = new HashMap<EdmSchemaBuilder, List<EdmEntityTypeBuilder>>();
	protected Map<EdmSchemaBuilder,List<EdmComplexTypeBuilder>> 		   complexTypes 		= new HashMap<EdmSchemaBuilder, List<EdmComplexTypeBuilder>>();
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
	
	public Map<Object,EdmFullQualifiedName> getBaseTypes() {
		return baseTypes;
	}

	public ODataServicesBuilder addBaseType(Object type,String baseTypeName){
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
				schema.addComplexType(complexType.build());
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
						
						schema.addComplexType(complexType.build());
						
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
		
		return schema.build();
	}
	
	protected EdmEntityType build(EdmEntityTypeBuilder entityType){
		
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
}