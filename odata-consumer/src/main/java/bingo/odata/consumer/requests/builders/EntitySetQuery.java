package bingo.odata.consumer.requests.builders;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ext.OrderByDirection;
import bingo.odata.consumer.ext.Page;

public interface EntitySetQuery {
	
	EntitySetQuery entitySet(String entitySet);
	
	EntitySetQuery where(String where);
	
	EntitySetQuery param(String key, Object value);
	
	/**
	 * these params are added to current params, not replaced them!
	 * @param params
	 * @return
	 */
	EntitySetQuery params(Map<String, Object> params);
	
	/**
	 * default order direction is Asc.
	 * @param field
	 * @return
	 */
	EntitySetQuery orderBy(String field);
	
	EntitySetQuery orderBy(String field, OrderByDirection direction);
	
	EntitySetQuery orderBys(LinkedHashMap<String, OrderByDirection> orderBys);
	
	/**
	 * Not calling this method or calling select("*") will select all fields.
	 * @param fields
	 * @return
	 */
	EntitySetQuery select(String... fields);
	
	EntitySetQuery expand(String... expands);
	
	EntitySetQuery page(Page page);
	
	/**
	 * default page size is 10.
	 * @param page
	 * @return
	 */
	EntitySetQuery page(int page);
	
	EntitySetQuery page(int page, int pageSize);
	
	List<Map<String, Object>> execute();
}
