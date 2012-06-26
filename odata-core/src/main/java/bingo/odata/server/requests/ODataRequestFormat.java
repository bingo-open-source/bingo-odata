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
package bingo.odata.server.requests;

import bingo.odata.ODataFormat;

public class ODataRequestFormat {

	private final ODataFormat	format;

	private final String charset;

	private final boolean	 queryOption;
	
	public ODataRequestFormat(ODataFormat format, boolean queryOption) {
	    super();
	    this.format = format;
	    this.charset = null;
	    this.queryOption = queryOption;
    }
	
	public ODataRequestFormat(ODataFormat format, boolean queryOption, String charset) {
	    super();
	    this.format = format;
	    this.charset = charset;
	    this.queryOption = queryOption;
    }

	public ODataFormat getFormat() {
		return format;
	}

	public String getCharset() {
		return charset;
	}

	public boolean isQueryOption() {
    	return queryOption;
    }
}