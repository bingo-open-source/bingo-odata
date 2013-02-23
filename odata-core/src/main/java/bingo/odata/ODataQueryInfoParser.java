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
package bingo.odata;

import java.util.ArrayList;
import java.util.List;

import bingo.lang.Enums;
import bingo.lang.Strings;
import bingo.odata.expression.BoolExpression;
import bingo.odata.expression.Expression;
import bingo.odata.expression.EntitySimpleProperty;
import bingo.odata.expression.ExpressionParser;
import bingo.odata.expression.OrderByExpression;

public class ODataQueryInfoParser {
	
	public static ODataQueryInfo parse(ODataQueryOptions options){
		return new ODataQueryInfo(parseExpand(options.getExpand()), 
								   parseFilter(options.getFilter()), 
								   parseOrderBy(options.getOrderBy()),
								   parseSkip(options.getSkip()),
								   parseTop(options.getTop()),
								   options.getSkipToken(),
								   parseInlineCount(options.getInlineCount()),
								   parseSelect(options.getSelect()),
								   options.getOptions());
	}
	
	public static ODataQueryInfo parseForSingleEntity(ODataQueryOptions options){
		return new ODataQueryInfo(parseExpand(options.getExpand()), 
								   null, 
								   null,
								   null,
								   null,
								   null,
								   null,
								   parseSelect(options.getSelect()),
								   options.getOptions());
	}
	
	public static ODataInlineCount parseInlineCount(String value) {
		return Strings.isEmpty(value) ? null : Enums.valueOf(ODataInlineCount.class, value);
	}
	
	public static BoolExpression parseFilter(String value){
		Expression expr = Strings.isEmpty(value) ? null : ExpressionParser.parse(value);
		
		if(null != expr && !(expr instanceof BoolExpression)){
			throw ODataErrors.badRequest("invalid filter,should be boolean expression");
		}
		
		return (BoolExpression)expr;
	}
	
	public static List<OrderByExpression> parseOrderBy(String value){
		return Strings.isEmpty(value) ? new ArrayList<OrderByExpression>() : ExpressionParser.parseOrderBy(value);
	}
	
	public static Integer parseTop(String value){
		return Strings.isEmpty(value) ? null : Integer.parseInt(value);
	}
	
	public static Integer parseSkip(String value){
		return Strings.isEmpty(value) ? null : Integer.parseInt(value);
	}
	
	public static List<EntitySimpleProperty> parseExpand(String value){
		return Strings.isEmpty(value) ? new ArrayList<EntitySimpleProperty>() : ExpressionParser.parseExpand(value);
	}
	
	public static List<EntitySimpleProperty> parseSelect(String value){
		return Strings.isEmpty(value) ? new ArrayList<EntitySimpleProperty>() : ExpressionParser.parseExpand(value);
	}
}
