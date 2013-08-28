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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import bingo.lang.Collections;
import bingo.lang.Maps;
import bingo.lang.NamedValue;
import bingo.lang.Strings;
import bingo.lang.json.JSONObject;
import bingo.lang.tuple.MutableNamedEntry;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmNavigationProperty;
import bingo.meta.edm.EdmProperty;
import bingo.meta.edm.EdmType;
import bingo.odata.ODataErrors;
import bingo.odata.ODataReaderContext;
import bingo.odata.format.ODataJsonReader;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntityBuilder;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataKeyImpl;

public class JsonEntityReader extends ODataJsonReader<ODataEntity> {

	@SuppressWarnings("unchecked")
	@Override
    protected ODataEntity read(ODataReaderContext context, JSONObject json) {
		if(json.isNull() || json.isArray()){
			throw ODataErrors.badRequest("invalid json content");
		}
		
		ODataEntityBuilder builder = new ODataEntityBuilder(context.getEntitySet(), context.getEntityType());
		EdmEntityType entityType = context.getEntityType();
		
		Map<String,Object> map = json.map();
		
		if(map.size() == 1 && Maps.containsKeyIgnoreCase(map, "d")) {
			map = (Map<String,Object>)map.get("d");
		}
		
		List<NamedValue<Object>> keys = new ArrayList<NamedValue<Object>>();
		
		for(Entry<String, Object> entry : map.entrySet()){
			String name = entry.getKey();

			if(name.equals("__metadata")){
				continue;
			}
			
			EdmProperty p = entityType.findProperty(name);
			
			if(null != p){
				Object value = entry.getValue();
				
				EdmType type = p.getType();
				if(type.isSimple()){
					if(entityType.getKeys().toSet().contains(name)) {
						NamedValue<Object> keyNamedValue = new MutableNamedEntry<Object>(name, value);
						keys.add(keyNamedValue);
					}
					builder.addProperty(name,JsonReaderUtils.readValue(type.asSimple(),value));
				}else{
					throw ODataErrors.notImplemented("complex type not implemented");
				}
				
				continue;
			}
			
			EdmNavigationProperty np = entityType.findNavigationProperty(name);
			
			if(null != np){
				
				//TODO : support Links
			
				continue;
			}
			
			// TODO handle named "odata.type" property.
			// omitted so far.
			if(Strings.equals(name, "odata.type")) continue;
			
			throw ODataErrors.badRequest("Unknow property '{0}'",name);
		}
		
		// handle key(s).
		if(!Collections.isEmpty(keys)) {
			ODataKey oDataKey = null;
			if(keys.size() == 1) {
				// single key
				oDataKey = new ODataKeyImpl(keys.get(0).getValue());
			} else {
				// multi keys
				oDataKey = new ODataKeyImpl(keys);
			}
			builder.setKey(oDataKey);
		}
		
		return builder.build();
    }
}
