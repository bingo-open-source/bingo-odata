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

import java.net.URL;

import com.google.api.client.http.GenericUrl;

import bingo.lang.Strings;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.requests.builders.RequestBuilder;
import bingo.odata.consumer.requests.builders.RequestBuilderBase;

public class RetrieveEntityRequest extends Request {
	
	private String entitySet;
	private Object id;
	
	public RetrieveEntityRequest(ODataConsumerContext context, String serviceRoot) {
		super(context, serviceRoot);
	}

	@Override
	protected GenericUrl genUrl() {// TODO change to override just resourcePath.
		String string = this.serviceRoot + entitySet + "('" + id.toString() + "')";
		String queryString = getQueryString();
		if(Strings.isNotBlank(queryString)) {
			string += "?" + queryString;
		}
		GenericUrl url = new GenericUrl(string);
		return url;
	}

	public String getEntitySet() {
		return entitySet;
	}

	public RetrieveEntityRequest setEntitySet(String entitySet) {
		this.entitySet = entitySet;
		return this;
	}

	public Object getId() {
		return id;
	}

	public RetrieveEntityRequest setId(Object id) {
		this.id = id;
		return this;
	}

}