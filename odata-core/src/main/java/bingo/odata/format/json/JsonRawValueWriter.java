/*
 * Copyright 2013 the original author or authors.
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
package bingo.odata.format.json;

import java.io.Writer;

import bingo.lang.json.JSON;
import bingo.odata.ODataConstants.ContentTypes;
import bingo.odata.ODataContext;
import bingo.odata.ODataWriter;
import bingo.odata.data.ODataRawValue;

public class JsonRawValueWriter implements ODataWriter<ODataRawValue>{
	
	public String getContentType() {
	    return ContentTypes.APPLICATION_JSON_UTF8;
    }
	
	public void write(ODataContext context, Writer out, ODataRawValue target) throws Throwable {
		JsonWriterUtils.writeRawValue(context,JSON.createWriter(out),target);
    }
}
