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

import bingo.lang.Assert;
import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Immutables;
import bingo.lang.Named;
import bingo.odata.edm.EdmEntityContainer;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmSchema;

public class ODataServices implements Named {
	
	private final String name;

	private final ODataVersion version;
	
	private final List<EdmSchema> schemas;
	
	public ODataServices(ODataVersion version,Iterable<EdmSchema> schemas){
		this(ODataConstants.Defaults.DataServiceName,version,schemas);
	}
	
	public ODataServices(String name, ODataVersion version,Iterable<EdmSchema> schemas){
		Assert.notNull(version,"version cannot be null");
		
		this.name    = name;
		this.version = version;
		this.schemas = Immutables.listOf(schemas);
	}

	public String getName() {
	    return name;
    }

	public ODataVersion getVersion() {
    	return version;
    }

	public Enumerable<EdmSchema> getSchemas() {
    	return Enumerables.of(schemas);
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

}