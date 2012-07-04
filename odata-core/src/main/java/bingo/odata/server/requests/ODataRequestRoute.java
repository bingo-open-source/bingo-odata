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
package bingo.odata.server.requests;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bingo.lang.Strings;

public class ODataRequestRoute {
	private static Pattern PARAMS_PATTERN = Pattern.compile("\\{([a-zA-Z0-9_]+[\\*]?)\\}");
	
	protected String		        method;
	protected String		        path;
	protected ODataRequestHandler	handler;
	private Pattern pattern;
	private String[] pathParams;
	
	public static ODataRequestRoute compile(String method,String path,ODataRequestHandler handler){
		ODataRequestRoute route = new ODataRequestRoute();
		route.method  = method;
		route.path    = path;
		route.handler = handler;
		route.compile();
		return route;
	}
	
	public boolean matches(String method,String path,Map<String, String> params){
		compile();
		
		boolean matched = false;
		
		if(null == method || this.method.equals("*") || this.method.equalsIgnoreCase(method.trim())){
			if(this.path.equals("*")){
				matched = true;
			}else{
				if(null == path || "".equals(path = path.trim())){
					path = "/";
				}
				
				Matcher matcher = pattern.matcher(path);
				
	            if (matcher.matches()) {
	            	for(int i=0;i<pathParams.length;i++){
	            		String param = pathParams[i];
	            		params.put(param, matcher.group(i+1));
	            	}
	            	
	            	matched    = true;
	            }					
			}
		}
		
		return matched;
	}
	
	ODataRequestHandler getHandler(){
		return handler;
	}
	
	private void compile() {
		if(null == method || "".equals(method = method.trim())){
			method = "*";
		}
		
		if(null == path || "".equals(path = path.trim())){
			path = "*";
		}else{
			Ref<String> expr      = new Ref<String>(path);
			pathParams   = findParams(expr,true);
			
			//replace "*" chartacter to regex "[^/]*"
			String regex = expr.value;
			
			regex = Strings.replace(regex, "*", "[^/]*");
			regex = Strings.replace(regex, "$", "\\$");
			
			pattern = Pattern.compile(regex);
		}
	}
	
	private static String[] findParams(Ref<String> text,boolean translate){
		Matcher matcher = PARAMS_PATTERN.matcher(text.value);
		
		ArrayList<String> params = new ArrayList<String>();
		while(matcher.find()){
			String param = matcher.group(1);
			
			if(translate){
				if(param.endsWith("*")){
					text.value = text.value.replace("{" + param + "}","([a-zA-Z_0-9/]+)");
					param = param.substring(0,param.length() - 1);
				}else{
					//replace "{param}" to regex "([a-zA-Z_0-9]+)"
					text.value = text.value.replace("{" + param + "}","([a-zA-Z_0-9]+)");
				}
			}
			params.add(param.trim());
		}
		
		return params.toArray(new String[]{});
	}
	
	private static final class Ref<E> {
		private E value;
		private Ref(E value){
			this.value = value;
		}
	}
}
