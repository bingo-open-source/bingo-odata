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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import bingo.lang.Builder;
import bingo.lang.Collections;
import bingo.lang.exceptions.ObjectNotFoundException;
import bingo.odata.edm.EdmBuilders;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.edm.EdmFeedCustomization.SyndicationItemProperty;
import bingo.odata.edm.EdmNavigationProperty;
import bingo.odata.edm.EdmProperty;
import bingo.odata.edm.EdmType;

public class ODataEntityBuilder implements Builder<ODataEntity>{
	
	private final EdmEntitySet  	  entitySet;
	private final EdmEntityType 	  entityType;
	private final List<ODataProperty> properties = new ArrayList<ODataProperty>();
	private final List<ODataNavigationProperty> navigationProperties = new ArrayList<ODataNavigationProperty>();
	private boolean createNotExpandedNavProperties = true;
	private ODataKey key;
	
	public ODataEntityBuilder(EdmEntitySet entitySet,EdmEntityType entityType){
		this.entitySet        = entitySet;
		this.entityType       = entityType;
	}
	
	public EdmEntityType getEntityType(){
		return entityType;
	}
	
	public boolean isCreateNotExpandedNavProperties() {
		return createNotExpandedNavProperties;
	}

	public ODataEntityBuilder setCreateNotExpandedNavProperties(boolean createNotExpandedNavProperties) {
		this.createNotExpandedNavProperties = createNotExpandedNavProperties;
		return this;
	}
	
	public ODataEntityBuilder setKey(ODataKey key) {
		this.key = key;
		return this;
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
	
	public ODataEntityBuilder addNavigationProperty(ODataNavigationProperty navigationProperty){
		navigationProperties.add(navigationProperty);
		return this;
	}
	
	public ODataEntityBuilder addNavigationProperties(Iterable<ODataNavigationProperty> navigationProperties){
		Collections.addAll(this.navigationProperties, navigationProperties);
		return this;
	}
	
	public ODataEntity build() {
		if(createNotExpandedNavProperties){
			List<ODataNavigationProperty> list = new ArrayList<ODataNavigationProperty>();
			for(EdmNavigationProperty np : entityType.getAllNavigationProperties()){
				boolean created = false;
				for(ODataNavigationProperty onp : navigationProperties){
					if(onp.getMetadata().getName().equalsIgnoreCase(np.getName())){
						list.add(onp);
						created = true;
						break;
					}
				}
				if(!created){
					list.add(new ODataNavigationPropertyImpl(np));
				}
			}
			
			return new ODataEntityImpl(entitySet, entityType, key, properties,list);	 
		}else{
			return new ODataEntityImpl(entitySet, entityType, key, properties,navigationProperties);	
		}
    }
}