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
import bingo.odata.data.ODataEntity;
import bingo.odata.data.ODataEntitySet;
import bingo.odata.format.ODataJsonWriter;
import bingo.utils.json.JSONWriter;

public class JsonEntitySetWriter extends ODataJsonWriter<ODataEntitySet> {

	@Override
    protected void write(ODataContext context, JSONWriter writer, ODataEntitySet entitySet) throws Throwable {
		
		if(entitySet.getInlineCount() != null){
			
			writer.startObject()
			      .property("__count", String.valueOf(entitySet.getInlineCount())).separator();
			
			writer.name("results");
			
			writeEntities(context, writer, entitySet);
			
			writer.endObject();
			
		}else{
			writeEntities(context, writer, entitySet);
		}
    }
	
	protected static void writeEntities(ODataContext context,JSONWriter writer,ODataEntitySet entitySet){
		writer.startArray();
		
		int i=0;
		for(ODataEntity entity : entitySet.getEntities()){
			
			if(i==0){
				i=1;
			}else{
				writer.separator();
			}
			
			writeEntity(context, writer, entity);
		}
		
		writer.endArray();
	}
}