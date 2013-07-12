package bingo.odata.consumer.util;

public class ODataUrlFormatter {

	public static String formatValue(Object obj) {
		if(obj instanceof String) {
			return "'" + obj.toString() + "'";
		}
		if(obj instanceof Integer || obj instanceof Float || obj instanceof Double) {
			return obj.toString();
		}
		return obj.toString();
	}
}
