package bingo.odata.consumer.requests.builders;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ext.OrderByDirection;
import bingo.odata.consumer.ext.Page;

public interface QueryBuilder {
	
	QueryBuilder entitySet(String entitySet);
	
	QueryBuilder where(String where);
	
	QueryBuilder param(String key, Object value);
	
	/**
	 * these params are added to current params, not replaced them!
	 * @param params
	 * @return
	 */
	QueryBuilder params(Map<String, Object> params);
	
	/**
	 * default order direction is Asc.
	 * @param field
	 * @return
	 */
	QueryBuilder orderBy(String field);
	
	QueryBuilder orderBy(String field, OrderByDirection direction);
	
	QueryBuilder orderBys(LinkedHashMap<String, OrderByDirection> orderBys);
	
	/**
	 * Not calling this method or calling select("*") will select all fields.
	 * @param fields
	 * @return
	 */
	QueryBuilder select(String... fields);
	
	QueryBuilder expand(String... expands);
	
	QueryBuilder page(Page page);
	
	/**
	 * default page size is 10.
	 * @param page
	 * @return
	 */
	QueryBuilder page(int page);
	
	QueryBuilder page(int page, int pageSize);
	
	List<Map<String, Object>> exec();
}
