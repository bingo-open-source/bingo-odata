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

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import bingo.odata.ODataResponse;

public class MockODataResponse implements ODataResponse {

	private String	      contentType  = null;
	private Writer	      writer	   = new StringWriter();
	private int          status       = 200;
	private Map<String, String> headers = new HashMap<String, String>();

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setHeader(String name, String value) {
		headers.put(name, value);	    
    }
	
	public String getHeader(String name){
		return headers.get(name);
	}

	public int getStatus() {
    	return status;
    }

	public void setStatus(int status) {
    	this.status = status;
    }

	public Writer getWriter() {
		return writer;
	}
	
	public String getContent(){
		return writer.toString();
	}
}