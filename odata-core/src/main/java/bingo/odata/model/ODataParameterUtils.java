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

import bingo.lang.Enumerables;
import bingo.lang.Predicates;
import bingo.lang.Strings;
import bingo.odata.ODataConverts;
import bingo.odata.ODataQueryOptions;
import bingo.odata.edm.EdmFunctionImport;
import bingo.odata.edm.EdmParameter;
import bingo.odata.edm.EdmSimpleType;
import bingo.odata.expression.Expressions;
import bingo.odata.expression.ExpressionParser;
import bingo.odata.expression.LiteralExpression;

public class ODataParameterUtils {
	
	public static ODataParameters parse(EdmFunctionImport func, String paramsString, ODataQueryOptions options){
		
		paramsString = Strings.trim(paramsString);
		
		if(paramsString.startsWith("(") && paramsString.endsWith("")){
			paramsString = paramsString.substring(1,paramsString.length() - 1);
		}

		List<ODataParameter> params = new ArrayList<ODataParameter>();
		
		if(!Strings.isEmpty(paramsString)){
			String[] tokens = Strings.split(paramsString,',');
			
			for (String token : tokens) {
				String[] nv = Strings.split(token, '=');
				
				if (nv.length != 2){
					throw new IllegalArgumentException("bad paramsString: " + paramsString);
				}
					
				String name   = nv[0].trim();
				String string = nv[1].trim();
				
				EdmParameter mp = func.getParameter(name);
				
				if(null == mp){
					throw new IllegalArgumentException("param '" + name + "' not found");
				}
				
				if(string.startsWith("@")){
					string = options.getOption(string.substring(1));
				}
				
				Object value = Expressions.literalValue((LiteralExpression)ExpressionParser.parse(string));
				
				params.add(createParameter(mp,value));
			}
		}
		
		for(EdmParameter p : func.getParameters()){
			if(!Enumerables.any(params, Predicates.<ODataParameter>nameEquals(p.getName()))){
				String string = options.getOption(p.getName());
				
				if(!Strings.isEmpty(string)){
					Object value = Expressions.literalValue((LiteralExpression)ExpressionParser.parse(string));
					
					params.add(createParameter(p,value));
				}
			}
		}
		
		return new ODataParametersImpl(params);
	}
	
	public static ODataParameter createParameter(EdmParameter parameter, Object value) {
		return new ODataParameterImpl(parameter,value);
	}
	
	public static ODataParameter createNullParameter(EdmParameter parameter) {
		return new ODataParameterImpl(parameter,null);
	}
	
	public static ODataParameter createParameter(EdmParameter parameter, String value) {
		return new ODataParameterImpl(parameter,ODataConverts.fromString((EdmSimpleType)parameter.getType(), value));
	}
}