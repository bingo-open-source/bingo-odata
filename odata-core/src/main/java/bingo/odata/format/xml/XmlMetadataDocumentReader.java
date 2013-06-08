/**
 * file created at 2013-6-5
 */
package bingo.odata.format.xml;

import java.util.List;

import bingo.lang.Strings;
import bingo.lang.xml.XmlReader;
import bingo.meta.edm.EdmAssociation;
import bingo.meta.edm.EdmAssociationBuilder;
import bingo.meta.edm.EdmAssociationEnd;
import bingo.meta.edm.EdmAssociationSetBuilder;
import bingo.meta.edm.EdmAssociationSetEnd;
import bingo.meta.edm.EdmBuilderWithDocumentation;
import bingo.meta.edm.EdmCollectionType;
import bingo.meta.edm.EdmComplexTypeBuilder;
import bingo.meta.edm.EdmComplexTypeRef;
import bingo.meta.edm.EdmEntityContainerBuilder;
import bingo.meta.edm.EdmEntitySetBuilder;
import bingo.meta.edm.EdmEntityTypeBuilder;
import bingo.meta.edm.EdmEntityTypeRef;
import bingo.meta.edm.EdmEnumTypeBuilder;
import bingo.meta.edm.EdmFullQualifiedName;
import bingo.meta.edm.EdmFunctionImportBuilder;
import bingo.meta.edm.EdmMultiplicity;
import bingo.meta.edm.EdmNavigationPropertyBuilder;
import bingo.meta.edm.EdmParameterBuilder;
import bingo.meta.edm.EdmParameterMode;
import bingo.meta.edm.EdmProperty;
import bingo.meta.edm.EdmPropertyBuilder;
import bingo.meta.edm.EdmSchema;
import bingo.meta.edm.EdmSchemaBuilder;
import bingo.meta.edm.EdmSimpleType;
import bingo.meta.edm.EdmType;
import bingo.meta.edm.EdmUtils;
import bingo.odata.ODataException;
import bingo.odata.ODataReaderContext;
import bingo.odata.ODataServices;
import bingo.odata.ODataServicesBuilder;
import bingo.odata.ODataVersion;
import bingo.odata.format.ODataXmlReader;

public class XmlMetadataDocumentReader extends ODataXmlReader<ODataServices> {

	@Override
    protected ODataServices read(ODataReaderContext context, XmlReader reader) throws Throwable {
		ODataServices dataServices = null;
		
		while(reader.next()){
			if(reader.isStartElement("DataServices")){
				
				ODataServicesBuilder dataServicesBuilder = new ODataServicesBuilder();
				
				String versionString = reader.getAttributeValue("DataServiceVersion");
				
				dataServicesBuilder.setVersion(Strings.isEmpty(versionString) ? null : ODataVersion.parse(versionString));
				
				while(reader.next()){
					if(reader.isStartElement("Schema")){
						readSchema(context, reader, dataServicesBuilder);
					}
				}
				
				dataServices = dataServicesBuilder.build();
				break;
			}
		}
		
	    return dataServices;
    }
	
	protected List<EdmSchema> buildSchemas(List<EdmSchemaBuilder> schemas){
		return null;
	}
	
	protected void readSchema(ODataReaderContext context,XmlReader reader, ODataServicesBuilder dataServices) throws Throwable {
		EdmSchemaBuilder schema = new EdmSchemaBuilder();
		
		schema.setNamespace(reader.getAttributeValue("Namespace"));
		schema.setAlias(reader.getAttributeValue("Alias"));
		
		dataServices.addSchema(schema);
		
		while(reader.nextIfElementNotEnd("Schema")){
			if(readDocument(reader,schema)){
				continue;
			}
			
			if(reader.isStartElement("EntityType")){
				readEntityType(context, reader, dataServices, schema);
				continue;
			}
			
			if(reader.isStartElement("Association")){
				readAssociation(context, reader, dataServices, schema);
				continue;
			}
			
			if(reader.isStartElement("ComplexType")){
				readComplexType(context, reader, dataServices, schema);
				continue;
			}
			
			if(reader.isStartElement("EnumType")){
				readEnumType(context, reader, dataServices, schema);
				continue;
			}
			
			if(reader.isStartElement("EntityContainer")){
				readEntityContainer(context, reader, dataServices, schema);
			}
		}
	}
	
	protected void readEntityType(ODataReaderContext context,XmlReader reader,ODataServicesBuilder dataServices,EdmSchemaBuilder schema) throws Throwable {
		EdmEntityTypeBuilder entityType = new EdmEntityTypeBuilder();
		
		entityType.setName(reader.requiredGetAttributeValue("Name"));
		entityType.setFullQualifiedName(EdmUtils.fullQualifiedName(schema, entityType.getName()));
		entityType.setAbstract(reader.getAttributeValueForBool("Abstract", false));
		entityType.setHasStream(reader.getAttributeValueForBool("HasStream", false));
		entityType.setOpenType(reader.getAttributeValueForBool("OpenType", false));
		entityType.setTitle(reader.getAttributeValue(EXTEND_METADATA_QN_TITLE));
		
		String baseType = reader.getAttributeValue("BaseType");
		if(!Strings.isEmpty(baseType)){
			dataServices.addBaseType(entityType, baseType);
		}
		
		while(reader.nextIfElementNotEnd("EntityType")){
			if(readDocument(reader,entityType)){
				continue;
			}
			
			if(reader.isStartElement("Key")){
				readEntityTypeKey(context, reader, dataServices, schema, entityType);
				continue;
			}
			
			if(reader.isStartElement("Property")){
				readEntityTypeProperty(context, reader, dataServices, schema, entityType);
				continue;
			}
			
			if(reader.isStartElement("NavigationProperty")){
				readEntityTypeNavProperty(context, reader, dataServices, schema, entityType);
				continue;
			}
		}
		
		dataServices.addEntityType(schema, entityType);
	}
	
	protected boolean readDocument(XmlReader reader,EdmBuilderWithDocumentation item) throws Throwable{
		if(reader.isStartElement("Documentation")){
			String summary = null;
			String longDescription = null;
			while(reader.nextIfElementNotEnd("Documentation")){
				if(reader.isStartElement("Summary")){
					summary = reader.getElementText();
					continue;
				}
				if(reader.isStartElement("LongDescription")){
					longDescription = reader.getElementText();
					continue;
				}
			}
			if(!Strings.isEmpty(summary) || !Strings.isEmpty(longDescription)){
				item.setDocumentation(summary,longDescription);
			}
			return true;
		}
		return false;
	}
	
	protected void readEntityTypeKey(ODataReaderContext context,XmlReader reader,ODataServicesBuilder dataServices,EdmSchemaBuilder schema,EdmEntityTypeBuilder entityType) throws Throwable {
		while(reader.nextIfElementNotEnd("Key")){
			if(reader.isStartElement("PropertyRef")){
				entityType.addKey(reader.requiredGetAttributeValue("Name"));
			}
		}
	}
	
	protected void readEntityTypeProperty(ODataReaderContext context,XmlReader reader,ODataServicesBuilder dataServices,EdmSchemaBuilder schema,EdmEntityTypeBuilder entityType) throws Throwable {
		entityType.addProperty(readProperty(reader));
	}
	
	protected EdmProperty readProperty(XmlReader reader) throws Throwable {
		EdmPropertyBuilder prop = new EdmPropertyBuilder();
		
		prop.setName(reader.requiredGetAttributeValue("Name"));
		prop.setTitle(reader.getAttributeValue(EXTEND_METADATA_QN_TITLE));
		prop.setType(readEdmType(reader.requiredGetAttributeValue("Type")));
		prop.setNullable(reader.getAttributeValueForBool("Nullable",false));
		prop.setMaxLength(reader.getAttributeValueForInt("MaxLength", -1));
		prop.setDefaultValue(reader.getAttributeValue("DefaultValue"));
		prop.setPrecision(reader.getAttributeValueForInt("Precision", -1));
		prop.setScale(reader.getAttributeValueForInt("Scale",-1));
		prop.setFixedLength(reader.getAttributeValueForBool("FixedLength", false));
		prop.setFcTargetPath(reader.getAttributeValue("FC_TargetPath"));
		prop.setFcContentKind(reader.getAttributeValue("FC_CcontentKind"));
		prop.setFcKeepInContent(reader.getAttributeValueForBool("FC_KeepInContent", false));
		prop.setSerializeFormat(reader.getAttributeValue(EXTEND_METADATA_QN_SERIALIZE_FORMAT));
		prop.setSerializeType(reader.getAttributeValue(EXTEND_METADATA_QN_SERIALIZE_TYPE));
		
		while(reader.nextIfElementNotEnd("Property")){
			readDocument(reader, prop);
		}
		
		return prop.build();
	}
	
	protected void readEntityTypeNavProperty(ODataReaderContext context,XmlReader reader,ODataServicesBuilder dataServices,EdmSchemaBuilder schema,EdmEntityTypeBuilder entityType) throws Throwable {
		EdmNavigationPropertyBuilder navProp = new EdmNavigationPropertyBuilder();
		
		navProp.setName(reader.requiredGetAttributeValue("Name"));
		navProp.setRelationshipFullQualifiedName(reader.requiredGetAttributeValue("Relationship"));
		navProp.setFromRoleName(reader.requiredGetAttributeValue("FromRole"));
		navProp.setToRoleName(reader.requiredGetAttributeValue("ToRole"));
		
		while(reader.nextIfElementNotEnd("NavigationProperty")){
			readDocument(reader, navProp);
		}
		
		dataServices.addNavigationProperty(entityType, navProp);
	}
	
	protected void readAssociation(ODataReaderContext context,XmlReader reader,ODataServicesBuilder dataServices,EdmSchemaBuilder schema) throws Throwable {
		EdmAssociationBuilder assoc = new EdmAssociationBuilder();
		
		assoc.setName(reader.requiredGetAttributeValue("Name"));
		
		while(reader.nextIfElementNotEnd("Association")){
			if(readDocument(reader, assoc)){
				continue;
			}
			
			if(reader.isStartElement("End")){
				if(null == assoc.getEnd1()){
					assoc.setEnd1(readAssociationEnd(reader));
				}else{
					assoc.setEnd2(readAssociationEnd(reader));
				}
				continue;
			}
		}
		
		schema.addAssociation(assoc.build());
	}
	
	protected EdmAssociationEnd readAssociationEnd(XmlReader reader){
		String           role         = reader.requiredGetAttributeValue("Role");
		EdmEntityTypeRef type         = toEntityTypeRef(reader.requiredGetAttributeValue("Type"));
		EdmMultiplicity  multiplicity = EdmMultiplicity.parse(reader.requiredGetAttributeValue("Multiplicity"));

		return new EdmAssociationEnd(role, type, multiplicity);
	}
	
	protected void readComplexType(ODataReaderContext context,XmlReader reader,ODataServicesBuilder dataServices,EdmSchemaBuilder schema) throws Throwable {
		EdmComplexTypeBuilder complexType = new EdmComplexTypeBuilder();
		
		complexType.setName(reader.requiredGetAttributeValue("Name"));
		
		while(reader.nextIfElementNotEnd("ComplexType")){
			if(readDocument(reader, complexType)){
				continue;
			}
			
			if(reader.isStartElement("Property")){
				readComplexTypeProperty(context, reader, dataServices, schema, complexType);
				continue;
			}
		}
		
		schema.addComplexType(complexType.build());
	}
	
	protected void readComplexTypeProperty(ODataReaderContext context,XmlReader reader,ODataServicesBuilder dataServices,EdmSchemaBuilder schema,EdmComplexTypeBuilder complexType) throws Throwable {
		complexType.addProperty(readProperty(reader));
	}
	
	protected void readEnumType(ODataReaderContext context,XmlReader reader,ODataServicesBuilder dataServices,EdmSchemaBuilder schema) throws Throwable {
		EdmEnumTypeBuilder enumType = new EdmEnumTypeBuilder();

		enumType.setName(reader.requiredGetAttributeValue("Name"));
		enumType.setTitle(reader.getAttributeValue(EXTEND_METADATA_QN_TITLE));
		enumType.setFlags(reader.getAttributeValueForBool("IsFlags",false));
		enumType.setUnderlyingType((EdmSimpleType)readEdmType(reader.requiredGetAttributeValue("UnderlyingType")));
		
		while(reader.nextIfElementNotEnd("EnumType")){
			if(readDocument(reader, enumType)){
				continue;
			}

			if(reader.isStartElement("Member")){
				String name  = reader.requiredGetAttributeValue("Name");
				String value = reader.getAttributeValue("Value");
				
				enumType.addMember(name, value);
				continue;
			}
		}
		
		schema.addEnumType(enumType.build());
	}
	
	protected void readEntityContainer(ODataReaderContext context,XmlReader reader,ODataServicesBuilder dataServices,EdmSchemaBuilder schema) throws Throwable {
		EdmEntityContainerBuilder container = new EdmEntityContainerBuilder();
		
		container.setName(reader.getAttributeValue("Name"));
		container.setDefault(reader.getAttributeValueForBool("IsDefaultEntityContainer",false));
		
		while(reader.nextIfElementNotEnd("EntityContainer")){
			if(reader.isStartElement("EntitySet")){
				readEntitySet(context, reader, dataServices, schema, container);
				continue;
			}
			
			if(reader.isStartElement("AssociationSet")){
				readAssociationSet(context, reader, dataServices, schema, container);
				continue;
			}
			
			if(reader.isStartElement("FunctionImport")){
				readFunctionImport(context, reader, dataServices, schema, container);
				continue;
			}
		}
		
		schema.addEntityContainer(container.build());
	}
	
	protected void readEntitySet(ODataReaderContext context,XmlReader reader,ODataServicesBuilder dataServices,EdmSchemaBuilder schema,EdmEntityContainerBuilder container) throws Throwable {
		EdmEntitySetBuilder entitySet = new EdmEntitySetBuilder();
		
		entitySet.setName(reader.requiredGetAttributeValue("Name"));
		entitySet.setTitle(reader.getAttributeValue(EXTEND_METADATA_QN_TITLE));
		entitySet.setEntityType(toEntityTypeRef(reader.requiredGetAttributeValue("EntityType")));
		
		while(reader.nextIfElementNotEnd("EntitySet")){
			if(readDocument(reader, entitySet)){
				continue;
			}
		}

		container.addEntitySet(entitySet.build());
	}
	
	protected void readAssociationSet(ODataReaderContext context,XmlReader reader,ODataServicesBuilder dataServices,EdmSchemaBuilder schema,EdmEntityContainerBuilder container) throws Throwable {
		EdmAssociationSetBuilder assocSet = new EdmAssociationSetBuilder();
		
		assocSet.setName(reader.requiredGetAttributeValue("Name"));
		
		EdmFullQualifiedName fqName = new EdmFullQualifiedName(reader.requiredGetAttributeValue("Association"));
		
		EdmAssociation assoc = dataServices.findAssociation(fqName);
		if(null == assoc){
			throw new ODataException("association '" + fqName.getFqName() + "' not found in schema");
		}
		
		assocSet.setAssociation(assoc);
		
		while(reader.nextIfElementNotEnd("AssociationSet")){
			if(readDocument(reader, assocSet)){
				continue;
			}
			
			if(reader.isStartElement("End")){
				if(assocSet.getEnd1() == null){
					assocSet.setEnd1(readAssociationSetEnd(reader));
				}else{
					assocSet.setEnd2(readAssociationSetEnd(reader));
				}				
				continue;
			}
		}
		
		container.addAssociationSet(assocSet.build());
	}
	
	protected EdmAssociationSetEnd readAssociationSetEnd(XmlReader reader){
		String role      = reader.requiredGetAttributeValue("Role");
		String entitySet = reader.requiredGetAttributeValue("EntitySet");
		
		return new EdmAssociationSetEnd(role, entitySet);
	}	
	
	protected void readFunctionImport(ODataReaderContext context,XmlReader reader,ODataServicesBuilder dataServices,EdmSchemaBuilder schema,EdmEntityContainerBuilder container) throws Throwable {
		EdmFunctionImportBuilder func = new EdmFunctionImportBuilder();
		
		func.setName(reader.requiredGetAttributeValue("Name"));
		func.setTitle(reader.getAttributeValue(EXTEND_METADATA_QN_TITLE));
		func.setEntitySet(reader.getAttributeValue("EntitySet"));
		func.setHttpMethod(reader.getAttributeValue("HttpMethod"));
		
		String returnType = reader.getAttributeValue("ReturnType");
		if(!Strings.isEmpty(returnType)){
			func.setReturnType(readEdmType(returnType));
		}

		func.setSideEffecting(reader.getAttributeValueForBool("IsSideEffecting",true));
		
		while(reader.nextIfElementNotEnd("FunctionImport")){
			if(readDocument(reader, func)){
				continue;
			}
			
			if(reader.isStartElement("Parameter")){
				EdmParameterBuilder p = new EdmParameterBuilder();
				
				p.setName(reader.requiredGetAttributeValue("Name"));
				p.setType(readEdmType(reader.requiredGetAttributeValue("Type")));
				p.setMode(EdmParameterMode.valueOf(reader.requiredGetAttributeValue("Mode")));
				p.setTitle(reader.getAttributeValue(EXTEND_METADATA_QN_TITLE));
				p.setSerializeFormat(reader.getAttributeValue(EXTEND_METADATA_QN_SERIALIZE_FORMAT));
				p.setSerializeType(reader.getAttributeValue(EXTEND_METADATA_QN_SERIALIZE_TYPE));
				p.setNullable(reader.getAttributeValueForBool(EXTEND_METADATA_QN_NULLABLE));
				
				func.addParameter(p.build());
			}
		}
		
		container.addFunctionImport(func.build());
	}	
	
	protected EdmType readEdmType(String fqName){
		if(fqName.startsWith("Edm.")){
			return EdmSimpleType.of(fqName.substring(4));
		}
		
		if(fqName.startsWith("Collection(")){
			String elementTypeName = fqName.substring(12,fqName.length()-1);
			return new EdmCollectionType(readEdmType(elementTypeName));
		}
		
		int lastDotIndex = fqName.lastIndexOf(".");
		if(lastDotIndex > 0){
			String name = fqName.substring(lastDotIndex+1);
			return new EdmComplexTypeRef(name,fqName);
		}else{
			return new EdmComplexTypeRef(fqName,fqName);
		}
	}
	
	protected EdmEntityTypeRef toEntityTypeRef(String fqName){
		int lastDotIndex = fqName.lastIndexOf(".");
		if(lastDotIndex > 0){
			String name = fqName.substring(lastDotIndex+1);
			return new EdmEntityTypeRef(name,fqName);
		}else{
			return new EdmEntityTypeRef(fqName,fqName);
		}
	}
}