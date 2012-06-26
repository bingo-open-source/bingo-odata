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
import bingo.lang.NamedValue;
import bingo.lang.Predicates;
import bingo.lang.Strings;
import bingo.lang.tuple.ImmutableNamedValue;

public class ODataUrl {

	private final String	          serviceRootPath;
	private final String	          serviceRootUrl;
	private final String	          queryString;
	private final ODataResourcePath  resourcePath;
	
	private Enumerable<NamedValue<String>> queryOptions;

	public ODataUrl(String serviceRootPath, String serviceRootUrl, String resourcePath, String queryString) {
	    super();
	    this.serviceRootPath = serviceRootPath;
	    this.serviceRootUrl  = serviceRootUrl;
	    this.resourcePath    = new ODataResourcePath(resourcePath);
	    this.queryString     = queryString;
    }

	public String getServiceRootPath() {
		return serviceRootPath;
	}

	public String getServiceRootUrl() {
		return serviceRootUrl;
	}

	public ODataResourcePath getResourcePath() {
		return resourcePath;
	}

	public String getQueryString() {
    	return queryString;
    }
	
	public Enumerable<NamedValue<String>> getQueryOptions(){
		if(null == queryOptions){
			parse();
		}
		return queryOptions;
	}
	
	public String getQueryOption(String name){
		NamedValue<String> v = getQueryOptions().firstOrNull(Predicates.<NamedValue<String>>nameEqualsIgnoreCase(name));
		return v == null ? null : v.getValue();
	}
	
	private void parse(){
		if(Strings.isEmpty(queryString)){
			queryOptions = Enumerables.empty();
		}else{
			String[] parts = Strings.split(queryString,"&");
			
			List<NamedValue<String>> params = new ArrayList<NamedValue<String>>();
			
			for(String part : parts){
				
				int eqIndex = part.indexOf('=');
				
				if(eqIndex > 0){
					params.add(ImmutableNamedValue.<String>of(part,null));
				}else{
					params.add(ImmutableNamedValue.of(part.substring(0,eqIndex),part.substring(eqIndex + 1)));
				}
			}
			
			queryOptions = Enumerables.of(params);
		}
	}
}