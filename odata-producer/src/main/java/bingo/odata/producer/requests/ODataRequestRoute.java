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

import java.util.Map;

import bingo.lang.uri.UriPattern;

public class ODataRequestRoute {
	private String	            method;
	private String	            regex;
	private UriPattern 			pattern;
	private ODataRequestHandler handler;
	
	public static ODataRequestRoute compile(String method,String regex,ODataRequestHandler handler){
		ODataRequestRoute route = new ODataRequestRoute();
		route.method  = method;
		route.regex   = regex;
		route.handler = handler;
		route.compile();
		return route;
	}
	
	public boolean matches(String method,String uri,Map<String, String> variables){
		if(null == method || this.method.equals("*") || this.method.equalsIgnoreCase(method.trim())){
			return pattern.matches(uri,variables);
		}
		return false;
	}
	
	ODataRequestHandler getHandler(){
		return handler;
	}
	
	private void compile() {
		if(null == method || "".equals(method = method.trim())){
			method = "*";
		}
		this.pattern = UriPattern.compile(regex);
	}
}