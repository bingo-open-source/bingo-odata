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

import java.util.Map;
import java.util.Map.Entry;

import bingo.odata.ODataReaderContext;
import bingo.odata.ODataErrors;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.edm.EdmNavigationProperty;
import bingo.odata.edm.EdmProperty;
import bingo.odata.format.ODataJsonReader;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntityBuilder;
import bingo.lang.json.JSONObject;

public class JsonEntityReader extends ODataJsonReader<ODataEntity> {

	@Override
    protected ODataEntity read(ODataReaderContext context, JSONObject json) {
		if(json.isNull() || json.isArray()){
			throw ODataErrors.badRequest("invalid json content");
		}
		
		ODataEntityBuilder builder = new ODataEntityBuilder(context.getEntitySet(), context.getEntityType());
		EdmEntityType entityType = context.getEntityType();
		
		Map<String,Object> map = json.map();
		
		for(Entry<String, Object> entry : map.entrySet()){
			String name = entry.getKey();

			if(name.equals("__metadata")){
				continue;
			}
			
			EdmProperty p = entityType.findDeclaredProperty(name);
			
			if(null != p){
				Object value = entry.getValue();
				
				//TODO : support ComplexType
				
				builder.addProperty(name,value);
				
				continue;
			}
			
			EdmNavigationProperty np = entityType.findDeclaredNavigationProperty(name);
			
			if(null != np){
				
				//TODO : support Links
			
				continue;
			}
			
			throw ODataErrors.badRequest("Unknow property '{0}'",name);
		}
		
		return builder.build();
    }
}
