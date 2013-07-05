package bingo.odata.consumer.util;

import java.util.List;
import java.util.Map;

import bingo.lang.Strings;

public class CommonUtil {

	/**
	 * null safe.
	 * @param map
	 * @param key
	 * @return
	 */
	public static Object tryDownstair(Map<String, Object> map, String key) {
		if(null != map && Strings.isNotBlank(key)) {
			Object valueObject = map.get(key);
			if(null != valueObject) {
				 return valueObject;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> tryDownstairForMap(Map<String, Object> map, String key) {
		return (Map<String, Object>) tryDownstair(map, key);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> tryDownstairForList(Map<String, Object> map, String key) {
		return (List<Map<String, Object>>) tryDownstair(map, key);
	}
	
}
