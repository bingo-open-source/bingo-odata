package bingo.odata.consumer.requests;

import java.util.List;
import java.util.Map;

import bingo.odata.consumer.ODataConsumer;

public interface QueryBuilder {

	QueryBuilder skip(int skip);
	
	QueryBuilder top(int top);
	
	QueryBuilder filter(QueryFilter filter);
	
	/**
	 * Not calling this method or calling select("*") will select all fields.
	 * @param fields
	 * @return
	 */
	QueryBuilder select(String... fields);
	
	/**
	 * e.g. 
	 * queryBuilder.orderBy("name asc"); // normal usage
	 * queryBuilder.orderBy("name"); // use 'asc' while omitting order direction.
	 * queryBuilder.orderBy("name", "sex desc"); // multi order fields 
	 *  
	 * @param orderByFields
	 * @return
	 */
	QueryBuilder orderBy(String... orderByFields);
	
	List<Map<String, Object>> listMap();
}
