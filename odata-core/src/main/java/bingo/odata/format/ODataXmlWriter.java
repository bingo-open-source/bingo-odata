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

import static bingo.odata.format.ODataXmlConstants.EDM_NS;

import java.io.Writer;

import bingo.lang.Strings;
import bingo.lang.xml.XmlFactory;
import bingo.lang.xml.XmlWriter;
import bingo.odata.ODataObject;
import bingo.odata.ODataRequest;
import bingo.odata.ODataWriter;
import bingo.odata.ODataConstants.ContentTypes;
import bingo.odata.edm.EdmDocumentation;
import bingo.odata.edm.EdmObjectWithDocumentation;

public abstract class ODataXmlWriter<T extends ODataObject> implements ODataWriter<T> {

	public final void write(ODataRequest request,Writer out, T target) throws Throwable {
		write(request,XmlFactory.createWriter(out),target);
    }
	
	public String getContentType() {
	    return ContentTypes.APPLICATION_XML;
    }

	protected abstract void write(ODataRequest request,XmlWriter writer,T target) throws Throwable;
	
	protected static void writeDocument(XmlWriter writer,EdmObjectWithDocumentation item){
		EdmDocumentation doc = item.getDocumentation();
		
		if(null != doc && !Strings.isEmpty(doc.getSummary()) && !Strings.isEmpty(item.getDocumentation())){
			writer.startElement(EDM_NS,"Documentation");
			writer.elementOptional(EDM_NS, "Summary");
			writer.elementOptional(EDM_NS, "LongDescription");
			writer.endElement();
		}
	}
}