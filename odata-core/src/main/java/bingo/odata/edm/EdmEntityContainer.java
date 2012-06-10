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

public class EdmEntityContainer extends EdmNamedObject {

	private final boolean isDefault;
	
	private final boolean lazyLoadingEnabled;
	
	private final List<EdmEntitySet> entitySets;
	
	private final List<EdmFunctionImport> functionImports;
	
	private final List<EdmAssociationSet> associationSets;
	
	public EdmEntityContainer(String name,boolean isDefault,boolean lazyLoadingEnabled,
							   Iterable<EdmEntitySet> entitySets,
							   Iterable<EdmFunctionImport> functionImports,
							   Iterable<EdmAssociationSet> associationSets) {
		
		this.name = name;
		this.isDefault = isDefault;
		this.lazyLoadingEnabled = lazyLoadingEnabled;
		this.entitySets = Immutables.listOf(entitySets);
		this.functionImports = Immutables.listOf(functionImports);
		this.associationSets = Immutables.listOf(associationSets);
	}
	
	public EdmEntityContainer(String name,boolean isDefault,boolean lazyLoadingEnabled,
							   Iterable<EdmEntitySet> entitySets,
							   Iterable<EdmFunctionImport> functionImports,
							   Iterable<EdmAssociationSet> associationSets,
							   EdmDocumentation documentation) {

		this(name,isDefault,lazyLoadingEnabled,entitySets,functionImports,associationSets);

		this.documentation = documentation;
	}

	public boolean isDefault() {
    	return isDefault;
    }

	public boolean isLazyLoadingEnabled() {
    	return lazyLoadingEnabled;
    }

	public Enumerable<EdmEntitySet> getEntitySets() {
    	return Enumerables.of(entitySets);
    }

	public Enumerable<EdmFunctionImport> getFunctionImports() {
    	return Enumerables.of(functionImports);
    }

	public Enumerable<EdmAssociationSet> getAssociationSets() {
    	return Enumerables.of(associationSets);
    }
}