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
package bingo.odata.model;

import java.util.LinkedHashMap;
import java.util.Map;

import bingo.lang.Assert;
import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Predicates;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;

public class ODataEntityImpl implements ODataEntity {

	private final EdmEntityType	    	    entityType;
	private final EdmEntitySet	    	    entitySet;
	private final ODataKey 					key;
	private final Enumerable<ODataProperty>	properties;
	private final Enumerable<ODataNavigationProperty> navigationProperties;
	
	private String keyString;
	
	public ODataEntityImpl(EdmEntitySet entitySet, 
						   EdmEntityType entityType, 
						   ODataKey	key,
						   Iterable<ODataProperty> properties,Iterable<ODataNavigationProperty> navigationProperties) {
	    super();
	    Assert.notNull(entitySet,"entitySet cannot be null");
	    Assert.notNull(entityType,"entityType cannot be null");
	    this.entitySet  = entitySet;
	    this.entityType = entityType;
	    this.key		= key;
	    this.properties = Enumerables.of(properties);
	    this.navigationProperties = Enumerables.of(navigationProperties);
    }

	public EdmEntitySet getEntitySet() {
	    return entitySet;
    }

	public EdmEntityType getEntityType() {
	    return entityType;
    }

	public Enumerable<ODataProperty> getProperties() {
		return properties;
	}
	
	public Enumerable<ODataNavigationProperty> getNavigationProperties() {
		return navigationProperties;
	}

	public Object getPropertyValue(String name) {
		ODataProperty p = Enumerables.firstOrNull(properties,Predicates.<ODataProperty>nameEqualsIgnoreCase(name));
		
	    return null == p ? null : p.getValue();
    }

	public ODataKey getKey() {
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
