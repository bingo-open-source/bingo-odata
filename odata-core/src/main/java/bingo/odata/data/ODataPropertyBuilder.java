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
package bingo.odata.data;

import bingo.odata.ODataConverts;
import bingo.odata.edm.EdmProperty;
import bingo.odata.edm.EdmSimpleType;

public class ODataPropertyBuilder {

	public static ODataProperty build(EdmProperty property, Object value) {
		return new ODataPropertyImpl(property,value);
	}
	
	public static ODataProperty buildNull(EdmProperty property) {
		return new ODataPropertyImpl(property,null);
	}
	
	public static ODataProperty build(EdmProperty property, String value) {
		return new ODataPropertyImpl(property,ODataConverts.fromString((EdmSimpleType)property.getType(), value));
	}
}
