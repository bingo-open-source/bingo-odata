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
package bingo.odata.producer.sql;

import bingo.odata.expression.Expression;

public class SqlExpressions {
	
	protected SqlExpressions(){
		
	}
	
	public static SqlExpression filter(Expression odataExpression){
		SqlExpressionVisitor v = new SqlExpressionVisitor();
		
		odataExpression.visit(v);
		
		return new SqlExpression(v.getText(),v.getParamValues().toArray(),v.getParamTypes().toArray(new Integer[]{}));
	}

}
