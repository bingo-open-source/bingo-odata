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

public class EdmSchema extends EdmObjectWithDocumentation {
	
	private final String namespaceName;

	private final String alias;
	
	private final List<EdmEntityContainer> entityContainers;
	
	private final List<EdmEntityType> entityTypes;
	
	private final List<EdmAssociation> associations;
	
	private final List<EdmComplexType> complexTypes;
	
	private final List<EdmFunction> functions;
	
	public EdmSchema(String namespaceName,String alias,
					  Iterable<EdmEntityContainer> entityContainers,Iterable<EdmEntityType> entityTypes,
					  Iterable<EdmAssociation> associations,Iterable<EdmComplexType> complexTypes,Iterable<EdmFunction> functions) {
		
		this.namespaceName = namespaceName;
		this.alias         = alias;
		
		this.entityContainers = Immutables.listOf(entityContainers);
		this.entityTypes      = Immutables.listOf(entityTypes);
		this.associations     = Immutables.listOf(associations);
		this.complexTypes     = Immutables.listOf(complexTypes);
		this.functions        = Immutables.listOf(functions);
	}

	public String getNamespaceName() {
    	return namespaceName;
    }

	public String getAlias() {
    	return alias;
    }

	public Enumerable<EdmEntityContainer> getEntityContainers() {
    	return Enumerables.of(entityContainers);
    }

	public Enumerable<EdmEntityType> getEntityTypes() {
    	return Enumerables.of(entityTypes);
    }

	public Enumerable<EdmAssociation> getAssociations() {
    	return Enumerables.of(associations);
    }

	public Enumerable<EdmComplexType> getComplexTypes() {
    	return Enumerables.of(complexTypes);
    }

	public Enumerable<EdmFunction> getFunctions() {
    	return Enumerables.of(functions);
    }
}