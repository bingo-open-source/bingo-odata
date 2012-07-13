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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Predicates;

public class ODataParametersImpl implements ODataParameters {
	
	private final Enumerable<ODataParameter> parameters;
	
	private Map<String, Object> map;
	
	public ODataParametersImpl(List<ODataParameter> parameters) {
	    this.parameters = Enumerables.of(parameters);
    }
	
	public ODataParametersImpl(Enumerable<ODataParameter> parameters) {
	    this.parameters = parameters;
    }

	public ODataParameter getParameter(String name) {
	    return Enumerables.firstOrNull(parameters,Predicates.<ODataParameter>nameEquals(name));
    }

	public Map<String, Object> getParametersMap() {
		if(null == map){
			map = new LinkedHashMap<String, Object>();
			
			for(ODataParameter p : parameters){
				map.put(p.getName(), p.getValue());
			}
		}
	    return map;
    }

	public Iterator<ODataParameter> iterator() {
	    return parameters.iterator();
    }

	public Enumerable<ODataParameter> asEnumerable() {
	    return parameters;
    }

	public boolean isEmpty() {
	    return parameters.isEmpty();
    }

	public int size() {
	    return parameters.size();
    }
}
