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

import bingo.odata.consumer.ODataConsumerContext;

public class FindNavigationPropertyRequest extends Request {
	private String entitySet;
	private Object id;
	private String naviProperty;

	public FindNavigationPropertyRequest(ODataConsumerContext context, String serviceRoot) {
		super(context, serviceRoot);
	}
	
	@Override
	public String getResourcePath() {
		return entitySet + "(" + id.toString() + ")" + URL_FRAGMENT_DIVIDER + naviProperty;
	}

	public String getEntitySet() {
		return entitySet;
	}

	public FindNavigationPropertyRequest setEntitySet(String entitySet) {
		this.entitySet = entitySet;
		return this;
	}

	public Object getId() {
		return id;
	}

	public FindNavigationPropertyRequest setId(Object id) {
		this.id = id;
		return this;
	}

	public String getNaviProperty() {
		return naviProperty;
	}

	public FindNavigationPropertyRequest setNaviProperty(String naviProperty) {
		this.naviProperty = naviProperty;
		return this;
	}

}