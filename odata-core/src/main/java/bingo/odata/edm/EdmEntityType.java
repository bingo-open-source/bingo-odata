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

import java.util.List;

import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Immutables;

public class EdmEntityType extends EdmNamedStructualType {
	
	private final EdmEntityType baseType;
	private final boolean hasStream;
	private final List<String> keys;
	private final List<EdmNavigationProperty> navigationProperties;
	
	public EdmEntityType(String name,
						  Iterable<EdmProperty> properties,
						  Iterable<EdmNavigationProperty> navigationProperties,
						  Iterable<String> keys, 
						  boolean isAbstract,
						  boolean hasStream,
						  EdmEntityType baseType){
		
		super(name,properties,isAbstract);
		this.keys       = Immutables.listOf(keys);
		this.navigationProperties = Immutables.listOf(navigationProperties);
		this.baseType   = baseType;
		this.hasStream  = hasStream;
		
		doCheckValidKeys();
	}
	
	public EdmEntityType(String name,
						  Iterable<EdmProperty> properties,
						  Iterable<EdmNavigationProperty> navigationProperties,
						  Iterable<String> keys,
						  boolean isAbstract,
						  boolean hasStream,
						  EdmEntityType baseType,
						  EdmDocumentation documentation){
		
		this(name,properties,navigationProperties,keys,isAbstract,hasStream,baseType);
		
		this.documentation = documentation;
	}

	public EdmEntityType getBaseType() {
    	return baseType;
    }

	public Enumerable<EdmNavigationProperty> getDeclaredNavigationProperties(){
		return Enumerables.of(navigationProperties);
	}
	
	public Enumerable<String> getKeys() {
    	return Enumerables.of(keys);
    }
	
	public boolean hasStream() {
    	return hasStream;
    }
	
	@Override
    public EdmTypeKind getTypeKind() {
	    return EdmTypeKind.Entity;
    }

	protected void doCheckValidKeys(){
		for(String key : keys){
			if(null == findDeclaredProperty(key)){
				throw new EdmException("key property '{0}' not found",key);
			}
		}
	}
}