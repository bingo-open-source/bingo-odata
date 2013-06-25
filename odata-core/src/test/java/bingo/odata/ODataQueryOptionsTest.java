package bingo.odata;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import bingo.lang.Strings;
import bingo.odata.ODataConstants.QueryOptions;

public class ODataQueryOptionsTest {

	@Test
	public void testToQueryString() {
		Map<String, String> options = new HashMap<String, String>();
		options.put(QueryOptions.SKIP, "10");
		options.put(QueryOptions.EXPAND, "name");
		options.put(QueryOptions.FILTER, "id eq 123123");
		options.put("param1", "balabala");
		ODataQueryOptions queryOptions = new ODataQueryOptions(options);
		ODataQueryInfo info = ODataQueryInfoParser.parse(queryOptions);
		
		String queryString = ODataQueryInfoParser.toQueryString(info);
		assertEquals(1, Strings.countOccurrences(queryString, "$skip=10"));
		assertEquals(1, Strings.countOccurrences(queryString, "$filter=id eq 123123"));
		assertEquals(1, Strings.countOccurrences(queryString, "$expand=name"));
		assertEquals(1, Strings.countOccurrences(queryString, "param1=balabala"));
		assertEquals(options.size() - 1, Strings.countOccurrences(queryString, "&"));
	}

}
