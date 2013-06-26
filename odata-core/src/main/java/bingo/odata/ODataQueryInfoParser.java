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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bingo.lang.Collections;
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
								   options.getAllOptionsMap());
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
								   options.getAllOptionsMap());
	}
	
	public static ODataInlineCount parseInlineCount(String value) {
		return Strings.isEmpty(value) ? null : Enums.valueOf(ODataInlineCount.class, value);
	}
	
	public static String toInlineCountString(ODataInlineCount inlineCount) {
		if(null == inlineCount) return null;
		String valueString = inlineCount.getValue();
		if(Strings.isNotBlank(valueString)) {
			return ODataConstants.QueryOptions.INLINE_COUNT + "=" + valueString;
		} else return null;
	}
	
	public static BoolExpression parseFilter(String value){
		Expression expr = Strings.isEmpty(value) ? null : ExpressionParser.parse(value);
		
		if(null != expr && !(expr instanceof BoolExpression)){
			throw ODataErrors.badRequest("invalid filter,should be boolean expression");
		}
		
		return (BoolExpression)expr;
	}
	
	public static String toFilterString(BoolExpression filter) {
		if(null == filter) return null;
		// TODO resolve filter expression.
		return null;
	}
	
	public static List<OrderByExpression> parseOrderBy(String value){
		return Strings.isEmpty(value) ? new ArrayList<OrderByExpression>() : ExpressionParser.parseOrderBy(value);
	}

	public static String toOrderByString(List<OrderByExpression> orderBy) {
		if(!Collections.isEmpty(orderBy)) {
			StringBuilder builder = new StringBuilder(ODataConstants.QueryOptions.ORDER_BY);
			builder.append("=");
			for (OrderByExpression item : orderBy) {
				builder.append("").append(" ").append(item.getDirection()).append(",");// TODO how to get property name?
			}
			return builder.substring(0, builder.length() - 1);
		} else return null;
	}
	
	public static Integer parseTop(String value){
		return Strings.isEmpty(value) ? null : Integer.parseInt(value);
	}
	
	public static String toTopString(Integer top) {
		if(null == top) return null;
		return ODataConstants.QueryOptions.TOP + "=" + top;
	}
	
	public static Integer parseSkip(String value){
		return Strings.isEmpty(value) ? null : Integer.parseInt(value);
	}
	
	public static String toSkipString(Integer skip) {
		if(null == skip) return null;
		return ODataConstants.QueryOptions.SKIP + "=" + skip;
	}
	
	public static String toSkipTokenString(String skipToken) {
		if(Strings.isNotBlank(skipToken)) {
			return ODataConstants.QueryOptions.SKIP_TOKEN + "=" + skipToken;
		} else return null;
	}
	
	public static List<EntitySimpleProperty> parseExpand(String value){
		return Strings.isEmpty(value) ? new ArrayList<EntitySimpleProperty>() : ExpressionParser.parseExpand(value);
	}
	
	public static String toExpandString(List<EntitySimpleProperty> expand) {
		if(!Collections.isEmpty(expand)) {
			StringBuilder builder = new StringBuilder(ODataConstants.QueryOptions.EXPAND);
			builder.append("=");
			for (EntitySimpleProperty property : expand) {
				builder.append(property.getName()).append(",");
			}
			return builder.substring(0, builder.length() - 1);
		} else return null;
	}
	
	public static List<EntitySimpleProperty> parseSelect(String value){
		return Strings.isEmpty(value) ? new ArrayList<EntitySimpleProperty>() : ExpressionParser.parseExpand(value);
	}

	public static String toSelectString(List<EntitySimpleProperty> select) {
		if(!Collections.isEmpty(select)) {
			StringBuilder builder = new StringBuilder(ODataConstants.QueryOptions.SELECT);
			builder.append("=");
			for (EntitySimpleProperty property : select) {
				builder.append(property.getName()).append(",");
			}
			return builder.substring(0, builder.length() - 1);
		} else return null;
	}
	
	public static String toParamsString(Map<String, String> paramMap) {
		if(null == paramMap || paramMap.size() == 0) return null;
		StringBuilder str = new StringBuilder();
		for (String key : paramMap.keySet()) {
			String value = paramMap.get(key);
			str.append(key + "=" + value + "&");
		}
		return str.substring(0, str.length() - 1);
	}
	
	public static String toQueryString(ODataQueryInfo queryInfo) {
		if(null == queryInfo) return null;
		Set<String> set = new HashSet<String>();
//		set.add(toFilterString(queryInfo.getFilter()));
//		set.add(toInlineCountString(queryInfo.getInlineCount()));
//		set.add(toSkipString(queryInfo.getSkip()));
//		set.add(toSelectString(queryInfo.getSelect()));
//		set.add(toExpandString(queryInfo.getExpand()));
//		set.add(toOrderByString(queryInfo.getOrderBy()));
//		set.add(toSkipTokenString(queryInfo.getSkipToken()));
//		set.add(toTopString(queryInfo.getTop()));
		set.add(toParamsString(queryInfo.getParams()));
		set.remove(null);
		return Strings.join(set, "&");
	}
}
