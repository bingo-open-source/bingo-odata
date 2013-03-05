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
package bingo.odata;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

import bingo.odata.edm.EdmSimpleType;
import bingo.odata.edm.EdmSimpleTypeKind;
import bingo.odata.utils.InternalDateUtils;
import bingo.odata.utils.InternalTypeUtils;
import bingo.odata.values.DateTimeOffset;
import bingo.odata.values.Guid;
import bingo.odata.values.UnsignedByte;
import bingo.lang.Converts;
import bingo.lang.Strings;
import bingo.lang.codec.Base64;

public class ODataConverts {
	
	public static Object convert(EdmSimpleType type,Object value) throws ODataError {
		if(type.getValueKind().equals(EdmSimpleTypeKind.String)){
			return Converts.toString(value);
		}

		value = Strings.trimToNull(value);
		if(null == value){
			return null;
		}
		
		if(type.isMappingType(value.getClass())){
			return value;
		}
		
        if (EdmSimpleType.GUID.equals(type)) {
        	return Guid.fromString(Converts.toString(value));
        } else if (EdmSimpleType.BOOLEAN.equals(type)) {
            return Converts.toBoolean(value);
        } else if (EdmSimpleType.BYTE.equals(type)) {
            return UnsignedByte.valueOf(Converts.toInt(value));
        } else if (EdmSimpleType.SBYTE.equals(type)) {
            return Byte.valueOf(Converts.convert(value, Byte.class));
        } else if (EdmSimpleType.INT16.equals(type)) {
            return Converts.convert(value, Short.class);
        } else if (EdmSimpleType.INT32.equals(type)) {
            return Converts.convert(value, Integer.class);
        } else if (EdmSimpleType.INT64.equals(type)) {
            return Converts.convert(value, Long.class);
        } else if (EdmSimpleType.SINGLE.equals(type)) {
            return Converts.convert(value, Float.class);
        } else if (EdmSimpleType.DOUBLE.equals(type)) {
            return Converts.convert(value, Double.class);
        } else if (EdmSimpleType.DECIMAL.equals(type)) {
            return Converts.convert(value, BigDecimal.class);
        } else if (EdmSimpleType.BINARY.equals(type)) {
            return Base64.decode(Converts.toString(type));
        } else if (EdmSimpleType.DATETIME.equals(type)) {
            if(type instanceof Appendable){
            	return InternalTypeUtils.parseDateTime(((Appendable)value).toString());
            }else{
            	return Converts.convert(value, Date.class);
            }
        } else if (EdmSimpleType.TIME.equals(type)) {
            if(type instanceof Appendable){
            	return InternalTypeUtils.parseTime(((Appendable)value).toString());
            }else{
            	return Converts.convert(value, Time.class);
            }
        } else if (EdmSimpleType.BINARY.equals(type)){
        	return Converts.convert(value, byte[].class);
        }
        throw ODataErrors.notImplemented("type '" + type.getFullQualifiedName() + "' not supported");
		
		
	}
	
	public static Object fromString(EdmSimpleType type,String value) throws ODataError {
		if(null == value){
			return null;
		}
		
		if(type == EdmSimpleType.STRING){
			return value;
		}
		
		value = value.trim();
		
        if (EdmSimpleType.GUID.equals(type)) {
        	return Guid.fromString(value);
        } else if (EdmSimpleType.BOOLEAN.equals(type)) {
            return Boolean.parseBoolean(value);
        } else if (EdmSimpleType.BYTE.equals(type)) {
            return UnsignedByte.parseUnsignedByte(value);
        } else if (EdmSimpleType.SBYTE.equals(type)) {
            return Byte.parseByte(value);
        } else if (EdmSimpleType.INT16.equals(type)) {
            return Short.parseShort(value);
        } else if (EdmSimpleType.INT32.equals(type)) {
            return Integer.parseInt(value);
        } else if (EdmSimpleType.INT64.equals(type)) {
            return Long.parseLong(value);
        } else if (EdmSimpleType.SINGLE.equals(type)) {
            return Float.parseFloat(value);
        } else if (EdmSimpleType.DOUBLE.equals(type)) {
            return Double.parseDouble(value);
        } else if (EdmSimpleType.DECIMAL.equals(type)) {
            return new BigDecimal(value);
        } else if (EdmSimpleType.BINARY.equals(type)) {
            return Base64.decode(value);
        } else if (EdmSimpleType.DATETIME.equals(type)) {
        	return InternalDateUtils.parse(value);
        } else if (EdmSimpleType.DATETIME_OFFSET.equals(type)) {
            return InternalTypeUtils.parseDateTimeOffset(value);
        } else if (EdmSimpleType.TIME.equals(type)) {
            return InternalTypeUtils.parseTime(value);
        } 
        throw ODataErrors.notImplemented("type '" + type.getFullQualifiedName() + "' not supported");
	}

	/**
	 * null value return null string
	 */
	public static String toString(EdmSimpleType type, Object value) throws ODataError {
		if (null == value) {
			return null;
		}
		
		if(type == EdmSimpleType.STRING || value instanceof String){
			return value.toString();
		}
		
		if (type == EdmSimpleType.INT32) {
			return value.toString();
		} else if (type == EdmSimpleType.INT16) {
			return value.toString();
		} else if (type == EdmSimpleType.INT64) {
			return value.toString();
		} else if (type == EdmSimpleType.BOOLEAN) {
			return value.toString();
		} else if (type == EdmSimpleType.BYTE) {
			return value.toString();
		} else if (type == EdmSimpleType.SBYTE) {
			return value.toString();
		} else if (type == EdmSimpleType.DECIMAL) {
			return value.toString();
		} else if (type == EdmSimpleType.SINGLE) {
			return value.toString();
		} else if (type == EdmSimpleType.DOUBLE) {
			return value.toString();
		} else if (type == EdmSimpleType.STRING) {
			return value.toString();
		} else if (type == EdmSimpleType.DATETIME) {
			return InternalTypeUtils.formatDateTime((Date) value);
		} else if (type == EdmSimpleType.BINARY) {
			byte[] bValue = Converts.convert(value, byte[].class);
			return Base64.encode(bValue);
		} else if (type == EdmSimpleType.GUID) {
			return value.toString();
		} else if (type == EdmSimpleType.TIME) {
			Time time = Converts.convert(value, Time.class);
			return InternalTypeUtils.formatTime(time);
		} else if (type == EdmSimpleType.DATETIME_OFFSET) {
			// Edm.DateTimeOffset '-'? yyyy '-' mm '-' dd 'T' hh ':' mm
			// ':' ss ('.' s+)? (zzzzzz)?
			return InternalTypeUtils.formatDateTimeOffset((DateTimeOffset) value);
		}
		
		throw ODataErrors.notImplemented("'" + type.getFullQualifiedName() + "' not supported");
	}
}
