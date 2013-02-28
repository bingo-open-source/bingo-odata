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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import bingo.lang.Builder;
import bingo.lang.exceptions.ObjectNotFoundException;
import bingo.odata.edm.EdmBuilders;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.edm.EdmType;
import bingo.odata.edm.EdmFeedCustomization.SyndicationItemProperty;
import bingo.odata.edm.EdmProperty;

public class ODataEntityBuilder implements Builder<ODataEntity>{
	
	private final EdmEntitySet  	  entitySet;
	private final EdmEntityType 	  entityType;
	private final List<ODataProperty> properties = new ArrayList<ODataProperty>();
	
	public ODataEntityBuilder(EdmEntitySet entitySet,EdmEntityType entityType){
		this.entitySet        = entitySet;
		this.entityType       = entityType;
	}
	
	public EdmEntityType getEntityType(){
		return entityType;
	}

	public ODataEntityBuilder addProperty(ODataProperty property){
		properties.add(property);
		return this;
	}
	
	public ODataEntityBuilder addProperty(EdmProperty metadata,Object value){
		return addProperty(new ODataPropertyImpl(metadata, value));
	}
	
	public ODataEntityBuilder addProperties(Map<String,Object> properties){
		for(Entry<String,Object> entry : properties.entrySet()){
			addProperty(entry.getKey(),entry.getValue());
		}
		return this;
	}
	
	/**
	 * @exception ObjectNotFoundException throws when property not exists 
	 */
	public ODataEntityBuilder addProperty(String name,Object value) throws ObjectNotFoundException {
		EdmProperty p = entityType.findProperty(name);

		if(null == p){
			throw new ObjectNotFoundException("property '{0}' not exists in entity type '{1}'",name,entityType.getName());
		}
		
		return addProperty(new ODataPropertyImpl(p,value));
	}
	
	/**
	 * @exception IllegalStateException throws if entity type is not a open type.
	 */
	public ODataEntityBuilder addDynamicProperty(EdmType type,String name,Object value) throws IllegalStateException{
		if(!entityType.isOpenType()){
			throw new IllegalStateException("only OpenEntitType can add dynamic property");
		}
		EdmProperty dynamicProperty = EdmBuilders.property(name).setType(type).setNullable(true).build();
		return addProperty(new ODataPropertyImpl(dynamicProperty,value));
	}
	
	public ODataEntityBuilder addPropertyIfExists(String name,Object value){
		EdmProperty p = entityType.findProperty(name);

		if(null == p){
			return this;
		}
		
		return addProperty(new ODataPropertyImpl(p,value));
	}
	
	public boolean tryAddProperty(String name,Object value){
		EdmProperty p = entityType.findProperty(name);

		if(null == p){
			return false;
		}
		
		addProperty(new ODataPropertyImpl(p,value));
		return true;
	}
	
	public ODataEntityBuilder addProperties(Iterable<ODataProperty> properties){
		for(ODataProperty p : properties){
			addProperty(p);
		}
		return this;
	}
	
	public ODataEntityBuilder addTitlePropertyIfExists(String title){
		return addFeedPropertyIfExists(SyndicationItemProperty.Title,title);
	}
	
	public ODataEntityBuilder addSummaryPropertyIfExists(String summary){
		return addFeedPropertyIfExists(SyndicationItemProperty.Summary,summary);
	}
	
	public ODataEntityBuilder addFeedPropertyIfExists(SyndicationItemProperty feedProperty,String value){
		for(EdmProperty p : entityType.getDeclaredProperties()){
			if(feedProperty.equalsValue(p.getFcTargetPath())){
				return addProperty(ODataPropertyBuilder.of(p, value));
			}
		}
		return this;
	}
	
	public ODataEntity build() {
	    return new ODataEntityImpl(entitySet, entityType, properties);
    }
}