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
package bingo.odata.consumer.requests;

import static bingo.odata.ODataConstants.Headers.DATA_SERVICE_VERSION;
import static bingo.odata.ODataConstants.Headers.MAX_DATA_SERVICE_VERSION;
import static bingo.odata.ODataConstants.Headers.MIN_DATA_SERVICE_VERSION;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;

import bingo.odata.consumer.ODataConsumerContext;


public class MetadataRequest extends Request{
	
	private static final String METADATA_DOCUMENT = "$metadata";

	public MetadataRequest(ODataConsumerContext context, String serviceRoot) {
		super(context, serviceRoot);
	}

	@Override
	protected GenericUrl generateUrl() {
		String str = serviceRoot + METADATA_DOCUMENT;
		GenericUrl url = new GenericUrl(str);
		return url;
	}

	@Override
	protected HttpHeaders generateHeaders() {
		HttpHeaders headers = super.generateHeaders();
		headers.setAccept("application/xml");
		return headers;
	}

	/**
	 * no need to set json format
	 */
	@Override
	protected void setUpDefaultHeaders() {
		this.addHeader(DATA_SERVICE_VERSION, context.getVersion().getValue());
		this.addHeader(MAX_DATA_SERVICE_VERSION, context.getVersion().getValue());
		this.addHeader(MIN_DATA_SERVICE_VERSION, context.getVersion().getValue());
	}

	@Override
	protected void setUpDefaultParameters() {
		// empty
	}
	
	
}