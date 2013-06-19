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

import bingo.lang.http.HttpMethods;


public class DeleteEntityRequest extends Request {
	
	private String entitySet;
	private Object id;
	
	public DeleteEntityRequest(String serviceRoot) {
		this.serviceRoot = serviceRoot;
	}
	
	public Object getId() {
		return id;
	}
	public DeleteEntityRequest setId(Object id) {
		this.id = id;
		return this;
	}
	public String getEntitySet() {
		return entitySet;
	}
	public DeleteEntityRequest setEntitySet(String entitySet) {
		this.entitySet = entitySet;
		return this;
	}

	@Override
	public String getMethod() {
		return HttpMethods.DELETE;
	}

	@Override
	public String getResourcePath() {
		return URL_FRAGMENT_DIVIDER + entitySet + "('" + id + "')";
	}
	
}