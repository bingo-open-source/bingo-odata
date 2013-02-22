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

public class EdmEntityType extends EdmNamedStructualType {
	
	private final String             fullQualifiedName;
	private final EdmEntityTypeRef   baseType;
	private final boolean 		     hasStream;
	private final Enumerable<String> keys;
	private final Enumerable<EdmNavigationProperty> navigationProperties;
	
	public EdmEntityType(String name,String fullQualifiedName,
						  Iterable<EdmProperty> properties,
						  Iterable<EdmNavigationProperty> navigationProperties,
						  Iterable<String> keys, 
						  boolean isAbstract,
						  boolean hasStream,
						  EdmEntityTypeRef baseType){
		
		super(name,properties,isAbstract);
		
		this.fullQualifiedName = fullQualifiedName;
		this.keys       = Enumerables.of(keys);
		this.navigationProperties = Enumerables.of(navigationProperties);
		this.baseType   = baseType;
		this.hasStream  = hasStream;
		
		doCheckValidKeys();
	}
	
	public EdmEntityType(String name,String fullQualifiedName,
						  Iterable<EdmProperty> properties,
						  Iterable<EdmNavigationProperty> navigationProperties,
						  Iterable<String> keys,
						  boolean isAbstract,
						  boolean hasStream,
						  EdmEntityTypeRef baseType,
						  EdmDocumentation documentation){
		
		this(name,fullQualifiedName,properties,navigationProperties,keys,isAbstract,hasStream,baseType);
		
		this.documentation = documentation;
	}
	
	public String getFullQualifiedName() {
    	return fullQualifiedName;
    }

	public EdmEntityTypeRef getBaseType() {
    	return baseType;
    }

	public Enumerable<EdmNavigationProperty> getDeclaredNavigationProperties(){
		return navigationProperties;
	}
	
	public EdmNavigationProperty findDeclaredNavigationProperty(String name){
		return Enumerables.firstOrNull(navigationProperties,Predicates.<EdmNavigationProperty>nameEqualsIgnoreCase(name));
	}
	
	public Enumerable<String> getKeys() {
    	return keys;
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