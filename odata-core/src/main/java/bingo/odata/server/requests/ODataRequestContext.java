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
import bingo.odata.ODataUrlInfo;
import bingo.odata.ODataVersion;
import bingo.odata.producer.ODataProvider;

public class ODataRequestContext {

	private final ODataProvider	provider;
	private final ODataVersion	    version;
	private final ODataFormat	    format;
	private final ODataUrlInfo	    urlInfo;

	public ODataRequestContext(ODataProvider producer,ODataVersion version,ODataFormat format,ODataUrlInfo urlInfo) {
		this.provider = producer;
		this.version  = version;
		this.format   = format;
		this.urlInfo  = urlInfo;
	}

	public ODataProvider getProvider() {
		return provider;
	}

	public ODataVersion getVersion() {
    	return version;
    }

	public ODataFormat getFormat() {
    	return format;
    }

	public ODataUrlInfo getUrlInfo() {
    	return urlInfo;
    }
}