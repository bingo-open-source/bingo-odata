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
package bingo.odata.consumer.requests.metadata;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;

import bingo.odata.consumer.requests.Request;


public class MetadataDocumentRequest extends Request{
	
	private static final String METADATA_DOCUMENT = "$metadata";

	public MetadataDocumentRequest(String serviceRoot) {
		this.serviceRoot = serviceRoot;
	}

	@Override
	protected GenericUrl genUrl() {
		String str = serviceRoot + URL_FRAGMENT_DIVIDER + METADATA_DOCUMENT;
		GenericUrl url = new GenericUrl(str);
		return url;
	}

	@Override
	protected HttpHeaders genHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept("application/xml");
		return headers;
	}
	
	
}