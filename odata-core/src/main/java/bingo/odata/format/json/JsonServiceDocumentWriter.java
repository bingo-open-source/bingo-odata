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
package bingo.odata.format.json;

import bingo.odata.ODataContext;
import bingo.odata.ODataServices;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.format.ODataJsonWriter;
import bingo.lang.json.JSONWriter;

public class JsonServiceDocumentWriter extends ODataJsonWriter<ODataServices>{

	@Override
    protected void write(ODataContext context, JSONWriter writer, ODataServices target) throws Throwable {
		/*
			 {
				d: {
					EntitySets: [
						"Products",
						"Categories",
						"Suppliers"
					]
				}
			 }
		 */
		
		writer.startObject();
		writer.startArray("EntitySets");
		
		int i=0;
		
		for(EdmEntitySet es : target.getEntitySets()){
			if(i==0){
				i=1;
			}else{
				writer.separator();
			}
			writer.value(es.getName());
		}
		
		writer.endArray();
		writer.endObject();
    }
	
}