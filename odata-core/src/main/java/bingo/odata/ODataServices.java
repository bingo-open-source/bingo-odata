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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Named;
import bingo.lang.Strings;
import bingo.lang.http.HttpMethods;
import bingo.lang.io.IO;
import bingo.meta.edm.EdmComplexType;
import bingo.meta.edm.EdmComplexTypeRef;
import bingo.meta.edm.EdmEntityContainer;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmEntityTypeRef;
import bingo.meta.edm.EdmFullQualifiedName;
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmSchema;
import bingo.odata.format.xml.XmlMetadataDocumentReader;

public class ODataServices implements Named,ODataObject {
	
	private static final ODataReaderContext        PARSER_CONTEXT = new ODataContextImpl();
	private static final XmlMetadataDocumentReader PARSER         = new XmlMetadataDocumentReader();
	
	public static ODataServices parse(InputStream in){
		return parse(new InputStreamReader(in),true);
	}
	
	public static ODataServices parse(Reader reader) {
		return parse(reader,true);
	}
	
	public static ODataServices parse(Reader reader,boolean close) {
		try {
	        return PARSER.read(PARSER_CONTEXT,reader);
        } catch (Throwable e) {
        	if(e instanceof RuntimeException){
        		throw (RuntimeException)e;
        	}
        	throw new ODataException("error parsing service metadata document",e);
        }finally{
        	if(close){
        		IO.close(reader);
        	}
        }
	}
	
	private final String name;
	
	private final ODataVersion version;

	private final Enumerable<EdmSchema> schemas;
	
	public ODataServices(Iterable<EdmSchema> schemas){
		this(ODataConstants.Defaults.DATA_SERVICE_NAME,null,schemas);
	}
	
	public ODataServices(ODataVersion version,Iterable<EdmSchema> schemas){
		this(ODataConstants.Defaults.DATA_SERVICE_NAME,version,schemas);
	}
	
	public ODataServices(String name,ODataVersion version,Iterable<EdmSchema> schemas){
		this.name    = name;
		this.version = version;
		this.schemas = Enumerables.of(schemas);
	}

	public String getName() {
	    return name;
    }
	
	public ODataVersion getVersion() {
		return version;
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
	
	public EdmComplexType findComplexType(String complexTypeName){
		for(EdmSchema schema : schemas){
			for(EdmComplexType complexType : schema.getComplexTypes()){
				if(Strings.equals(complexType.getName(),complexTypeName)) {
					return complexType;
				}
			}
		}
		return null;
	}
	
	public EdmComplexType findComplexType(EdmComplexTypeRef complexTypeRef){
		EdmFullQualifiedName fqName = new EdmFullQualifiedName(complexTypeRef.getFullQualifiedName());
		
		for(EdmSchema schema : schemas){
			for(EdmComplexType complexType : schema.getComplexTypes()){
				if(Strings.equals(fqName.getNamespace(), schema.getNamespaceName()) && Strings.equals(fqName.getName(), complexType.getName())) {
					return complexType;
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