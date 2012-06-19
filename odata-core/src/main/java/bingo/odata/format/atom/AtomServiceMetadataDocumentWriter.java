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
package bingo.odata.format.atom;

import static bingo.odata.format.ODataXmlConstants.DATA_SERVICES_METADATA_NS;
import static bingo.odata.format.ODataXmlConstants.DATA_SERVICES_METADATA_PREFIX;
import static bingo.odata.format.ODataXmlConstants.DATA_SERVICES_NS;
import static bingo.odata.format.ODataXmlConstants.DATA_SERVICES_PREFIX;
import static bingo.odata.format.ODataXmlConstants.EDMX_NS;
import static bingo.odata.format.ODataXmlConstants.EDMX_PREFIX;
import bingo.lang.Strings;
import bingo.lang.xml.XmlWriter;
import bingo.odata.ODataServices;
import bingo.odata.ODataContext;
import bingo.odata.edm.EdmAssociation;
import bingo.odata.edm.EdmAssociationSet;
import bingo.odata.edm.EdmEntityContainer;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.edm.EdmNavigationProperty;
import bingo.odata.edm.EdmProperty;
import bingo.odata.edm.EdmSchema;
import bingo.odata.edm.EdmUtils;
import bingo.odata.format.ODataXmlWriter;

public class AtomServiceMetadataDocumentWriter extends ODataXmlWriter<ODataServices> {

	@Override
    protected void write(ODataContext context, XmlWriter writer, ODataServices services) throws Throwable {
		writer.startDocument();
		
		//Edmx
		writer.startElement(EDMX_PREFIX, EDMX_NS, "Edmx")
			  .attribute("Version", "1.0")
			  .namespace(EDMX_PREFIX,EDMX_NS)
			  .namespace(DATA_SERVICES_PREFIX,DATA_SERVICES_NS)
			  .namespace(DATA_SERVICES_METADATA_PREFIX,DATA_SERVICES_METADATA_NS);

		//DataServides
		writer.startElement(EDMX_PREFIX,EDMX_NS,"DataServices")
		      .attribute(DATA_SERVICES_METADATA_PREFIX,DATA_SERVICES_METADATA_NS,"DataServiceVersion",services.getVersion().getValue());

		//Schemas
		for(EdmSchema schema : services.getSchemas()){
			writer.startElement(EDMX_NS, "Schema")
				  .attributeOptional("Namespace", schema.getNamespaceName());

			writeDocument(writer, schema);

			//EntityType
			writeEntityTypes(writer, schema);
			
			//Association
			writeAssociations(writer, schema);
			
			//ComplexType
			writeComplexTypes(writer, schema);
			
			//Function
			writeFunctions(writer, schema);
			
			//EntityContainer
			writeEntityContainers(writer, schema);
		}
		
		writer.endDocument();
		writer.close();
    }

	private static void writeEntityTypes(XmlWriter writer,EdmSchema schema) {
		for(EdmEntityType entityType : schema.getEntityTypes()){
			writer.startElement("EntityType")
				  .attribute("Name", entityType.getName());
			
			if(entityType.isAbstract()){
				writer.attribute("Abstract", "true");
			}
			
			if(entityType.hasStream()){
				writer.attribute(DATA_SERVICES_METADATA_NS, "HasStream","true");
			}

			//key
			if(null == entityType.getBaseType()){
				writeDocument(writer, entityType);
				
				writer.startElement("Key");

				for(String key : entityType.getKeys()){
					writer.startElement("PropertyRef")
					      .attribute("Name", key)
					      .endElement();
				}
				
				writer.endElement();
			}else{
				writer.attribute("BaseType", EdmUtils.getQualifiedName(schema, entityType.getBaseType()));
				
				writeDocument(writer, entityType);
			}
			
			writeProperties(writer,schema,entityType.getDeclaredProperties());
			
			for(EdmNavigationProperty navProp : entityType.getDeclaredNavigationProperties()) {
				writer.startElement("NavigationProperty")
				      .attribute("Name", navProp.getName())
				      .attribute("Relationship", navProp.getRelationship().getQualifiedName());
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
			      .attribute("Type", EdmUtils.getQualifiedName(schema, assoc.getEnd1().getType()))
			      .attribute("Multiplicity", assoc.getEnd1().getMultiplicity().getValue())
			      .endElement();
			
			writer.startElement("End")
		      .attribute("Role", assoc.getEnd2().getRole())
		      .attribute("Type", EdmUtils.getQualifiedName(schema, assoc.getEnd2().getType()))
		      .attribute("Multiplicity", assoc.getEnd2().getMultiplicity().getValue())
		      .endElement();
			
			
			writer.endElement();
		}
	}
	
	private static void writeProperties(XmlWriter writer,EdmSchema schema,Iterable<EdmProperty> properties) {
		for (EdmProperty prop : properties) {
			writer.startElement("Property");

			writer.attribute("Name",  prop.getName());
			writer.attribute("Type",  EdmUtils.getQualifiedName(schema, prop.getType()));
			writer.attribute("Nullable", Boolean.toString(prop.isNullable()));

			if (prop.getMaxLength() > 0) {
				writer.attribute("MaxLength", Integer.toString(prop.getMaxLength()));
			}

			if (!Strings.isEmpty(prop.getDefaultValue())) {
				writer.attribute("DefaultValue", prop.getDefaultValue());
			}

			if (prop.getPrecision() > 0) {
				writer.attribute("Precision", Integer.toString(prop.getPrecision()));
				writer.attribute("Scale", Integer.toString(prop.getPrecision()));
			}
			
			writer.endElement();
		}
	}

	private static void writeEntityContainers(XmlWriter writer,EdmSchema schema) {
		for(EdmEntityContainer ec : schema.getEntityContainers()){
			
			writer.startElement("EntityContainer")
				  .attribute("Name", ec.getName());
			
			if(ec.isDefault()){
				writer.attribute(DATA_SERVICES_METADATA_NS,"true");
			}
			
			writeDocument(writer, ec);
			
			//TODO : FunctionImports
			
			//EntitySets
			for(EdmEntitySet es : ec.getEntitySets()){
				writer.startElement("EntitySet")
					  .attribute("Name", es.getName())
					  .attribute("EntityType", es.getEntityType().getQualifiedName());
				
				writeDocument(writer, es);
				
				writer.endElement();
			}
			
			//AssociationSets
			for(EdmAssociationSet as : ec.getAssociationSets()){
				writer.startElement("AssociationSet")
					  .attribute("Name", as.getName())
					  .attribute("Association", as.getAssociation().getQualifiedName());
				
				writeDocument(writer, as);
				
				writer.startElement("End")
					  .attribute("Role", as.getEnd1().getRole())
					  .attribute("EntitySet",as.getEnd1().getEntitySet().getName())
					  .endElement();
				
				writer.startElement("End")
					  .attribute("Role", as.getEnd2().getRole())
					  .attribute("EntitySet",as.getEnd2().getEntitySet().getName())
					  .endElement();				
				
				writer.endElement();
			}
			
			writer.endElement();
		}
	}
	
	private static void writeComplexTypes(XmlWriter writer,EdmSchema schema) {
		//TODO : writeComplexTypes
		
	}

	private static void writeFunctions(XmlWriter writer,EdmSchema schema) {
		//TODO : writeFunctions
	}
}