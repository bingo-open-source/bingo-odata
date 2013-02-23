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
package bingo.odata.data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bingo.lang.Assert;
import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Immutables;
import bingo.lang.Predicates;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;

class ODataEntityImpl implements ODataEntity {

	private final EdmEntityType	    entityType;
	private final EdmEntitySet	    	entitySet;
	private final List<ODataProperty>	properties;
	
	private ODataKey key;
	private String   keyString;
	
	public ODataEntityImpl(EdmEntitySet entitySet, EdmEntityType entityType, List<ODataProperty> properties) {
	    super();
	    Assert.notNull(entitySet,"entitySet cannot be null");
	    Assert.notNull(entityType,"entityType cannot be null");
	    this.entitySet  = entitySet;
	    this.entityType = entityType;
	    this.properties = Immutables.listOf(properties);
    }

	public EdmEntitySet getEntitySet() {
	    return entitySet;
    }

	public EdmEntityType getEntityType() {
	    return entityType;
    }

	public List<ODataProperty> getProperties() {
		return properties;
	}
	
	public Object getPropertyValue(String name) {
		ODataProperty p = Enumerables.firstOrNull(properties,Predicates.<ODataProperty>nameEqualsIgnoreCase(name));
		
	    return null == p ? null : p.getValue();
    }

	public ODataKey getKey() {
		if(null == key){
			Enumerable<String> keys = entityType.getKeys();
			
			if(keys.size() == 1){
				String keyName = entityType.getKeys().first();
				for(ODataProperty p : properties){
					if(p.getName().equalsIgnoreCase(keyName)){
						key = new ODataKeyImpl(p.getValue());
						break;
					}
				}
			}else{
				Object[] values = new Object[keys.size()];
				
				for(ODataProperty p : properties){
					for(int i=0;i<values.length;i++){
						if(keys.get(i).equalsIgnoreCase(p.getName())){
							values[i] = p.getValue();
						}
					}
				}
			}
		}
	    return key;
    }

	public String getKeyString() {
		if(null == keyString){
			keyString = entitySet.getName() + getKey().toKeyString();
		}
	    return keyString;
    }

	public Map<String, Object> toMap() {
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		
		for(ODataProperty p : properties){
			map.put(p.getName(),p.getValue());
		}
		
	    return map;
    }
}
