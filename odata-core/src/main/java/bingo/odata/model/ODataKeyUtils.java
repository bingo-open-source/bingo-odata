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

import java.util.ArrayList;
import java.util.List;

import bingo.lang.Assert;
import bingo.lang.NamedValue;
import bingo.lang.Strings;
import bingo.lang.tuple.ImmutableNamedValue;
import bingo.odata.expression.Expressions;
import bingo.odata.expression.ExpressionParser;
import bingo.odata.expression.LiteralExpression;

public class ODataKeyUtils {

	public static ODataKey parse(String keyString) {
		keyString = Strings.trim(keyString);
		
		if(keyString.startsWith("(") && keyString.endsWith("")){
			keyString = keyString.substring(1,keyString.length() - 1);
		}
		
		Assert.notEmpty(keyString,"keyString cannot be empty");
		
		String[] tokens = Strings.split(keyString,',');
		
		if(tokens.length == 1 && !tokens[0].contains("=")){
			return new ODataKeyImpl(toValue(tokens[0]));
		}
		
		List<NamedValue<Object>> values = new ArrayList<NamedValue<Object>>(tokens.length);
		
		for (String token : tokens) {
			String[] nv = Strings.split(token, '=');
			
			if (nv.length != 2){
				throw new IllegalArgumentException("bad keyString: " + keyString);
			}
				
			String valueString = nv[1].trim();
			try {
				values.add(ImmutableNamedValue.of(nv[0].trim(),toValue(valueString)));
			} catch (Exception e) {
				throw new IllegalArgumentException(String.format("bad valueString [%s] as part of keyString [%s]", valueString, keyString), e);
			}
		}
		
		return new ODataKeyImpl(values);
	}
	
	private static Object toValue(String valueString){
		return Expressions.literalValue((LiteralExpression)ExpressionParser.parse(valueString));
	}
	
	protected ODataKeyUtils(){
		
	}
}