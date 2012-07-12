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
package bingo.odata.edm;

import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Predicates;

public abstract class EdmStructualType extends EdmType {

	protected final Enumerable<EdmProperty> properties;
	
	protected EdmStructualType(Iterable<EdmProperty> properties){
		this.properties = Enumerables.of(properties);
	}
	
	public Enumerable<EdmProperty> getDeclaredProperties() {
		return properties;
	}
	
	public EdmProperty findDeclaredProperty(String name){
		return Enumerables.firstOrNull(properties,Predicates.<EdmProperty>nameEqualsIgnoreCase(name));
	}
}
