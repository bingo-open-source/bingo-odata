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

public class QueryBuilderImpl implements EntitySetQuery {
	
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
	
	public EntitySetQuery entitySet(String entitySet) {
		this.entitySet = entitySet;
		return this;
	}

	public EntitySetQuery where(String where) {
		this.where = where;
		return this;
	}

	public EntitySetQuery param(String key, Object value) {
		params.put(key, value);
		return this;
	}

	public EntitySetQuery params(Map<String, Object> params) {
		params.putAll(params);
		return this;
	}

	public EntitySetQuery orderBy(String field) {
		this.orderBys.put(field, OrderByDirection.asc);
		return this;
	}

	public EntitySetQuery orderBy(String field, OrderByDirection direction) {
		this.orderBys.put(field, direction);
		return this;
	}

	public EntitySetQuery orderBys(LinkedHashMap<String, OrderByDirection> orderBys) {
		this.orderBys.putAll(orderBys);
		return this;
	}

	public EntitySetQuery select(String... fields) {
		this.selects.addAll(Collections.listOf(fields));
		return this;
	}

	public EntitySetQuery expand(String... expands) {
		this.expands.addAll(Collections.listOf(expands));
		return this;
	}

	public EntitySetQuery page(Page page) {
		this.page = page;
		return this;
	}

	public EntitySetQuery page(int page) {
		this.page = new Page(page);
		return this;
	}

	public EntitySetQuery page(int page, int pageSize) {
		this.page = new Page(page, pageSize);
		return this;
	}

	public List<Map<String, Object>> execute() {
		StringBuilder orderByBuilder = new StringBuilder();
		String orderBy = null;
		if(orderBys.size() > 0) {
			for (String field : orderBys.keySet()) {
				orderByBuilder.append(field).append(" ").append(orderBys.get(field)).append(",");
			}
			orderBy = orderByBuilder.substring(0, orderByBuilder.length() - 1);
		}
		return consumer.findEntitySet(entitySet, where, params, orderBy, 
				Collections.toStringArray(selects), Collections.toStringArray(expands), page);
	}

}
