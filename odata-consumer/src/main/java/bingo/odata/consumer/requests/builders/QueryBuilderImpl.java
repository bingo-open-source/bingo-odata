package bingo.odata.consumer.requests.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bingo.lang.Collections;
import bingo.lang.New;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ext.OrderByDirection;
import bingo.odata.consumer.ext.Page;

public class QueryBuilderImpl implements QueryBuilder {
	
	private ODataConsumer consumer;
	private String entitySet;
	private String where;
	private Map<String, Object> params = new HashMap<String, Object>();
	private LinkedHashMap<String, OrderByDirection> orderBys = new LinkedHashMap<String, OrderByDirection>();
	private List<String> selects = new ArrayList<String>();
	private List<String> expands = new ArrayList<String>();
	private Page page;
	
	public QueryBuilderImpl(ODataConsumer consumer) {
		this.consumer = consumer;
	}
	
	public QueryBuilder entitySet(String entitySet) {
		this.entitySet = entitySet;
		return this;
	}

	public QueryBuilder where(String where) {
		this.where = where;
		return this;
	}

	public QueryBuilder param(String key, Object value) {
		params.put(key, value);
		return this;
	}

	public QueryBuilder params(Map<String, Object> params) {
		params.putAll(params);
		return this;
	}

	public QueryBuilder orderBy(String field) {
		this.orderBys.put(field, OrderByDirection.asc);
		return this;
	}

	public QueryBuilder orderBy(String field, OrderByDirection direction) {
		this.orderBys.put(field, direction);
		return this;
	}

	public QueryBuilder orderBys(LinkedHashMap<String, OrderByDirection> orderBys) {
		this.orderBys.putAll(orderBys);
		return this;
	}

	public QueryBuilder select(String... fields) {
		this.selects.addAll(Collections.listOf(fields));
		return this;
	}

	public QueryBuilder expand(String... expands) {
		this.expands.addAll(Collections.listOf(expands));
		return this;
	}

	public QueryBuilder page(Page page) {
		this.page = page;
		return this;
	}

	public QueryBuilder page(int page) {
		this.page = new Page(page);
		return this;
	}

	public QueryBuilder page(int page, int pageSize) {
		this.page = new Page(page, pageSize);
		return this;
	}

	public List<Map<String, Object>> exec() {
		StringBuilder orderByBuilder = new StringBuilder();
		if(orderBys.size() > 0) {
			for (String field : orderBys.keySet()) {
				orderByBuilder.append(field).append(" ").append(orderBys.get(field)).append(",");
			}
		}
		return consumer.findEntitySet(entitySet, where, params, 
				orderByBuilder.substring(0, orderByBuilder.length() - 1), 
				Collections.toStringArray(selects), Collections.toStringArray(expands), page);
	}

}
