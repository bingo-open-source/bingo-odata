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
package bingo.odata.consumer.requests;

import java.util.Date;
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
import bingo.odata.format.json.JsonWriterUtils;
import bingo.odata.utils.InternalTypeUtils;


public class FunctionRequest extends Request{
	
	private String entitySet = "";
	private String function;
	private String httpMethod;
	private Map<String, Object> funcParams;

	public FunctionRequest(ODataConsumerContext context, String serviceRoot) {
		super(context, serviceRoot);
	}

	@Override
	public String getResourcePath() {
		if(Strings.isBlank(entitySet)) {
			return function;
//		} else return entitySet + URL_FRAGMENT_DIVIDER + function;
		} else return function;
	}

	public String getFunction() {
		return function;
	}

	public FunctionRequest setFunction(String function) {
		this.function = function;
		return this;
	}

	public FunctionRequest setFuncParams(Map<String, Object> funcParams) {
		this.funcParams = funcParams;
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
			prepareFuncParams();
		}
	}

	@Override
	protected HttpContent generateContent() {
		if(getMethod().equals(HttpMethods.POST)) {
			String json = JSON.encode(getQualifiedFuncParamsForPost(), true);
			HttpContent content = ByteArrayContent.fromString(ContentTypes.APPLICATION_JSON_UTF8, json);
			return content;
		}
		return null;
	}

	private void prepareFuncParams() {
		Map<String, String> qualifiedFuncParams = getQualifiedFuncParamsForGet();
		this.addParameters(qualifiedFuncParams);
	}

	private Map<String, String> getQualifiedFuncParamsForGet() {
		Map<String, String> qualifiedFuncParams = new LinkedHashMap<String, String>();
		if(null != funcParams) {
			for (String key : funcParams.keySet()) {
				Object valueObject = funcParams.get(key);
				
				// TODO maybe need to do some url-format convert for some Type, like Date.
				String valueString = null;
				if(valueObject instanceof Date) {
					valueString = InternalTypeUtils.formatDateTime((Date) valueObject);
				} else {
					valueString = Objects.toString(valueObject);
				}
				
				qualifiedFuncParams.put(key, valueString);
			}
		}
		return qualifiedFuncParams;
	}
	
	private Map<String, Object> getQualifiedFuncParamsForPost() {
		Map<String, Object> qualifiedFuncParams = new LinkedHashMap<String, Object>();
		if(null != funcParams) {
			for (String key : funcParams.keySet()) {
				Object valueObject = funcParams.get(key);
				if(null == valueObject) continue;
				// TODO maybe need to do some url-format convert for some Type, like Date.
				Object value = null;
				if(valueObject instanceof Date) {
					value = JsonWriterUtils.formatDateTimeForJson((Date) valueObject);
				} else if(valueObject.getClass().isPrimitive()) {
					value = Objects.toString(valueObject);
				} else {
					value = valueObject;
				}
				
				qualifiedFuncParams.put(key, value);
			}
		}
		return qualifiedFuncParams;
	}
}