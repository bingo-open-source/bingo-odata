/*
 * Copyright 2013 the original author or authors.
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
package bingo.odata.format.json;

import java.sql.Date;

import bingo.meta.edm.EdmSimpleType;

public class JsonReaderUtils {

	public static Object readValue(EdmSimpleType type,Object value){
		if(null == value){
			return null;
		}
		
		Class<?> clazz = value.getClass();
		
		if(EdmSimpleType.DATETIME.equals(type)){
			if(Long.class.equals(clazz) || Long.TYPE.equals(clazz)){
				return new Date((Long)value);
			}
			
			if(String.class.equals(clazz)){
				String string = (String)value;
				
				if(string.startsWith(JsonWriterUtils.DATETIME_JSON_PREFIX)){
					String longValue = string.substring(JsonWriterUtils.DATETIME_JSON_PREFIX.length(),string.length() - JsonWriterUtils.DATETIME_JSON_SUFFIX.length());
					return new Date(Long.parseLong(longValue));
				}
				
			}
		}
		
		return value;
	}
	
}
