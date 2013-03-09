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
import bingo.meta.edm.EdmEntitySet;
import bingo.odata.ODataWriterContext;
import bingo.odata.ODataServices;
import bingo.odata.format.ODataXmlWriter;

public class AtomServiceDocumentWriter extends ODataXmlWriter<ODataServices>{
	
	@Override
    protected void write(ODataWriterContext context, XmlWriter writer, ODataServices services) throws Throwable {
		writer.startDocument();
		
		writer.startElement("service")
				  .namespace(ATOM_PREFIX,ATOM_NS)
				  .namespace(APP_PREFIX,APP_NS)
				  .namespace(APP_NS);

		writer.attributeOptional("xml:base",context.getUrlInfo().getServiceRootUri());
		
		writer.startElement("workspace");
			  
		writeAtomTitle(writer,services.getName());
		
		for(EdmEntitySet entitySet : services.getEntitySets()){
			writeCollection(writer, entitySet);
		}
		
		writer.endElement();
		writer.endDocument();
		
		writer.close();
    }
	
	private static void writeAtomTitle(XmlWriter writer,String title) {
		writer.startElement(ATOM_QN_TITLE).text(title).endElement();
	}
	
	private static void writeCollection(XmlWriter writer,EdmEntitySet entitySet){
		writer.startElement("collection").attribute("href", entitySet.getName());
		
		writeAtomTitle(writer, entitySet.getName());
		
		writer.endElement();
	}
}