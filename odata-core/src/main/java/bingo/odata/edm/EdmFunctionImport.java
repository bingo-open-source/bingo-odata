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

public class EdmFunctionImport extends EdmFunctionBase {

	private final EdmEntitySet entitySet;

	public EdmFunctionImport(String name,String qualifiedName,Iterable<EdmParameter> parameters, EdmType returnType) {
	    super(name,qualifiedName,parameters, returnType);
	    
	    this.entitySet = null;
    }
	
	public EdmFunctionImport(String name,String qualifiedName,Iterable<EdmParameter> parameters, EdmType returnType, EdmEntitySet entitySet) {
	    super(name,qualifiedName,parameters, returnType);
	    
	    this.entitySet = entitySet;
    }
	
	
	public EdmFunctionImport(String name,String qualifiedName, Iterable<EdmParameter> parameters, EdmType returnType, EdmEntitySet entitySet, EdmDocumentation documentation) {
	    this(name, qualifiedName, parameters, returnType, entitySet);

	    this.documentation = documentation;
    }

	public EdmEntitySet getEntitySet() {
    	return entitySet;
    }
}