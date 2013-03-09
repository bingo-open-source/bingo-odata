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

import bingo.lang.Builder;
import bingo.meta.edm.EdmProperty;
import bingo.meta.edm.EdmSimpleType;
import bingo.odata.ODataConverts;

public class ODataPropertyBuilder implements Builder<ODataProperty>{
	
	public static ODataProperty of(EdmProperty property, Object value) {
		return new ODataPropertyImpl(property,value);
	}
	
	public static ODataProperty of(EdmProperty property, String value) {
		return new ODataPropertyImpl(property,ODataConverts.fromString((EdmSimpleType)property.getType(), value));
	}
	
	public static ODataProperty nullOf(EdmProperty property) {
		return new ODataPropertyImpl(property,null);
	}
	
	protected EdmProperty property;
	protected Object      value;
	
	public ODataPropertyBuilder(EdmProperty property){
		this.property = property;
	}
	
	public ODataPropertyBuilder setRawValue(Object value){
		this.value = value;
		return this;
	}
	
	public ODataPropertyBuilder setStringValue(String value){
		this.value = ODataConverts.fromString((EdmSimpleType)property.getType(), value);
		return this;
	}

	public ODataProperty build() {
	    return new ODataPropertyImpl(property, value);
    }
}
