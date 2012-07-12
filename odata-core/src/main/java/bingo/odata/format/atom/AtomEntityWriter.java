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
import bingo.odata.format.ODataAtomUtils;
import bingo.odata.format.ODataAtomWriter;

public class AtomEntityWriter extends ODataAtomWriter<ODataEntity> {

	@Override
    protected void write(ODataContext context, XmlWriter writer, ODataEntity entity) throws Throwable {
		String updated = ODataAtomUtils.lastUpdated();
		
		writer.startDocument();
		
		writer.startElement("entry")
			  .namespace(METADATA_PREFIX,METADATA_NS)
			  .namespace(DATASERVICES_PREFIX,DATASERVICES_NS)
			  .namespace(ATOM_NS)
			  .attributeOptional("xml:base",context.getUrlInfo().getServiceRootUri());
		
		writeEntryBody(context, writer, entity, updated,false);
		
		writer.endElement();
		writer.endDocument();
    }
}