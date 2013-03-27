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
package bingo.odata;

import java.util.ArrayList;
import java.util.List;

import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Named;
import bingo.lang.Strings;
import bingo.lang.http.HttpMethods;
import bingo.meta.edm.EdmEntityContainer;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmEntityTypeRef;
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmSchema;

public class ODataServices implements Named,ODataObject {
	
	private final String name;

	private final Enumerable<EdmSchema> schemas;
	
	public ODataServices(Iterable<EdmSchema> schemas){
		this(ODataConstants.Defaults.DATA_SERVICE_NAME,schemas);
	}
	
	public ODataServices(String name,Iterable<EdmSchema> schemas){
		this.name    = name;
		this.schemas = Enumerables.of(schemas);
	}

	public String getName() {
	    return name;
    }

	public Enumerable<EdmSchema> getSchemas() {
    	return schemas;
    }
	
	public Enumerable<EdmEntitySet> getEntitySets(){
		List<EdmEntitySet> entitySets = new ArrayList<EdmEntitySet>();
		
		for(EdmSchema schema : schemas){
			for(EdmEntityContainer container : schema.getEntityContainers()){
				for(EdmEntitySet entitySet : container.getEntitySets()){
					entitySets.add(entitySet);
				}
			}
		}
		
		return Enumerables.of(entitySets);
	}
	
	public EdmEntitySet findEntitySet(String entitySetName){
		for(EdmSchema schema : schemas){
			for(EdmEntityContainer container : schema.getEntityContainers()){
				for(EdmEntitySet entitySet : container.getEntitySets()){
					if(entitySet.getName().equalsIgnoreCase(entitySetName)){
						return entitySet;
					}
				}
			}
		}
		return null;
	}
	
	public EdmEntitySet findEntitySet(EdmEntityType entityType){
		for(EdmSchema schema : schemas){
			for(EdmEntityContainer container : schema.getEntityContainers()){
				for(EdmEntitySet entitySet : container.getEntitySets()){
					if(entitySet.getEntityType().getFullQualifiedName().equalsIgnoreCase(entityType.getFullQualifiedName())){
						return entitySet;
					}
				}
			}
		}
		return null;
	}
	
	public EdmEntitySet findEntitySet(EdmEntityTypeRef entityTypeRef){
		boolean fullQualified = !entityTypeRef.getName().equalsIgnoreCase(entityTypeRef.getFullQualifiedName());
		for(EdmSchema schema : schemas){
			for(EdmEntityContainer container : schema.getEntityContainers()){
				for(EdmEntitySet entitySet : container.getEntitySets()){
					if(fullQualified){
						if(entitySet.getEntityType().getFullQualifiedName().equalsIgnoreCase(entityTypeRef.getFullQualifiedName())){
							return entitySet;
						}	
					}else{
						if(entitySet.getEntityType().getName().equalsIgnoreCase(entityTypeRef.getName())){
							return entitySet;
						}	
					}
				}
			}
		}
		return null;
	}
	
	public EdmEntityType findEntityType(String entityTypeName) {
		for(EdmSchema schema : schemas){
			for(EdmEntityType entityType : schema.getEntityTypes()){
				if(entityType.getName().equalsIgnoreCase(entityTypeName)){
					return entityType;
				}
			}
		}
		return null;
	}
	
	public EdmEntityType findEntityType(EdmEntityTypeRef entityTypeRef){
		for(EdmSchema schema : schemas){
			for(EdmEntityType entityType : schema.getEntityTypes()){
				if(entityType.getFullQualifiedName().equalsIgnoreCase(entityTypeRef.getFullQualifiedName())){
					return entityType;
				}
			}
		}
		return null;
	}
	
	public EdmFunctionImport[] findFunctionImport(String functionName){
		List<EdmFunctionImport> list = new ArrayList<EdmFunctionImport>();
		
		for(EdmSchema schema : schemas){
			for(EdmEntityContainer container : schema.getEntityContainers()){
				for(EdmFunctionImport functionImport : container.getFunctionImports()){
					if(functionImport.getName().equalsIgnoreCase(functionName)){
						list.add(functionImport);
					}
				}
			}
		}
		return list.toArray(new EdmFunctionImport[list.size()]);
	}
	
	public EdmFunctionImport findFunctionImport(String functionName,String httpMethod){
		EdmFunctionImport[] funcs = findFunctionImport(functionName);
		return findFunctionImportMatched(funcs, httpMethod);
	}
	
	public static EdmFunctionImport findFunctionImportMatched(EdmFunctionImport[] funcs,String httpMethod){
		if(funcs.length == 0){
			return null;
		}
		
		if(funcs.length == 1){
			EdmFunctionImport func = funcs[0];
			if(Strings.isEmpty(func.getHttpMethod()) || HttpMethods.ALL.equals(func.getHttpMethod()) || func.getHttpMethod().equalsIgnoreCase(httpMethod)){
				return func;
			}
			return null;
		}
		
		for(EdmFunctionImport func : funcs){
			if(Strings.equalsIgnoreCase(func.getHttpMethod(),httpMethod)){
				return func;
			}
		}
		
		for(EdmFunctionImport func : funcs){
			if(Strings.isEmpty(func.getHttpMethod()) || HttpMethods.ALL.equals(func.getHttpMethod())){
				return func;
			}
		}
		
		return null;
	}
}