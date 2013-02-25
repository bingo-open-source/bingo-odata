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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ODataUrlInfo {

	private final String	           serviceRootUri;
	private final ODataResourcePath	   resourcePath;
	private final String			   resourceUri;
	private final ODataQueryOptions	   queryOptions;
	private final Map<String, String>  pathParameters = new HashMap<String, String>();

	public ODataUrlInfo(String serviceRootUri, String resourcePath, String queryString) {
	    super();
	    this.serviceRootUri  = serviceRootUri;
	    this.resourcePath    = new ODataResourcePath(resourcePath);
	    this.resourceUri     = serviceRootUri + resourcePath.substring(1);
	    this.queryOptions    = new ODataQueryOptions(queryString);
    }
	
	public ODataUrlInfo(String serviceRootUri, String resourcePath, Map<String, String> queryOptions) {
	    super();
	    this.serviceRootUri  = serviceRootUri;
	    this.resourcePath    = new ODataResourcePath(resourcePath);
	    this.resourceUri     = serviceRootUri + resourcePath.substring(1);
	    this.queryOptions    = new ODataQueryOptions(queryOptions);
    }

	public String getServiceRootUri() {
		return serviceRootUri;
	}

	public ODataResourcePath getResourcePath() {
		return resourcePath;
	}
	
	public String getResourceUri() {
    	return resourceUri;
    }

	public ODataQueryOptions getQueryOptions(){
		return queryOptions;
	}
	
	public Set<String> getPathParameterNames(){
		return pathParameters.keySet();
	}
	
	public String getPathParameter(String name){
		return pathParameters.get(name);
	}
	
	public void setPathParameter(String name,String value){
		pathParameters.put(name, value);
	}
	
	public void setPathParameters(Map<String, String> params){
		pathParameters.clear();
		pathParameters.putAll(params);
	}
}