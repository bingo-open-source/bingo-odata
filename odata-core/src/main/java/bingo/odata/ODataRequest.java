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

import java.io.Reader;
import java.util.Map;

public interface ODataRequest {
	
	Reader getReader();

	String getContentType();
	
	String getHeader(String name);
	
	String getParameter(String name);
	
	Map<String, String> getParameters();
	
	String getMethod();
	
	String getServiceRootPath();
	
	/**
	 * must be ends with '/'
	 */
	String getServiceRootUri();
	
	/**
	 * must be starts with '/' and not ends with '/'
	 */
	String getResourcePath();
	
	String getUriString();
	
	String getQueryString();
	
	boolean isPost();
	
	boolean isGet();
	
	boolean isPut();
	
	boolean isDelete();
	
	boolean isPatch();
	
	boolean isMerge();
}