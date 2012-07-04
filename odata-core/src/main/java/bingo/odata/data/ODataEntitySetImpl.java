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

import java.util.List;

import bingo.lang.Immutables;
import bingo.odata.edm.EdmEntitySet;

public class ODataEntitySetImpl implements ODataEntitySet {
	
	private final EdmEntitySet      metadata;
	private final List<ODataEntity> entities;
	private final Long              inlineCount;
	private final String            skipToken;
	
	public ODataEntitySetImpl(EdmEntitySet metadata, List<ODataEntity> entities) {
	    super();
	    this.metadata    = metadata;
	    this.entities    = Immutables.listOf(entities);
	    this.inlineCount = null;
	    this.skipToken   = null;
    }	
	
	public ODataEntitySetImpl(EdmEntitySet metadata, List<ODataEntity> entities, Long inlineCount) {
	    super();
	    this.metadata    = metadata;
	    this.entities    = Immutables.listOf(entities);
	    this.inlineCount = inlineCount;
	    this.skipToken   = null;
    }
	
	public ODataEntitySetImpl(EdmEntitySet metadata, List<ODataEntity> entities, Long inlineCount, String skipToken) {
	    super();
	    this.metadata    = metadata;
	    this.entities    = Immutables.listOf(entities);
	    this.inlineCount = inlineCount;
	    this.skipToken   = skipToken;
    }

	public EdmEntitySet getMetadata() {
    	return metadata;
    }

	public List<ODataEntity> getEntities() {
    	return entities;
    }

	public Long getInlineCount() {
    	return inlineCount;
    }

	public String getSkipToken() {
    	return skipToken;
    }
}