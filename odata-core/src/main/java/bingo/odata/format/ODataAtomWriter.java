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
package bingo.odata.format;

import java.util.List;

import bingo.lang.Converts;
import bingo.lang.Strings;
import bingo.lang.xml.XmlWriter;
import bingo.odata.ODataConstants.ContentTypes;
import bingo.odata.ODataContext;
import bingo.odata.ODataConverts;
import bingo.odata.ODataObject;
import bingo.odata.ODataUtils;
import bingo.odata.data.ODataEntity;
import bingo.odata.data.ODataProperty;
import bingo.odata.edm.EdmFeedCustomization.SyndicationItemProperty;
import bingo.odata.edm.EdmFeedCustomization.SyndicationTextContentKind;
import bingo.odata.edm.EdmNavigationProperty;
import bingo.odata.edm.EdmProperty;
import bingo.odata.edm.EdmSimpleType;
import bingo.odata.edm.EdmType;

public abstract class ODataAtomWriter<T extends ODataObject> extends ODataXmlWriter<T> {

	@Override
    public String getContentType() {
	    return ContentTypes.APPLICATION_ATOM_XML_UTF8;
    }
	
	protected static void writeId(XmlWriter writer,String id){
		writer.startElement("id").text(id).endElement();
	}
	
	protected static void writeTitle(XmlWriter writer,String title) {
		writer.startElement("title").attribute("type", "text").text(title).endElement();
	}
	
	protected static void writeSummary(XmlWriter writer,String summary) {
		writer.startElement("summary").attribute("type", "text").text(summary).endElement();
	}

	protected static void writeTitle(XmlWriter writer,String title,String type) {
		writer.startElement("title").attribute("type", type).text(title).endElement();
	}
	
	protected static void writeSummary(XmlWriter writer,String summary,String type) {
		writer.startElement("summary").attribute("type", type).text(summary).endElement();
	}
	
	protected static void writeUpdated(XmlWriter writer,String updated) {
		writer.startElement("updated").text(updated).endElement();
	}
	
	protected static void writeLink(XmlWriter writer,String href,String rel){
		writer.startElement("link")
			   .attribute("href", href)
			   .attribute("rel",rel)
			   .endElement();		
	}
	
	protected static void writeLink(XmlWriter writer,String title,String href,String rel){
		writer.startElement("link")
			   .attribute("title",title)
			   .attribute("href", href)
			   .attribute("rel",rel)
			   .endElement();		
	}

	protected static void writeLink(XmlWriter writer,String title,String type,String href,String rel){
		writer.startElement("link")
			   .attribute("title",title)
			   .attribute("type", type)
			   .attribute("href", href)
			   .attribute("rel",rel)
			   .endElement();		
	}
	
	protected static void writeEntryBody(ODataContext context, XmlWriter writer,ODataEntity entity,String updated,boolean isFeed) {
		
		if(context.isProducer()){
			writer.startElement("id").text(ODataUtils.getEntryId(context.getUrlInfo(), entity)).endElement();
		}
		
		writeEntryFeedCustomizationAttributes(context,writer,entity,updated);
		
		//edit link
		if(context.isProducer()){
			writeLink(writer, entity.getEntityType().getName(), entity.getKeyString(), "edit");
		}
		
		writeEntryAssociationLinks(context, writer, entity, isFeed);
		
		//<category scheme="http://schemas.microsoft.com/ado/2007/08/dataservices/scheme" term="ODataDemo.Product"/>
		writer.startElement("category").attribute("schema", SCHEMA_NS)
									   .attribute("term", entity.getEntityType().getFullQualifiedName())
									   .endElement();
		
		//TODO : hasStream
		
		//content
		writer.startElement("content").attribute("type", ContentTypes.APPLICATION_XML);
		
		writer.startElement(METADATA_NS,"properties");
		
		writeProperties(context, writer, entity.getProperties());
		
		writer.endElement();
		writer.endElement();
	}
	
	protected static void writeEntryFeedCustomizationAttributes(ODataContext context,XmlWriter writer,ODataEntity entity,String updated){
		EdmProperty titleProperty   = null;
		Object      titleValue      = null;
		
		EdmProperty summaryProperty = null;
		Object      summaryValue    = null;
		
		for(ODataProperty p : entity.getProperties()){
			
			EdmProperty mp = p.getMetadata();
			
			if(SyndicationItemProperty.Title.equalsValue(mp.getFcTargetPath())){
				titleProperty = mp;
				titleValue    = p.getValue();
			}else if(SyndicationItemProperty.Summary.equalsValue(mp.getFcTargetPath())){
				summaryProperty = mp;
				summaryValue    = p.getValue();
			}
		}
		
		if(null != titleValue){
			String type = Strings.isEmpty(titleProperty.getFcContentKind()) ? SyndicationTextContentKind.Text.getValue() : titleProperty.getFcContentKind();
			
			writeTitle(writer, Converts.toString(titleValue),type);
		}
		
		if(null != summaryProperty){
			String type  = Strings.isEmpty(summaryProperty.getFcContentKind()) ? SyndicationTextContentKind.Text.getValue() : summaryProperty.getFcContentKind();
			
			writeSummary(writer, Converts.toString(summaryValue),type);
		}
		
		writeUpdated(writer,updated);
	}
	
	protected static void writeEntryAssociationLinks(ODataContext context,XmlWriter writer,ODataEntity entity, boolean isFeed){
		//<link title="Products" type="application/atom+xml;type=feed" href="Categories(0)/Products" 
		//		rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/Products"/>
		
		String type = isFeed ? ContentTypes.APPLICATION_ATOM_FEED : ContentTypes.APPLICATION_ATOM_ENTRY;
		
		for(EdmNavigationProperty navProp : entity.getEntityType().getDeclaredNavigationProperties()){
			writeLink(writer, navProp.getName(), type,  entity.getKeyString() + "/" + navProp.getName(),RELATED_NS + navProp.getName());
		}
	}
	
	protected static void writeProperties(ODataContext context,XmlWriter writer,List<ODataProperty> properties){
		for(ODataProperty p : properties){
			if(p.getMetadata().isFcKeepInContent() || Strings.isEmpty(p.getMetadata().getFcTargetPath())){
				writeProperty(context, writer, p,false);	
			}
		}
	}
	
	protected static void writeProperty(ODataContext context,XmlWriter writer,ODataProperty property,boolean isRootElement){
		if(isRootElement){
			writer.startElement(property.getName()).namespace(DATASERVICES_NS);
		}else{
			writer.startElement(DATASERVICES_NS,property.getName());
		}
		
		EdmType type  = property.getType();
		Object  value = property.getValue();
		
		if(property.getType().isSimple()){
			
			EdmSimpleType simpleType = (EdmSimpleType)type;

			if(type != EdmSimpleType.STRING){
				writer.attribute(METADATA_NS, "type", simpleType.getFullQualifiedName());
			}
			
			if(null == value){
				writer.attribute(METADATA_NS, "null","true");
			}else{
				writer.text(ODataConverts.toString(simpleType, value));
			}
		}else{
			//TODO : complex type
		}
		
		writer.endElement();
	}
}
