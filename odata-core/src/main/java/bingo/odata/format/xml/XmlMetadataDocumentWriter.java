/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bingo.odata.format.xml;

import static bingo.meta.edm.EdmUtils.fullQualifiedName;
import bingo.lang.Strings;
import bingo.lang.http.HttpContentTypes;
import bingo.lang.http.HttpMethods;
import bingo.lang.xml.XmlWriter;
import bingo.meta.edm.EdmAssociation;
import bingo.meta.edm.EdmAssociationSet;
import bingo.meta.edm.EdmComplexType;
import bingo.meta.edm.EdmEntityContainer;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmEntityTypeRef;
import bingo.meta.edm.EdmEnumMember;
import bingo.meta.edm.EdmEnumType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmNavigationProperty;
import bingo.meta.edm.EdmParameter;
import bingo.meta.edm.EdmProperty;
import bingo.meta.edm.EdmSchema;
import bingo.meta.edm.EdmSimpleType;
import bingo.meta.edm.EdmType;
import bingo.meta.edm.EdmUtils;
import bingo.meta.edm.EdmFeedCustomization.SyndicationTextContentKind;
import bingo.odata.ODataVersion;
import bingo.odata.ODataWriterContext;
import bingo.odata.ODataServices;
import bingo.odata.format.ODataXmlWriter;
import bingo.odata.utils.InternalTypeUtils;

public class XmlMetadataDocumentWriter extends ODataXmlWriter<ODataServices> {
	
	@Override
    public String getContentType() {
	    return HttpContentTypes.APPLICATION_XML_UTF8;
    }

	@Override
    protected void write(ODataWriterContext context, XmlWriter writer, ODataServices services) throws Throwable {
		writer.startDocument();
		
		//Edmx
		writer.startElement(EDMX_PREFIX, EDMX_NS, "Edmx")
			  .namespace(EDMX_PREFIX,EDMX_NS)
			  .attribute("Version", "1.0");

		//DataServides
		writer.startElement(EDMX_NS,"DataServices")
		      .attribute(METADATA_PREFIX,METADATA_NS,"DataServiceVersion",context.getVersion().getValue())
		      .namespace(DATASERVICES_PREFIX,DATASERVICES_NS)
		      .namespace(METADATA_PREFIX,METADATA_NS)
		      .namespace(EXTEND_METADATA_PREFIX, EXTEND_METADATA_NS);

		//Schemas
		for(EdmSchema schema : services.getSchemas()){
			writer.startElement("Schema")
				  .namespace(EDM2007_NS)
				  .attributeOptional("Namespace", schema.getNamespaceName());

			writeDocument(writer, schema);

			//EntityType
			writeEntityTypes(context,writer, schema);
			
			//Association
			writeAssociations(writer, schema);
			
			//ComplexType
			writeComplexTypes(writer, schema);
			
			//EnumType
			writeEnumTypes(writer,schema);
			
			//Function
			writeFunctions(writer, schema);
			
			//EntityContainer
			writeEntityContainers(context,writer, schema);
		}
		
		writer.endDocument();
		writer.close();
    }

	private static void writeEntityTypes(ODataWriterContext context,XmlWriter writer,EdmSchema schema) {
		for(EdmEntityType entityType : schema.getEntityTypes()){
			writer.startElement("EntityType")
				  .attribute("Name", entityType.getName());
			
			if(entityType.isAbstract()){
				writer.attribute("Abstract", "true");
			}
			
			if(entityType.hasStream()){
				writer.attribute(METADATA_NS, "HasStream","true");
			}
			
			if(context.getVersion().isGreaterThanOrEqualsTo(ODataVersion.V3)){
				if(entityType.isOpenType()){
					writer.attribute("OpenType","true");
				}
			}
			
			//key
			if(null == entityType.getBaseType()){
				if(!Strings.isEmpty(entityType.getTitle())){
					writer.attribute(EXTEND_METADATA_NS,"Title",entityType.getTitle());
				}
				
				writeDocument(writer, entityType);
				
				writer.startElement("Key");

				for(String key : entityType.getKeys()){
					writer.startElement("PropertyRef")
					      .attribute("Name", key)
					      .endElement();
				}
				
				writer.endElement();
			}else{
				writer.attribute("BaseType", fullQualifiedName(schema,(EdmType)entityType.getBaseType()));
				
				if(!Strings.isEmpty(entityType.getTitle())){
					writer.attribute(EXTEND_METADATA_NS,"Title",entityType.getTitle());
				}				
				
				writeDocument(writer, entityType);
			}
			
			writeProperties(writer,schema,entityType.getDeclaredProperties());
			
			for(EdmNavigationProperty navProp : entityType.getDeclaredNavigationProperties()) {
				writer.startElement("NavigationProperty")
				      .attribute("Name", navProp.getName())
				      .attribute("Relationship", fullQualifiedName(schema, navProp.getRelationship()))
				      .attribute("FromRole", navProp.getFromRole().getRole())
				      .attribute("ToRole", navProp.getToRole().getRole());
				
				writeDocument(writer,navProp);
				
				writer.endElement();
			}

			writer.endElement();
		}
	}
	
	private static void writeAssociations(XmlWriter writer,EdmSchema schema) {
		for(EdmAssociation assoc : schema.getAssociations()) {
			writer.startElement("Association")
				  .attribute("Name", assoc.getName());
			
			writeDocument(writer, assoc);

			writer.startElement("End")
			      .attribute("Role", assoc.getEnd1().getRole())
			      .attribute("Type", assoc.getEnd1().getType().getFullQualifiedName())
			      .attribute("Multiplicity", assoc.getEnd1().getMultiplicity().getValue())
			      .endElement();
			
			writer.startElement("End")
		      .attribute("Role", assoc.getEnd2().getRole())
		      .attribute("Type", assoc.getEnd2().getType().getFullQualifiedName())
		      .attribute("Multiplicity", assoc.getEnd2().getMultiplicity().getValue())
		      .endElement();
			
			
			writer.endElement();
		}
	}
	
	private static void writeProperties(XmlWriter writer,EdmSchema schema,Iterable<EdmProperty> properties) {
		for (EdmProperty prop : properties) {
			EdmType type = prop.getType();
			
			writer.startElement("Property");

			writer.attribute("Name",  prop.getName());
			
			writer.attribute("Type",  fullQualifiedName(schema,type));
			writer.attribute("Nullable", Boolean.toString(prop.isNullable()));

			if (EdmSimpleType.hasMaxLengthFacet(type) && prop.getMaxLength() > 0) {
				writer.attribute("MaxLength", Integer.toString(prop.getMaxLength()));
			}

			if (type.isSimple() && !Strings.isEmpty(prop.getDefaultValue())) {
				writer.attribute("DefaultValue", prop.getDefaultValue());
			}

			if (EdmSimpleType.hasPrecisionFacet(type) && prop.getPrecision() > 0) {
				writer.attribute("Precision", Integer.toString(prop.getPrecision()));
				
				if(EdmSimpleType.hasScaleFacet(type)){
					writer.attribute("Scale", Integer.toString(prop.getScale()));	
				}
			}
			
			if(EdmSimpleType.hasFixedLengthFacet(type) && prop.isFixedLength()){
				writer.attribute("FixedLength", Boolean.toString(prop.isFixedLength()));
			}
			
			if(!Strings.isEmpty(prop.getTitle())){
				writer.attribute(EXTEND_METADATA_QN_TITLE,prop.getTitle());
			}	
			
			if(!Strings.isEmpty(prop.getSerializeFormat())){
				writer.attribute(EXTEND_METADATA_QN_SERIALIZE_FORMAT,prop.getSerializeFormat());
			}
			
			if(!Strings.isEmpty(prop.getSerializeType())){
				writer.attribute(EXTEND_METADATA_QN_SERIALIZE_TYPE,prop.getSerializeType());
			}			
			
			//feed customerization attributes
			if(!Strings.isEmpty(prop.getFcTargetPath())){
				writer.attribute(METADATA_NS, "FC_TargetPath",prop.getFcTargetPath())
				      .attribute(METADATA_NS, "FC_CcontentKind",Strings.isEmpty(prop.getFcContentKind())? SyndicationTextContentKind.Text.getValue() : prop.getFcContentKind())
				      .attribute(METADATA_NS, "FC_KeepInContent",prop.isFcKeepInContent() ? "true" : "false");
			}
			
			writeDocument(writer,prop);
			
			writer.endElement();
		}
	}

	private static void writeEntityContainers(ODataWriterContext context, XmlWriter writer,EdmSchema schema) {
		for(EdmEntityContainer ec : schema.getEntityContainers()){
			
			writer.startElement("EntityContainer")
				  .attribute("Name", ec.getName());
			
			if(ec.isDefault()){
				writer.attribute(METADATA_NS,"IsDefaultEntityContainer","true");
			}
			
			writeDocument(writer, ec);
			
			//EntitySets
			for(EdmEntitySet es : ec.getEntitySets()){
				writer.startElement("EntitySet")
					  .attribute("Name", es.getName())
					  .attribute("EntityType",es.getEntityType().getFullQualifiedName());
				
				writeDocument(writer, es);
				
				writer.endElement();
			}
			
			//AssociationSets
			for(EdmAssociationSet as : ec.getAssociationSets()){
				writer.startElement("AssociationSet")
					  .attribute("Name", as.getName())
					  .attribute("Association",fullQualifiedName(schema, as.getAssociation()));
				
				writeDocument(writer, as);
				
				writer.startElement("End")
					  .attribute("Role", as.getEnd1().getRole())
					  .attribute("EntitySet",as.getEnd1().getEntitySet())
					  .endElement();
				
				writer.startElement("End")
					  .attribute("Role", as.getEnd2().getRole())
					  .attribute("EntitySet",as.getEnd2().getEntitySet())
					  .endElement();				
				
				writer.endElement();
			}
			
			//FunctionImports
			for(EdmFunctionImport func : ec.getFunctionImports()){
				writeFunctionImport(context,writer,schema,func);
			}
			
			writer.endElement();
		}
	}
	
	private static void writeComplexTypes(XmlWriter writer,EdmSchema schema) {
		for(EdmComplexType complexType : schema.getComplexTypes()){
			writer.startElement("ComplexType")
			      .attribute("Name", complexType.getName());
			
			writeProperties(writer, schema, complexType.getDeclaredProperties());
			
			writer.endElement();
		}
	}
	
	private static void writeEnumTypes(XmlWriter writer,EdmSchema schema) {
		for(EdmEnumType enumType : schema.getEnumTypes()){
			writer.startElement("EnumType")
				  .attribute("Name",enumType.getName())
				  .attribute("UnderlyingType",EdmUtils.fullQualifiedName(schema, enumType.getUnderlyingType()));
			
			if(enumType.isFlags()){
				writer.attribute("IsFlags","true");
			}
			
			for(EdmEnumMember member : enumType.getMembers()){
				writer.startElement("Member")
					  .attribute("Name",member.getName());
				
				if(!Strings.isEmpty(member.getValue())){
					writer.attribute("Value",InternalTypeUtils.toEdmString(member.getValue(),enumType.getUnderlyingType()));
				}
				
				writer.endElement();
			}
			
			writer.endElement();
		}
	}
	
	private static void writeFunctionImport(ODataWriterContext context,XmlWriter writer,EdmSchema schema,EdmFunctionImport func) {
		writer.startElement("FunctionImport")
		      .attribute("Name", func.getName());
		  
		writer.attributeOptional("EntitySet", func.getEntitySet());
		
		if(!(Strings.isEmpty(func.getHttpMethod()) || Strings.equals(HttpMethods.ALL, func.getHttpMethod()))){
			writer.attribute(METADATA_NS,"HttpMethod", func.getHttpMethod());
		}
		
		if(func.getReturnType() != null){
			writer.attribute("ReturnType",fullQualifiedName(schema, func.getReturnType()));
			
			if(func.getReturnType().isEntity()){
				writer.attribute("EntitySet",context.getServices().findEntitySet((EdmEntityType)func.getReturnType()).getName());
			}else if(func.getReturnType().isEntityRef()){
				writer.attribute("EntitySet",context.getServices().findEntitySet(((EdmEntityTypeRef)func.getReturnType()).getName()).getName());
			}
		}
		
		if(!func.isSideEffecting()){
			writer.attribute("IsSideEffecting","false");
		}
		
		if(!Strings.isEmpty(func.getTitle())){
			writer.attribute(EXTEND_METADATA_NS,"Title",func.getTitle());
		}
		
		/*
		if(!func.getParameters().isEmpty()){
			writer.attribute("IsBindable", "true");
		}
		*/
		  
		for(EdmParameter param : func.getParameters()){
			writer.startElement("Parameter")
			      .attribute("Name", param.getName());
			
			
			writer.attribute("Type", fullQualifiedName(schema, param.getType()))
			      .attribute("Mode", param.getMode().toString());
			
			if(null != param.getNullable()){
				writer.attribute(EXTEND_METADATA_QN_NULLABLE,param.getNullable() ? "true" : "false");	
			}
			
			if(!Strings.isEmpty(param.getTitle())){
				writer.attribute(EXTEND_METADATA_NS,"Title",param.getTitle());
			}
			
			if(!Strings.isEmpty(param.getSerializeFormat())){
				writer.attribute(EXTEND_METADATA_QN_SERIALIZE_FORMAT,param.getSerializeFormat());
			}
			
			if(!Strings.isEmpty(param.getSerializeType())){
				writer.attribute(EXTEND_METADATA_QN_SERIALIZE_TYPE,param.getSerializeType());
			}
			
			writer.endElement();
		}
		
		writer.endElement();
	}

	private static void writeFunctions(XmlWriter writer,EdmSchema schema) {
		//TODO : writeFunctions
	}
}