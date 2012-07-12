/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bingo.odata.utils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bingo.lang.Dates;

public class InternalDateUtils {

    // Since not everybody seems to adhere to the spec, we are trying to be
    // tolerant against different formats
    // spec says:
    // Edm.DateTime: yyyy-mm-ddThh:mm[:ss[.fffffff]]
    // Edm.DateTimeOffset: yyyy-mm-ddThh:mm[:ss[.fffffff]](('+'|'-')hh':'mm)|'Z'
    private static final Pattern DATETIME_PATTERN = 
    	Pattern.compile("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})(:\\d{2})?(\\.\\d{1,7})?((?:(?:\\+|\\-)\\d{2}:\\d{2})|Z)?");
	
    private static final String[] DATETIME_FORMATS = 
    	new String[] {
	        // formatter for parsing of dateTime and dateTimeOffset
	        "yyyy-MM-dd'T'HH:mm",
	        "yyyy-MM-dd'T'HH:mm:ss",
	        null,
	        "yyyy-MM-dd'T'HH:mm:ss.SSS",
	        "yyyy-MM-dd'T'HH:mmZZ",
	        "yyyy-MM-dd'T'HH:mm:ssZZ",
	        null,
	        "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"
	     };	

    public static Date parse(String value){
        Matcher matcher = DATETIME_PATTERN.matcher(value);

        if (matcher.matches()) {
            String dateTime    = matcher.group(1); //yyyy-mm-ddThh:mm
            String seconds 	   = matcher.group(2); //:ss
            String nanoSeconds = matcher.group(3); //.fffffff
            String timezone	   = matcher.group(4); //ZZ

            int formatIndex = (seconds != null ? 1 : 0) + (nanoSeconds != null ? 2 : 0) + (timezone != null ? 4 : 0);

            StringBuilder valueToParse = new StringBuilder(dateTime);
            
            if (seconds != null){
            	valueToParse.append(seconds);
            }

            // we know only about milliseconds not nanoseconds
            if (nanoSeconds != null) {
                if (nanoSeconds.length() > 4) {
                    valueToParse.append(nanoSeconds.substring(0, Math.min(nanoSeconds.length(), 4)));
                } else {
                    valueToParse.append(nanoSeconds);
                }
            }

            if (timezone != null) {
                if ("Z".equals(timezone)) {
                    timezone = "+00:00";
                }
                valueToParse.append(timezone);
            }
            
            String format = DATETIME_FORMATS[formatIndex];
            return Dates.parse(valueToParse.toString(),format);
        }
        
        throw new IllegalArgumentException("Illegal datetime format " + value);
    }
}
