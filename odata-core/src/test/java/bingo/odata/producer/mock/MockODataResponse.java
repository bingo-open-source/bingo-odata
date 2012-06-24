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
package bingo.odata.producer.mock;

import java.io.StringWriter;
import java.io.Writer;

import bingo.odata.ODataConstants;
import bingo.odata.ODataResponse;
import bingo.odata.ODataVersion;

public class MockODataResponse implements ODataResponse {

	private String	      contentType  = ODataConstants.ContentType.APPLICATION_ATOM_XML_UTF8;
	private ODataVersion version	   = ODataVersion.V3;
	private Writer	      writer	   = new StringWriter();

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public ODataVersion getVersion() {
		return version;
	}

	public void setVersion(ODataVersion version) {
		this.version = version;
	}

	public Writer getWriter() {
		return writer;
	}
	
	public String getContent(){
		return writer.toString();
	}
}