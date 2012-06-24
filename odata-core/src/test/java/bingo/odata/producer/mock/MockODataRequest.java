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

import bingo.odata.ODataConstants;
import bingo.odata.ODataFormat;
import bingo.odata.ODataRequest;
import bingo.odata.ODataVersion;

public class MockODataRequest implements ODataRequest {

	private String 		  contentType        = ODataConstants.ContentType.APPLICATION_ATOM_XML_UTF8;
	private ODataVersion dataServiceVersion = ODataVersion.V3;
	private ODataFormat  format				 = ODataFormat.Atom;
	
	private ODataVersion minDataServiceVersion = null;
	private ODataVersion maxDataServiceVersion = null;
	
	private String url;
	private String baseUrl;
	
	public MockODataRequest(){
		
	}
	
	public MockODataRequest(String url){
		this.url = url;
	}
	
	public String getContentType() {
    	return contentType;
    }

	public void setContentType(String contentType) {
    	this.contentType = contentType;
    }

	public ODataVersion getDataServiceVersion() {
    	return dataServiceVersion;
    }

	public void setDataServiceVersion(ODataVersion dataServiceVersion) {
    	this.dataServiceVersion = dataServiceVersion;
    }

	public ODataFormat getFormat() {
    	return format;
    }

	public void setFormat(ODataFormat format) {
    	this.format = format;
    }

	public ODataVersion getMinDataServiceVersion() {
    	return minDataServiceVersion;
    }

	public void setMinDataServiceVersion(ODataVersion minDataServiceVersion) {
    	this.minDataServiceVersion = minDataServiceVersion;
    }

	public ODataVersion getMaxDataServiceVersion() {
    	return maxDataServiceVersion;
    }

	public void setMaxDataServiceVersion(ODataVersion maxDataServiceVersion) {
    	this.maxDataServiceVersion = maxDataServiceVersion;
    }

	public String getUrl() {
    	return url;
    }

	public void setUrl(String url) {
    	this.url = url;
    }

	public String getBaseUrl() {
    	return baseUrl;
    }

	public void setBaseUrl(String baseUrl) {
    	this.baseUrl = baseUrl;
    }
}
