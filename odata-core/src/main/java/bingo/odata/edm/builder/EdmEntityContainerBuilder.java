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
package bingo.odata.edm.builder;

import java.util.ArrayList;
import java.util.List;

import bingo.lang.Builder;
import bingo.odata.edm.EdmAssociationSet;
import bingo.odata.edm.EdmEntityContainer;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityTypeRef;
import bingo.odata.edm.EdmFunctionImport;

public class EdmEntityContainerBuilder extends EdmNamedBuilder implements Builder<EdmEntityContainer> {
	
	protected boolean isDefault;
	
	protected boolean lazyLoadingEnabled;
	
	protected List<EdmEntitySet> entitySets = new ArrayList<EdmEntitySet>();
	
	protected List<EdmFunctionImport> functionImports = new ArrayList<EdmFunctionImport>();
	
	protected List<EdmAssociationSet> associationSets = new ArrayList<EdmAssociationSet>();
	
	public EdmEntityContainerBuilder(){
		
	}
	
	public EdmEntityContainerBuilder(String name){
		this.name = name;
	}
	
	public boolean isDefault() {
    	return isDefault;
    }

	public EdmEntityContainerBuilder setDefault(boolean isDefault) {
    	this.isDefault = isDefault;
    	return this;
    }
	
	public boolean isLazyLoadingEnabled() {
    	return lazyLoadingEnabled;
    }
	
	public List<EdmEntitySet> getEntitySets() {
    	return entitySets;
    }

	public List<EdmFunctionImport> getFunctionImports() {
    	return functionImports;
    }

	public List<EdmAssociationSet> getAssociationSets() {
    	return associationSets;
    }

	public EdmEntityContainerBuilder setLazyLoadingEnabled(boolean lazyLoadingEnabled) {
    	this.lazyLoadingEnabled = lazyLoadingEnabled;
    	return this;
    }
	
	public EdmEntityContainerBuilder addEntitySet(String name,EdmEntityTypeRef entityType){
		return addEntitySet(new EdmEntitySet(name, entityType));
	}
	
	public EdmEntityContainerBuilder addEntitySet(EdmEntitySet entitySet) {
		entitySets.add(entitySet);
		return this;
	}
	
	public EdmEntityContainerBuilder addEntitySets(EdmEntitySet... entitySets) {
		for(EdmEntitySet entitySet : entitySets){
			addEntitySet(entitySet);
		}
		return this;
	}
	
	public EdmEntityContainerBuilder addAssociationSet(EdmAssociationSet associationSet) {
		associationSets.add(associationSet);
		return this;
	}
	
	public EdmEntityContainerBuilder addFunctionImport(EdmFunctionImport functionImport) {
		functionImports.add(functionImport);
		return this;
	}

	public EdmEntityContainer build() {
	    return new EdmEntityContainer(name, isDefault, lazyLoadingEnabled, entitySets, functionImports, associationSets,documentation);
    }
}