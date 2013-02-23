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

import bingo.lang.Builder;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;

public class ODataEntitySetBuilder implements Builder<ODataEntitySet>{

	private final EdmEntitySet      entitySet;
	private final EdmEntityType	 	entityType;
	private final List<ODataEntity> entities = new ArrayList<ODataEntity>();
	
	private Long   inlineCount;
	private String skipToken;
	
	public ODataEntitySetBuilder(EdmEntitySet entitySet,EdmEntityType entityType) {
	    this.entitySet  = entitySet;
	    this.entityType = entityType;
    }
	
	public ODataEntitySetBuilder addEntity(ODataEntity entity) {
		entities.add(entity);
		
		return this;
	}
	
	public ODataEntitySetBuilder setInlineCount(Long inlineCount) {
    	this.inlineCount = inlineCount;
    	return this;
    }

	public ODataEntitySetBuilder setSkipToken(String skipToken) {
    	this.skipToken = skipToken;
    	return this;
    }
	
	public ODataEntityBuilder newEntity(){
		return new ODataEntityBuilder(entitySet, entityType);
	}

	public ODataEntitySet build() {
	    return new ODataEntitySetImpl(entitySet, entities,inlineCount,skipToken);
    }
}