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

import java.util.ArrayList;
import java.util.List;

import bingo.lang.Builder;
import bingo.lang.Enumerables;
import bingo.meta.edm.EdmComplexType;
import bingo.meta.edm.EdmProperty;

public class ODataComplexObjectBuilder implements Builder<ODataComplexObject> {
	
	protected EdmComplexType      metadata;
	protected List<ODataProperty> properties = new ArrayList<ODataProperty>();
	
	public ODataComplexObjectBuilder(){
		
	}

	public ODataComplexObjectBuilder(EdmComplexType metadata){
		this.metadata = metadata;
	}

	public ODataComplexObjectBuilder setMetadata(EdmComplexType metadata) {
		this.metadata = metadata;
		return this;
	}
	
	public ODataComplexObjectBuilder addProperty(ODataProperty property){
		properties.add(property);
		return this;
	}
	
	public ODataComplexObjectBuilder addProperties(ODataProperty... properties){
		for(ODataProperty p : properties){
			addProperty(p);
		}
		return this;
	}
	
	public ODataComplexObjectBuilder addProperty(EdmProperty p,Object value){
		return addProperty(ODataBuilders.property(p).setRawValue(value).build());
	}
	
	public ODataComplexObject build() {
		return new ODataComplexObjectImpl(this.metadata, Enumerables.of(this.properties));
	}

}
