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
package bingo.odata.model;

import bingo.lang.tuple.ImmutableNamedValue;
import bingo.odata.ODataObject;
import bingo.odata.ODataObjectKind;

public class ODataNamedValueImpl extends ImmutableNamedValue<ODataObject> implements ODataNamedValue {

	private static final long serialVersionUID = 300652794613428927L;

	protected ODataObjectKind valueKind;
	
	public ODataNamedValueImpl(String name,ODataObjectKind valueKind, ODataObject value) {
		super(name,value);
		this.valueKind = valueKind;
    }

	public ODataObjectKind getValueKind() {
	    return valueKind;
    }
}