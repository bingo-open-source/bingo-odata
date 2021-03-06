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
package bingo.odata.producer.servlets;

import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import bingo.lang.Exceptions;
import bingo.lang.Strings;
import bingo.lang.http.HttpHeaders;
import bingo.lang.http.HttpMethods;
import bingo.lang.servlet.Requests;
import bingo.odata.ODataRequest;

public class ODataServletRequest implements ODataRequest {
	
	private final HttpServletRequest request;
	private final String             method;
	private final String			 uriString;
	private final String             serviceRootPath;
	private final String             serviceRootUrl;
	private final String             resourcePath;
	private Map<String, String> 	 requestParameters;
	private Map<String, String>      headers;
	
	public ODataServletRequest(HttpServletRequest request){
		this(request,request.getServletPath());
	}
	
	public ODataServletRequest(HttpServletRequest request,String serviceRootPath){
		this.request               = request;
		this.method                = httpMethod();
		this.uriString             = request.getRequestURL().toString();
		this.serviceRootPath       = serviceRootPath;
		this.serviceRootUrl        = serviceRootUri();
		this.resourcePath          = resourcePath();
	}
	
	public Reader getReader() {
	    try {
	        return request.getReader();
        } catch (IOException e) {
        	throw Exceptions.uncheck(e);
        }
    }

	public String getContentType() {
		return request.getContentType();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getHeaders() {
		if(null == headers){
			headers = new LinkedHashMap<String, String>();
			Enumeration<String> names = request.getHeaderNames();
			
			while(names.hasMoreElements()){
				String name = names.nextElement();
				headers.put(name,request.getHeader(name));
			}
		}
		return headers;
	}

	public String getHeader(String name) {
	    return request.getHeader(name);
    }

	public String getParameter(String name) {
	    return request.getParameter(name);
    }
	
	public Map<String, String> getParameters() {
		if(null == requestParameters){
			requestParameters = Requests.getParametersMap(request);
		}
	    return requestParameters;
    }

	public String getMethod() {
	    return method;
    }

	public String getServiceRootPath() {
	    return serviceRootPath;
    }
	
	public String getResourcePath() {
	    return resourcePath;
    }

	public String getUriString() {
		return uriString;
	}
	
	public String getServiceRootUri() {
		return serviceRootUrl;
	}
	
	public String getQueryString() {
	    return request.getQueryString();
    }
	
	public boolean isDelete() {
	    return HttpMethods.DELETE.equalsIgnoreCase(getMethod());
    }

	public boolean isGet() {
		return HttpMethods.GET.equalsIgnoreCase(getMethod());
    }

	public boolean isPost() {
		return HttpMethods.POST.equalsIgnoreCase(getMethod());
    }

	public boolean isPut() {
		return HttpMethods.PUT.equalsIgnoreCase(getMethod());
    }

	public boolean isPatch() {
	    return HttpMethods.PATCH.equalsIgnoreCase(getMethod());
    }

	private String serviceRootUri(){
		String path = request.getContextPath() + serviceRootPath;
		
		int protocolIndex = uriString.indexOf("://");
		
		String serviceRootUri =  uriString.substring(0,uriString.indexOf(path, protocolIndex+3) + path.length());
		
		return serviceRootUri.endsWith("/") ? serviceRootUri : serviceRootUri + "/";
	}
	
	private String resourcePath() {
		// /{contextPath}/{serviceRootPath}/{resourcePath}
		String path = request.getRequestURI().substring((request.getContextPath() + serviceRootPath).length());
		
		if(!path.startsWith("/")){
			path = "/" + path;
		}
		
		return path.equals("") ? "/" : path.length() > 1 && path.endsWith("/") ?  path.substring(0,path.length() - 1) : path;
	}
	
	private String httpMethod(){
		String method = request.getHeader(HttpHeaders.X_HTTP_METHOD);
		
		if(Strings.isEmpty(method)){
			method = request.getHeader(HttpHeaders.X_HTTP_METHOD_OVERRIDE);
		}
		
		if(!Strings.isEmpty(method)){
			return method;
		}else{
			return request.getMethod();
		}
	}
}
