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

import bingo.lang.xml.XmlWriter;
import bingo.odata.ODataContext;
import bingo.odata.ODataError;
import bingo.odata.format.ODataXmlWriter;

public class XmlErrorWriter extends ODataXmlWriter<ODataError> {

	@Override
    protected void write(ODataContext context, XmlWriter writer, ODataError target) throws Throwable {
		writer.startDocument();
		
		writer.startElement(METADATA_PREFIX,METADATA_NS,"error").namespace(METADATA_PREFIX, METADATA_NS);
		
		writer.startElement(METADATA_NS,"code").text(target.getCode()).endElement();
		
		writer.startElement(METADATA_NS,"message").text(target.getMessage()).endElement();
		
		writer.endElement();
		
		writer.endDocument();
    }

}