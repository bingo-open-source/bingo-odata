package bingo.odata.consumer.requests.builders;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ext.OrderByDirection;
import bingo.odata.consumer.ext.Page;

public interface EntityQuery {
	
	EntityQuery entity(String name);
	
	EntityQuery where(String where);
	
	EntityQuery param(Object value);
	
	EntityQuery param(String key, Object value);
	
	/**
	 * these params are added to current params, not replaced them!
	 * @param params
	 * @return
	 */
	EntityQuery params(Map<String, Object> params);
	
	/**
	 * default order direction is Asc.
	 * @param field
	 * @return
	 */
	EntityQuery orderBy(String field);
	
	EntityQuery orderBy(String field, OrderByDirection direction);
	
	EntityQuery orderBys(LinkedHashMap<String, OrderByDirection> orderBys);
	
	/**
	 * Not calling this method or calling select("*") will select all fields.
	 * @param fields
	 * @return
	 */
	EntityQuery select(String... fields);
	
	EntityQuery expand(String... expands);
	
	EntityQuery page(Page page);
	
	/**
	 * default page size is 10.
	 * @param page
	 * @return
	 */
	EntityQuery page(int page);
	
	EntityQuery page(int page, int pageSize);
	
	List<Map<String, Object>> list();
	
	Map<String, Object> single();
	
	Map<String, Object> singleOrNull();
	
	long count();
}
