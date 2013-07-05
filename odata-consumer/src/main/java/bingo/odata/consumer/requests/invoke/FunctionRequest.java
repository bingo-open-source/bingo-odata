/*
 * Copyright 2013 the original author or authors.
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
package bingo.odata.consumer.requests.invoke;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpContent;

import bingo.lang.Objects;
import bingo.lang.Strings;
import bingo.lang.http.HttpMethods;
import bingo.lang.json.JSON;
import bingo.odata.ODataConstants.ContentTypes;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.requests.Request;


public class FunctionRequest extends Request{
	
	private String entitySet = "";
	private String function;
	private String httpMethod;
	private Map<String, Object> params;

	public FunctionRequest(ODataConsumerContext context, String serviceRoot) {
		super(context, serviceRoot);
	}

	@Override
	public String getResourcePath() {
		if(Strings.isBlank(entitySet)) {
			return function;
		} else return entitySet + URL_FRAGMENT_DIVIDER + function;
	}


	public String getFunction() {
		return function;
	}


	public FunctionRequest setFunction(String function) {
		this.function = function;
		return this;
	}

	public FunctionRequest setParams(Map<String, Object> params) {
		this.params = params;
		return this;
	}

	public String getEntitySet() {
		return entitySet;
	}

	public FunctionRequest setEntitySet(String entitySet) {
		if(Strings.isNotBlank(entitySet)) this.entitySet = entitySet;
		return this;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public FunctionRequest setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
		return this;
	}

	@Override
	public String getMethod() {
		if(Strings.isBlank(httpMethod)) {
			return super.getMethod();
		} else return httpMethod;
	}

	@Override
	protected void beforeSend() {
		if(getMethod().equals(HttpMethods.GET)) {
			prepareParams();
		}
	}

	@Override
	protected HttpContent genContent() {
		if(getMethod().equals(HttpMethods.POST)) {
			String json = JSON.encode(params, true);
			HttpContent content = ByteArrayContent.fromString(ContentTypes.APPLICATION_JSON_UTF8, json);
			return content;
		}
		return null;
	}

	private void prepareParams() {
		Map<String, String> qualifiedParams = new LinkedHashMap<String, String>();
		if(null != params) {
			for (String key : params.keySet()) {
				Object valueObject = params.get(key);
				
				// TODO maybe need to do some url-format convert for some Type, like Date.
				String valueString = Objects.toString(valueObject);
				
				qualifiedParams.put(key, valueString);
			}
		}
		this.addParameters(qualifiedParams);
	}
}