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
package bingo.odata.server.servlets;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import bingo.lang.Exceptions;
import bingo.odata.ODataResponse;

public class ODataServletResponseWrapper implements ODataResponse {
	
	private final HttpServletResponse	response;
	
	public ODataServletResponseWrapper(HttpServletResponse response){
		this.response = response;
	}
	
	public void setStatus(int status) {
		response.setStatus(status);
    }
	
	public void setHeader(String name, String value) {
		response.setHeader(name, value);
    }

	public String getContentType() {
		return response.getContentType();
	}
	
	public void setContentType(String contentType){
		response.setContentType(contentType);
	}

	public Writer getWriter() {
		try {
	        return response.getWriter();
        } catch (IOException e) {
        	throw Exceptions.uncheck(e);
        }
	}
}
