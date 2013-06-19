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

import com.google.api.client.http.GenericUrl;



public class RetrieveEntitySetRequest extends Request{
	
	private String entitySet;
	
	public RetrieveEntitySetRequest(String serviceRoot) {
		this.serviceRoot = serviceRoot;
	}

	@Override
	protected GenericUrl genUrl() {
		String string = this.serviceRoot + entitySet;
		string = addQueryString(string);
		GenericUrl url = new GenericUrl(string);
		return url;
	}

	public String getEntitySet() {
		return entitySet;
	}

	public RetrieveEntitySetRequest setEntitySet(String entitySet) {
		this.entitySet = entitySet;
		return this;
	}
	
}