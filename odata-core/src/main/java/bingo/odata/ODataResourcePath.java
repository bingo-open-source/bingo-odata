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
import bingo.lang.NamedValue;
import bingo.lang.Predicates;
import bingo.lang.Strings;
import bingo.lang.tuple.ImmutableNamedValue;

public class ODataResourcePath {
	private static final Enumerable<Resource> EMPTY_RESOURCES = Enumerables.empty();
	private static final Enumerable<NamedValue<String>> EMPTY_PARAMS = Enumerables.empty();
	private static final Resource NIL_RESOURCE = new Resource(null, null, EMPTY_PARAMS, EMPTY_RESOURCES);
	
	private final String   fullPath;
	private final boolean isServiceRoot;
	private final boolean isServiceMetadata;
	
	private Resource resource;
	
	public ODataResourcePath(String fullPath){
		Assert.notEmpty(fullPath,"resource path can not be empty");
		Assert.isTrue(fullPath.startsWith("/"),"resource path should starts with '/'");
		
		this.fullPath          = fullPath;
		this.isServiceRoot     = fullPath.equals("/");
		this.isServiceMetadata = !isServiceRoot & fullPath.equals("/$metadata");
	}
	
	public String getFullPath() {
		return fullPath;
	}
	
	public boolean isResource(){
		if(null == resource){
			parse();
		}
		return NIL_RESOURCE != resource;
	}
	
	public Resource getResource(){
		if(null == resource){
			parse();
		}
		return resource;
	}
	
	public boolean isServiceRoot(){
		return isServiceRoot;
	}
	
	public boolean isServiceMetadata(){
		return isServiceMetadata;
	}
	
	private void parse(){
		if(!isServiceRoot && !isServiceMetadata){
			//ResourceName(....)/ResourceName(...)/ResourceName(...)
			String[] parts = Strings.split(fullPath,"/");

			List<Resource> resources = new ArrayList<Resource>();
			
			for(int i=0;i<parts.length;i++){
				String part = parts[i];
				
				int index1 = part.indexOf('(');
				int index2 = part.indexOf(')');
				
				if(index1 < 0 && index2 < 0){
					resources.add(new Resource(part, Strings.EMPTY, EMPTY_PARAMS, Enumerables.of(resources)));
					continue;
				}
				
				if(index1 > 0 && index2 == part.length() - 1){
					String   paramString = part.substring(index1 + 1,index2 - 1).trim();
					String[] paramValues = Strings.split(paramString,",");
					
					String resourceName = part.substring(0,index1);
					String resourceKey  = null;
					List<NamedValue<String>> params = new ArrayList<NamedValue<String>>();
					
					if(paramValues.length == 1){
						resourceKey = paramString;
					}else{
						for(String param : paramValues){
							int eqIndex = param.indexOf("=");
							
							if(eqIndex > 0){
								params.add(ImmutableNamedValue.<String>of(param,null));
							}else{
								params.add(ImmutableNamedValue.of(param.substring(0,eqIndex),param.substring(eqIndex + 1)));
							}
						}
					}
					
					resources.add(new Resource(resourceName, resourceKey, Enumerables.of(params), Enumerables.of(resources)));
					continue;
				}
				
				return ;
			}
			
			if(!resources.isEmpty()){
				this.resource = resources.get(resources.size() - 1);	
			}
		}else{
			resource = NIL_RESOURCE;
		}
	}
	
	public static final class Resource {
		private final String		                   name;
		private final String		                   key;
		private final Enumerable<NamedValue<String>> parameters;
		private final Enumerable<Resource>           parents;
		
		Resource(String name,String key,Enumerable<NamedValue<String>> parameters,Enumerable<Resource> parents) {
			this.name       = name;
			this.key        = key;
			this.parameters = parameters;
			this.parents    = parents;
        }

		public String getName() {
			return name;
		}

		public String getKey() {
			return key;
		}
		
		public Enumerable<Resource> getParents() {
        	return parents;
        }

		public boolean hasParent(){
			return parents.isEmpty();
		}
		
		public Enumerable<NamedValue<String>> getParameters() {
			return parameters;
		}
		
		public String getParameter(String name) {
			NamedValue<String> p = parameters.firstOrNull(Predicates.<NamedValue<String>>nameEqualsIgnoreCase(name));
			
			return null == p ? null : p.getValue();
		}
	}
}