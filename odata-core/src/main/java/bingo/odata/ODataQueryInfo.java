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

import java.util.List;
import java.util.Map;

import bingo.lang.Collections;
import bingo.lang.Immutables;
import bingo.odata.expression.BoolExpression;
import bingo.odata.expression.EntitySimpleProperty;
import bingo.odata.expression.OrderByExpression;

public class ODataQueryInfo {
	
	private ODataInlineCount inlineCount;

	private Integer top;
	
	private Integer skip;
	
	private String skipToken;
	
	private BoolExpression filter;
	
	private List<OrderByExpression> orderBy;
	
	private List<EntitySimpleProperty> expand;
	
	private List<EntitySimpleProperty> select;
	
	private Map<String, String> params;
	
	public ODataQueryInfo(List<EntitySimpleProperty> expand,BoolExpression filter,List<OrderByExpression> orderBy,Integer skip, Integer top,String skipToken,ODataInlineCount inlineCount,List<EntitySimpleProperty> select,Map<String, String> params){
		this.expand      = expand;
		this.filter      = filter;
		this.orderBy     = orderBy;
		this.top         = top;
		this.skip        = skip;
		this.skipToken   = skipToken;
		this.inlineCount = inlineCount;
		this.select      = select;
		if(null != params) this.params      = Immutables.mapOf(params);
	}
	
	public BoolExpression getFilter() {
    	return filter;
    }

	public List<OrderByExpression> getOrderBy() {
    	return orderBy;
    }
	
	public List<EntitySimpleProperty> getExpand() {
    	return expand;
    }

	public List<EntitySimpleProperty> getSelect() {
    	return select;
    }

	public ODataInlineCount getInlineCount() {
    	return inlineCount;
    }
	
	public boolean isAllPagesInlineCount(){
		return null != inlineCount && inlineCount.equals(ODataInlineCount.AllPages);
	}

	public Integer getTop() {
    	return top;
    }

	public Integer getSkip() {
    	return skip;
    }

	public String getSkipToken() {
    	return skipToken;
    }

	public Map<String, String> getParams() {
    	return params;
    }
}
