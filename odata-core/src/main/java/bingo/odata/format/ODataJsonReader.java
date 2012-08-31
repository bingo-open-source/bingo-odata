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
package bingo.odata.format;

import java.io.Reader;

import bingo.odata.ODataContext;
import bingo.odata.ODataObject;
import bingo.odata.ODataReader;
import bingo.lang.json.JSON;
import bingo.lang.json.JSONObject;

public abstract class ODataJsonReader<T extends ODataObject> implements ODataReader<T> {

	public final T read(ODataContext context, Reader in) throws Throwable {
	    return read(context,JSON.decode(in));
    }
	
	protected abstract T read(ODataContext context,JSONObject json);
}
