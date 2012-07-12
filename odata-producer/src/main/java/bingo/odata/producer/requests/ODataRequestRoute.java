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
package bingo.odata.producer.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ODataRequestRoute {
	private String	             method;
	private String	             path;
	private String              regex;
	private ODataRequestHandler handler;
	private Pattern	             pattern;
	private String[]	         pathParams;
	
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
			pathParams = findParams();
			pattern    = Pattern.compile(regex);
		}
	}
	
	private String[] findParams(){
		StringBuilder buf = new StringBuilder();
		char[] chars = path.toCharArray();

		List<String> params = new ArrayList<String>();
		
		for(int i=0;i<chars.length;i++){
			char c = chars[i];

			if(c == '{'){
				for(int j=i+1;j<chars.length;j++){
					char c1 = chars[j];
					
					if(c1 == '}'){
						String param = path.substring(i+1,j);
						
						int index = param.indexOf(":");
						
						if(index > 1){
							String name  = param.substring(0,index);
							String regex = param.substring(index+1);
							
							buf.append("(" + regex.trim() + ")");
							params.add(name.trim());
						}else{
							buf.append("([a-zA-Z_0-9\\.]+)");
							params.add(param.trim());
						}
						
						i = j;
						break;
					}
				}
			}else{
				buf.append(c);
			}
		}
		
		regex = buf.toString();
		
		return params.toArray(new String[]{});
	}
}
