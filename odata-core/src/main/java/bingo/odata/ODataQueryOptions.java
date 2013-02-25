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
import java.util.LinkedHashMap;
import java.util.Map;

import bingo.lang.Immutables;
import bingo.lang.Strings;
import bingo.odata.ODataConstants.QueryOptions;

public class ODataQueryOptions {

	private final Map<String, String> options;

	public ODataQueryOptions(Map<String, String> options){
		this.options = Immutables.mapOf(options);
	}
	
	public ODataQueryOptions(String queryString){
		this.options = parse(queryString);
	}
	
	public String getExpand(){
		return options.get(QueryOptions.EXPAND);
	}
	
	public String getFilter(){
		return options.get(QueryOptions.FILTER);
	}
	
	public String getFormat(){
		return options.get(QueryOptions.FORMAT);
	}
	
	public String getOrderBy(){
		return options.get(QueryOptions.ORDER_BY);
	}
	
	public String getSkip(){
		return options.get(QueryOptions.SKIP);
	}
	
	public String getTop(){
		return options.get(QueryOptions.TOP);
	}
	
	public String getSkipToken(){
		return options.get(QueryOptions.SKIP_TOKEN);
	}
	
	public String getInlineCount(){
		return options.get(QueryOptions.INLINE_COUNT);
	}
	
	public String getSelect(){
		return options.get(QueryOptions.SELECT);
	}
	
	public String getCallback(){
		return options.get(QueryOptions.CALLBACK);
	}
	
	public String getOption(String name){
		return options.get(name);
	}
	
	public Map<String, String> getOptionsMap() {
    	return options;
    }
	
	private Map<String, String> parse(String queryString){
		if(Strings.isEmpty(queryString)){
			return new HashMap<String, String>();
		}else{
			String[] parts = Strings.split(queryString,"&");
			
			Map<String, String> params = new LinkedHashMap<String, String>();
			
			for(String part : parts){
				
				int eqIndex = part.indexOf('=');
				
				if(eqIndex > 0){
					params.put(part,Strings.EMPTY);
				}else{
					params.put(part.substring(0,eqIndex),part.substring(eqIndex + 1));
				}
			}
			
			return params;
		}
	}
}
