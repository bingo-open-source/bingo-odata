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

	private final String entitySet;
	private final String httpMethod;
	
	public EdmFunctionImport(String name,String entitySet, EdmType returnType,Iterable<EdmParameter> parameters) {
	    super(name,returnType,parameters);
	    
	    this.entitySet  = entitySet;
	    this.httpMethod = null;
    }
	
	public EdmFunctionImport(String name,String entitySet, EdmType returnType,Iterable<EdmParameter> parameters,String httpMethod) {
	    super(name,returnType,parameters);
	    
	    this.entitySet  = entitySet;
	    this.httpMethod = httpMethod;
    }
	
	public EdmFunctionImport(String name,String entitySet, EdmType returnType,Iterable<EdmParameter> parameters, EdmDocumentation documentation) {
	    this(name, entitySet, returnType, parameters);

	    this.documentation = documentation;
    }
	
	public EdmFunctionImport(String name,String entitySet, EdmType returnType,Iterable<EdmParameter> parameters, String httpMethod, EdmDocumentation documentation) {
	    this(name, entitySet, returnType, parameters, httpMethod);

	    this.documentation = documentation;
    }
	
	public String getHttpMethod() {
    	return httpMethod;
    }

	public String getEntitySet() {
    	return entitySet;
    }
}