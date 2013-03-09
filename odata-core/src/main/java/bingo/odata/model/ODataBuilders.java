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

import bingo.meta.edm.EdmBuilders;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmProperty;
import bingo.meta.edm.EdmType;

public class ODataBuilders {

	protected ODataBuilders() {
		
	}
	
	public static ODataEntitySetBuilder entitySet(EdmEntitySet entitySet,EdmEntityType entityType){
		return new ODataEntitySetBuilder(entitySet,entityType);
	}
	
	public static ODataEntityBuilder entity(EdmEntitySet entitySet,EdmEntityType entityType){
		return new ODataEntityBuilder(entitySet, entityType);
	}
	
	public static ODataPropertyBuilder property(EdmProperty property){
		return new ODataPropertyBuilder(property);
	}
	
	public static ODataPropertyBuilder dynamicProperty(EdmType type,String name){
		return new ODataPropertyBuilder(EdmBuilders.dynamicProperty(type, name).build());
	}

	public static ODataValueBuilder value(){
		return new ODataValueBuilder();
	}
}
