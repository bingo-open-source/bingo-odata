package bingo.odata.consumer.test;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestResource {

	public static final Map<String, String> serviceUrls = new LinkedHashMap<String, String>();

	static {
		serviceUrls.put("local", "http://localhost:8080/demo");
//		serviceUrls.put("remote1", "");
//		serviceUrls.put("remote2", "");
	}
}
