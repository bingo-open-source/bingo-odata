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

import bingo.lang.Assert;
import bingo.odata.ODataConstants;
import bingo.odata.consumer.ODataConsumerContext;


public class CountRequest extends Request {
	
	private String entitySet;

	public CountRequest(ODataConsumerContext context, String serviceRoot) {
		super(context, serviceRoot);
	}

	public String getEntitySet() {
		return entitySet;
	}

	public CountRequest setEntitySet(String entitySet) {
		this.entitySet = entitySet;
		return this;
	}

	@Override
	public String getResourcePath() {
		Assert.notBlank(entitySet);
		return entitySet + URL_FRAGMENT_DIVIDER + "$count";
	}

	@Override
	protected void setUpDefaultParameters_format() {
		// not using default json format for some odata server may response error.
	}

	@Override
	protected void setUpDefaultHeaders_accept() {
		// not using default json format for some odata server may response error.
		this.setAccept("");
	}

}