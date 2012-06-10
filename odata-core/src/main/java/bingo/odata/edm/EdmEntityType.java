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

import bingo.lang.Immutables;

public class EdmEntityType extends EdmNamedStructualType {
	
	private final EdmEntityType baseType;
	
	public EdmEntityType(String name,Iterable<EdmProperty> properties){
		this.name       = name;
		this.properties = Immutables.listOf(properties);
		this.baseType   = null;
	}
	
	public EdmEntityType(String name,Iterable<EdmProperty> properties,boolean isAbstract){
		this(name,properties);
		
		this.isAbstract = isAbstract;
	}
	
	public EdmEntityType(String name,Iterable<EdmProperty> properties,boolean isAbstract,EdmEntityType baseType){
		this.name       = name;
		this.properties = Immutables.listOf(properties);
		this.isAbstract = isAbstract;
		this.baseType   = baseType;
	}
	
	public EdmEntityType(String name,Iterable<EdmProperty> properties,boolean isAbstract,EdmEntityType baseType,EdmDocumentation documentation){
		this(name,properties,isAbstract,baseType);
		this.documentation = documentation;
	}

	public EdmEntityType getBaseType() {
    	return baseType;
    }
}
