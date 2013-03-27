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
package bingo.odata.server.mock;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import bingo.lang.Strings;
import bingo.odata.ODataFormat;
import bingo.odata.ODataRequest;
import bingo.odata.ODataVersion;
import bingo.odata.ODataConstants.Headers;
import bingo.odata.ODataConstants.QueryOptions;
import bingo.lang.http.HttpContentTypes;
import bingo.lang.http.HttpMethods;

public class MockODataRequest implements ODataRequest {

	private String	contentType	= HttpContentTypes.APPLICATION_ATOM_XML_UTF8;

	private String	method;
	private String  methodOverride;
	private String	serviceRootPath;
	private String	resourcePath;
	private String	serviceRootUri;
	private String	uriString;
	private String  queryString;
	private String  content;
	
	private final Map<String, String> headers    = new HashMap<String, String>();
	private final Map<String, String> parameters = new HashMap<String, String>();

	public MockODataRequest() {

	}

	public MockODataRequest(String url) {
		this.uriString = url;
	}
	
	public Reader getReader() {
	    return new StringReader(Strings.trim(content));
    }

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getContent() {
    	return content;
    }

	public void setContent(String content) {
    	this.content = content;
    }

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getMethodOverride() {
		return methodOverride;
	}

	public void setMethodOverride(String methodOverride) {
		this.methodOverride = methodOverride;
	}

	public String getMethodOverrideOrOriginal() {
	    return Strings.firstNotEmpty(methodOverride,method);
    }

	public String getServiceRootPath() {
		return serviceRootPath;
	}

	public void setServiceRootPath(String serviceRootPath) {
		this.serviceRootPath = serviceRootPath;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getUriString() {
		return uriString;
	}

	public void setUriString(String url) {
		this.uriString = url;
	}

	public String getServiceRootUri() {
		return serviceRootUri;
	}

	public void setServiceRootUri(String serviceRootUrl) {
    	this.serviceRootUri = serviceRootUrl;
    }
	
	public String getQueryString() {
	    return queryString;
    }

	public String getHeader(String name) {
	    return headers.get(name);
    }

	public String getParameter(String name) {
	    return parameters.get(name);
    }
	
	public Map<String, String> getParameters() {
	    return parameters;
    }

	public void setHeader(String name,String value){
		headers.put(name, value);
	}
	
	public void setParameter(String name,String value){
		parameters.put(name, value);
	}
	
	public void setFormat(ODataFormat format){
		setParameter(QueryOptions.FORMAT, format.getValue());
	}
	
	public void setDataServiceVerion(ODataVersion version){
		setHeader(Headers.DATA_SERVICE_VERSION, version.getValue());
	}
	
	public void setMinDataServiceVersion(ODataVersion version){
		setHeader(Headers.MIN_DATA_SERVICE_VERSION, version.getValue());
	}
	
	public void setMaxDataServiceVersion(ODataVersion version){
		setHeader(Headers.MAX_DATA_SERVICE_VERSION, version.getValue());
	}

	public boolean isDelete() {
	    return HttpMethods.DELETE.equalsIgnoreCase(getMethodOverrideOrOriginal());
    }

	public boolean isGet() {
		return HttpMethods.GET.equalsIgnoreCase(getMethodOverrideOrOriginal());
    }

	public boolean isPost() {
		return HttpMethods.POST.equalsIgnoreCase(getMethodOverrideOrOriginal());
    }

	public boolean isPut() {
		return HttpMethods.PUT.equalsIgnoreCase(getMethodOverrideOrOriginal());
    }

	public boolean isPatch() {
	    return HttpMethods.PATCH.equalsIgnoreCase(getMethodOverrideOrOriginal());
    }
}
