package bingo.odata.consumer.ext;

import java.util.Calendar;
import java.util.Date;

import bingo.lang.Converts;
import bingo.lang.Strings;
import bingo.lang.convert.AbstractDateConverter;
import bingo.lang.convert.Converter;
import bingo.lang.time.DateFormats;
import bingo.odata.format.json.JsonWriterUtils;

public class ODataDateConvertor extends AbstractDateConverter<Date> implements Converter<Date> {

	static final String	DATETIME_JSON_PREFIX = "\\/Date(";
	static final String	DATETIME_JSON_SUFFIX = ")\\/";
	public ODataDateConvertor() {
	    this.patterns = new String[]{DATETIME_JSON_PREFIX};
    }

	@Override
    protected Date toDate(Class<?> targetType, Date date) {
	    return date;
    }

	@Override
    protected Date toDate(Class<?> targetType, Calendar calendar) {
	    return new Date(calendar.getTimeInMillis());
    }

	@Override
    protected Date toDate(Class<?> targetType, Long time) {
	    return new Date(time);
    }

	@Override
	protected Date toDate(Class<?> targetType, String string) {
		if(string.startsWith(DATETIME_JSON_PREFIX)){
			String longValue = string.substring(DATETIME_JSON_PREFIX.length(),string.length() - DATETIME_JSON_SUFFIX.length());
			return new Date(Long.parseLong(longValue));
		} else {
			return Converts.convert(string, Date.class);
		}
	}
	
}
