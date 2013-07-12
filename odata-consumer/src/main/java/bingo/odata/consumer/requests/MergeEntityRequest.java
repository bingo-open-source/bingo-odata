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

import java.util.Map;

import bingo.lang.http.HttpMethods;
import bingo.lang.json.JSON;
import bingo.odata.consumer.ODataConsumerContext;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpContent;

public class MergeEntityRequest extends Request {
	private Object id;
	private String entitySet;
	private Map<String, Object> fields;
	public MergeEntityRequest(ODataConsumerContext context, String serviceRoot) {
		super(context, serviceRoot);
	}
	public Object getId() {
		return id;
	}
	public MergeEntityRequest setId(Object id) {
		this.id = id;
		return this;
	}
	public String getEntitySet() {
		return entitySet;
	}
	public MergeEntityRequest setEntitySet(String entitySet) {
		this.entitySet = entitySet;
		return this;
	}
	public Map<String, Object> getFields() {
		return fields;
	}
	public MergeEntityRequest setFields(Map<String, Object> fields) {
		this.fields = fields;
		return this;
	}
	@Override
	public String getResourcePath() {
		return entitySet + "('" + id + "')";
	}
	
	@Override
	public String getMethod() {
		return HttpMethods.PATCH;
	}
	@Override
	protected HttpContent genContent() {
		String json = JSON.encode(fields, true);
		HttpContent content = ByteArrayContent.fromString(accept, json);
		return content;
	}
}