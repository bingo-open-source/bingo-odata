package bingo.odata.consumer.util;

import java.util.LinkedHashMap;
import java.util.Map;

import bingo.lang.Strings;
import bingo.odata.ODataConstants;

public class ODataQueryTranslator {
	
	private static Map<String, String> operationMapping;
	
	static {
		operationMapping = new LinkedHashMap<String, String>();

		// take care of the order.
		operationMapping.put("!=", " ne ");
		operationMapping.put(">=", " ge ");
		operationMapping.put("<=", " le ");
		operationMapping.put("=", " eq ");
		operationMapping.put(">", " gt ");
		operationMapping.put("<", " lt ");
	}
	
	public static String translateFilter(String originalString) {
		return translateFilter(originalString, true);
	}
	
	public static String translateFilter(String originalString, Map<String, Object> params) {
		return translateFilter(originalString, params, true);
	}
	
	public static String translateFilter(String originalString, boolean with$Filter) {
		return translateFilter(originalString, null, with$Filter);
	}

	public static String translateFilter(String originalString, Map<String, Object> params, boolean with$Filter) {
		String resolvedString = null;
		
		resolvedString = translateOperation(originalString);
		
		resolvedString = replaceHolder(resolvedString, params);
		
		if(!with$Filter) return resolvedString;
		
		return ODataConstants.QueryOptions.FILTER + "=" + resolvedString;
	}
	
	private static String replaceHolder(String resolvedString, Map<String, Object> params) {
		if(null != params) {
			for (String key : params.keySet()) {
				resolvedString = Strings.replace(resolvedString, ":" + key, params.get(key).toString());
			}
		}
		return resolvedString;
	}

	private static String translateOperation(String origin) {
		for (String key : operationMapping.keySet()) {
			origin = Strings.replace(origin, key, operationMapping.get(key));
		}
		return origin;
	}
	
}
