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
import bingo.odata.utils.InternalDateUtils;
import bingo.odata.utils.InternalTypeUtils;
import bingo.odata.values.DateTimeOffset;
import bingo.odata.values.Guid;
import bingo.odata.values.UnsignedByte;
import bingo.utils.codec.binary.Base64;

public class ODataConverts {
	
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
        throw ODataErrors.notImplemented("'" + type.getFullQualifiedName() + "' not supported");
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
			byte[] bValue = (byte[]) value;
			return Base64.encode(bValue);
		} else if (type == EdmSimpleType.GUID) {
			return value.toString();
		} else if (type == EdmSimpleType.TIME) {
			return InternalTypeUtils.formatTime((Time) value);
		} else if (type == EdmSimpleType.DATETIME_OFFSET) {
			// Edm.DateTimeOffset '-'? yyyy '-' mm '-' dd 'T' hh ':' mm
			// ':' ss ('.' s+)? (zzzzzz)?
			return InternalTypeUtils.formatDateTimeOffset((DateTimeOffset) value);
		}
		
		throw ODataErrors.notImplemented("'" + type.getFullQualifiedName() + "' not supported");
	}
}
