/*
 * Copyright 2013 the original author or authors.
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

import bingo.odata.ODataObject;
import bingo.odata.edm.EdmNavigationProperty;

public class ODataNavigationPropertyImpl implements ODataNavigationProperty {

	protected final EdmNavigationProperty metadata;
	protected final ODataObject           inlineContent;
	
	public ODataNavigationPropertyImpl(EdmNavigationProperty metadata){
		this.metadata      = metadata;
		this.inlineContent = null;
	}
	
	public ODataNavigationPropertyImpl(EdmNavigationProperty metadata,ODataEntity relatedEntity){
		this.metadata      = metadata;
		this.inlineContent = relatedEntity;
	}
	
	public ODataNavigationPropertyImpl(EdmNavigationProperty metadata,ODataEntitySet relatedEntitySet){
		this.metadata      = metadata;
		this.inlineContent = relatedEntitySet;
	}

	public EdmNavigationProperty getMetadata() {
	    return metadata;
    }

	public boolean isExpanded() {
	    return null != inlineContent;
    }
	
	public boolean isRelatedEntitySet() {
	    return inlineContent instanceof ODataEntitySet;
    }

	public boolean isRelatedEntity() {
	    return inlineContent instanceof ODataEntity;
    }

	public ODataEntitySet getRelatedEntititySet() {
	    return (ODataEntitySet)inlineContent;
    }

	public ODataEntity getRelatedEntity() {
	    return (ODataEntity)inlineContent;
    }
}
