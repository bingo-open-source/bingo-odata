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

import bingo.lang.xml.XmlWriter;
import bingo.odata.ODataContext;
import bingo.odata.data.ODataEntity;
import bingo.odata.data.ODataEntitySet;
import bingo.odata.format.ODataAtomUtils;
import bingo.odata.format.ODataAtomWriter;

public class AtomEntitySetWriter extends ODataAtomWriter<ODataEntitySet> {

	@Override
    protected void write(ODataContext context, XmlWriter writer, ODataEntitySet entitySet) throws Throwable {
		String updated = ODataAtomUtils.lastUpdated();
		
		writer.startDocument();
		
		writer.startElement("feed")
			  .namespace(METADATA_PREFIX,METADATA_NS)
			  .namespace(DATASERVICES_PREFIX,DATASERVICES_NS)
			  .namespace(ATOM_NS)
			  .attributeOptional("xml:base",context.getUrlInfo().getServiceRootUri());

		
		writeHeader(context, writer, entitySet, updated);
		
		writeEntries(context, writer, entitySet, updated);
		
		writeNext(context,writer,entitySet);
		
		writer.endElement();
		writer.endDocument();
    }
	
	protected void writeHeader(ODataContext context, XmlWriter writer, ODataEntitySet entitySet,String updated) {
		
		String entitySetName = entitySet.getMetadata().getName();
		
		writeTitle(writer, entitySetName);
		
		writeId(writer, context.getUrlInfo().getResourceUri());
		
		writeUpdated(writer, updated);
		
		writeLink(writer, entitySetName, entitySetName, "self");
		
		if(entitySet.getInlineCount() != null){
			writer.startElement(METADATA_NS,"count")
				  .text(String.valueOf(entitySet.getInlineCount()), false)
				  .endElement();
		}
	}
	
	protected void writeEntries(ODataContext context, XmlWriter writer, ODataEntitySet entitySet,String updated) {
		for(ODataEntity entity : entitySet.getEntities()){
			writer.startElement("entry");
			
			writeEntryBody(context, writer, entity, updated, true);
			
			writer.endElement();
		}
	}
	
	protected void writeNext(ODataContext context, XmlWriter writer, ODataEntitySet entitySet) {
		if(null != entitySet.getSkipToken()){
			//TODO : SKIP TOKEN
		}
	}
}
