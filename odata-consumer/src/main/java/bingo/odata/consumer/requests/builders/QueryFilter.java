package bingo.odata.consumer.requests.builders;

public interface QueryFilter {
	/**
	 * Equal
	 * @param name
	 * @param value
	 * @return
	 */
	QueryFilter eq(String name, Object value);
	
	/**
	 * Not equal
	 * @param name
	 * @param value
	 * @return
	 */
	QueryFilter ne(String name, Object value);
	
	/**
	 * Greater than
	 * @param name
	 * @param value
	 * @return
	 */
	QueryFilter gt(String name, Object value);
	
	/**
	 * @param name
	 * @param value
	 * @return
	 */
	QueryFilter ge(String name, Object value);
	
	/**
	 * @param name
	 * @param value
	 * @return
	 */
	QueryFilter lt(String name, Object value);
	
	/**
	 * @param name
	 * @param value
	 * @return
	 */
	QueryFilter and(QueryFilter filter1, QueryFilter filter2);
	
	/**
	 * @param name
	 * @param value
	 * @return
	 */
	QueryFilter or(QueryFilter filter1, QueryFilter filter2);

	/**
	 * @param name
	 * @param value
	 * @return
	 */
	QueryFilter not(QueryFilter filter);
	
}
