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

import bingo.lang.Builder;
import bingo.meta.edm.EdmType;
import bingo.odata.ODataObject;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataServices;

public class ODataValueBuilder implements Builder<ODataValue> {
	
	protected ODataObjectKind kind;
	protected ODataObject     value;

	public ODataValueBuilder() {
		
	}
	
	public ODataValueBuilder entity(ODataEntity value){
		this.kind  = ODataObjectKind.Entity;
		this.value = value;
		return this;
	}
	
	public ODataValueBuilder entitySet(ODataEntitySet value){
		this.kind  = ODataObjectKind.EntitySet;
		this.value = value;
		return this;
	}
	
	public ODataValueBuilder services(ODataServices value){
		this.kind  = ODataObjectKind.ServiceDocument;
		this.value = value;
		return this;
	}
	
	public ODataValueBuilder property(ODataProperty value){
		this.kind  = ODataObjectKind.Property;
		this.value = value;
		return this;
	}
	
	public ODataValueBuilder raw(EdmType type,Object value){
		this.kind  = ODataObjectKind.Raw;
		this.value = new ODataRawValueImpl(type, value);
		return this;
	}
	
	public ODataValue build() {
	    return new ODataValueImpl(kind, value);
    }
}
