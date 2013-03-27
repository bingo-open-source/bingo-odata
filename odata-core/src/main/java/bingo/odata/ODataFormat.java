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

import bingo.lang.Valued;
import bingo.odata.ODataConstants.ContentTypes;

public enum ODataFormat implements Valued<String> {
	
	Default("default",""),
	
	Atom("atom",ContentTypes.APPLICATION_ATOM_XML),
	
	Json("json",ContentTypes.APPLICATION_JSON_LIGHT),
	
	VerboseJson("verbosejson",ContentTypes.APPLICATION_JSON_VERBOSE),
	
	Xml("xml",ContentTypes.APPLICATION_XML);
	
	private final String value;
	private final String contentType;
	
	ODataFormat(String value,String contentType){
		this.value       = value;
		this.contentType = contentType;
	}

	public String getValue() {
	    return value;
    }

	public String getContentType() {
    	return contentType;
    }

	@Override
    public String toString() {
		return value;
    }
	
	public boolean isAtom(){
		return this.equals(Atom);
	}
	
	public boolean isJson(){
		return this.equals(Json);
	}
	
	public boolean isVerboseJson(){
		return this.equals(VerboseJson);
	}
	
	public boolean isXml(){
		return this.equals(Xml);
	}
}