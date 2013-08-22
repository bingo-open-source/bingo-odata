package bingo.odata.consumer.requests.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bingo.lang.Assert;
import bingo.lang.Collections;
import bingo.lang.New;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerConfigImpl;
import bingo.odata.consumer.ODataConsumerImpl;
import bingo.odata.consumer.exceptions.UnexpectedResultException;
import bingo.odata.consumer.ext.OrderByDirection;
import bingo.odata.consumer.ext.Page;

public class EntityQueryImpl implements EntityQuery {
	
	private ODataConsumerImpl consumer;
	private String entityName;
	private String where;
	private Map<String, Object> params = new HashMap<String, Object>();
	private Object param;
	private LinkedHashMap<String, OrderByDirection> orderBys = new LinkedHashMap<String, OrderByDirection>();
	private List<String> selects = new ArrayList<String>();
	private List<String> expands = new ArrayList<String>();
	private Page page;
	
	private boolean useSingleParam = false;
	
	public EntityQueryImpl(ODataConsumerImpl consumer) {
		this.consumer = consumer;
	}
	
	public EntityQuery entity(String name) {
		this.entityName = name;
		return this;
	}

	public EntityQuery where(String where) {
		this.where = where;
		return this;
	}

	public EntityQuery param(Object value) {
		useSingleParam = true;
		this.param = value;
		return this;
	}
	
	public EntityQuery param(String key, Object value) {
		params.put(key, value);
		return this;
	}

	public EntityQuery params(Map<String, Object> params) {
		params.putAll(params);
		return this;
	}

	public EntityQuery orderBy(String field) {
		this.orderBys.put(field, OrderByDirection.asc);
		return this;
	}

	public EntityQuery orderBy(String field, OrderByDirection direction) {
		this.orderBys.put(field, direction);
		return this;
	}

	public EntityQuery orderBys(LinkedHashMap<String, OrderByDirection> orderBys) {
		this.orderBys.putAll(orderBys);
		return this;
	}

	public EntityQuery select(String... fields) {
		this.selects.addAll(Collections.listOf(fields));
		return this;
	}

	public EntityQuery expand(String... expands) {
		this.expands.addAll(Collections.listOf(expands));
		return this;
	}

	public EntityQuery page(Page page) {
		this.page = page;
		return this;
	}

	public EntityQuery page(int page) {
		this.page = new Page(page);
		return this;
	}

	public EntityQuery page(int page, int pageSize) {
		this.page = new Page(page, pageSize);
		return this;
	}

	public List<Map<String, Object>> list() {
		StringBuilder orderByBuilder = new StringBuilder();
		String orderBy = null;
		if(orderBys.size() > 0) {
			for (String field : orderBys.keySet()) {
				orderByBuilder.append(field).append(" ").append(orderBys.get(field)).append(",");
			}
			orderBy = orderByBuilder.substring(0, orderByBuilder.length() - 1);
		}
		return consumer.findEntitySet(getEntitySetName(entityName), where,
				useSingleParam? param : params, orderBy, 
				Collections.toStringArray(selects), Collections.toStringArray(expands), page);
	}
	
	public long count() {
		return consumer.count(getEntitySetName(entityName), where, 
				useSingleParam? param : params, page);
	}

	public Map<String, Object> single() {
		List<Map<String, Object>> list = list();
		if(!Collections.isEmpty(list) && list.size() == 1) return list.get(0);
		throw new UnexpectedResultException("result is single", "result is not single");
	}

	public Map<String, Object> singleOrNull() {
		List<Map<String, Object>> list = list();
		if(Collections.isEmpty(list)) return null;
		if(list.size() == 1) return list.get(0);
		throw new UnexpectedResultException("result is single or null", "result is more than one");
	}

	private String getEntitySetName(String entity) {
		EdmEntityType entityType = consumer.getServiceMetadata().findEntityType(entity);
		Assert.notNull(entityType);
		EdmEntitySet entitySet = consumer.getServiceMetadata().findEntitySet(entityType);
		Assert.notNull(entitySet);
		return entitySet.getName();
	}
}
