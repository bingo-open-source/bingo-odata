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
package bingo.odata.model;

import java.util.List;

import bingo.lang.For;
import bingo.lang.NamedValue;
import bingo.lang.NamedValues;
import bingo.lang.tuple.ImmutableNamedValues;
import bingo.odata.expression.Expressions;

class ODataKeyImpl implements ODataKey {
	
	private final boolean			    isPrimitive;
	private final Object 			    primitiveValue;
	private final NamedValues<Object> namedValues; 
	
	private String keyString;
	
	public ODataKeyImpl(Object value) {
		this.primitiveValue = value;
		this.isPrimitive    = true;
		this.namedValues    = null;
	}
	
	public ODataKeyImpl(List<NamedValue<Object>> namedValues) {
		this.namedValues    = ImmutableNamedValues.of(namedValues);
		this.primitiveValue = null;
		this.isPrimitive    = false;
	}
	
	public ODataKeyImpl(NamedValue<Object>... namedValues) {
		this.namedValues    = ImmutableNamedValues.of(namedValues);
		this.primitiveValue = null;
		this.isPrimitive    = false;		
	}
	
	public NamedValues<Object> getNamedValues() {
	    return namedValues;
    }

	public Object getPrimitiveValue() {
	    return primitiveValue;
    }

	public boolean isNamedValues() {
	    return null != primitiveValue;
    }

	public boolean isPrimitiveValue() {
	    return isPrimitive;
    }

	public String toKeyString() {
		if(null == keyString){
			if(isPrimitive){
				keyString = "(" + keyString(primitiveValue) + ")";
			}else{
				keyString = keyString(namedValues);
			}
		}
		return keyString;
	}
	
	private static String keyString(NamedValues<Object> namedValues) {

		final StringBuilder key = new StringBuilder();
		
		For.each(namedValues,
				new For.Action1<NamedValue<Object>>() {
					public void item(boolean isFirstItem, NamedValue<Object> item) {
						if(!isFirstItem){
							key.append(',');
						}
						key.append(keyString(item.getValue()));
		            }
				});
		
		return "(" + key.toString() + ")";
	}
	
	@SuppressWarnings({"rawtypes" })
	private static String keyString(Object keyValue) {
		if(keyValue instanceof NamedValue){
			NamedValue nv = (NamedValue)keyValue;
			
			return nv.getName() + "=" + Expressions.asFilterString(Expressions.literal(nv.getValue()));
		}else{
			return Expressions.asFilterString(Expressions.literal(keyValue));	
		}
	}
}
