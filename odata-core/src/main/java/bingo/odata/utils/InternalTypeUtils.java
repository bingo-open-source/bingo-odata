/**
 * Copyright 2005-2012 Restlet S.A.S.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL
 * 1.0 (the "Licenses"). You can select the license that you prefer but you may
 * not use this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.restlet.com/products/restlet-framework
 * 
 * Restlet is a registered trademark of Restlet S.A.S.
 */

package bingo.odata.utils;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import bingo.lang.Dates;
import bingo.lang.codec.Base64;
import bingo.lang.exceptions.NotImplementedException;
import bingo.meta.edm.EdmSimpleType;
import bingo.odata.ODataErrors;
import bingo.odata.ODataException;
import bingo.odata.values.DateTimeOffset;

/**
 * Handle type operations.
 * 
 * @author Thierry Boileau
 */
public class InternalTypeUtils {

	/** Formater for the EDM DateTime type. */
	public static final String[]	 dateTimeFormats	= 
		new String[] { "yyyy-MM-dd'T'HH:mm:ssz", "yyyy-MM-dd'T'HH:mm:ss", "EEE, dd MMM yyyy HH:mm:ss zzz"	      };

	/** Formater for the EDM Decimal type. */
	public static final NumberFormat	decimalFormat	= DecimalFormat.getNumberInstance(Locale.US);

	/** Formater for the EDM Double type. */
	public static final NumberFormat	doubleFormat	= DecimalFormat.getNumberInstance(Locale.US);

	/** Formater for the EDM Single type. */
	public static final NumberFormat	singleFormat	= DecimalFormat.getNumberInstance(Locale.US);

	/** Formater for the EDM Time type. */
	public static final NumberFormat	timeFormat	  = DecimalFormat.getIntegerInstance(Locale.US);

	public static Date parseDateTime(String value){
		return Dates.parse(value, dateTimeFormats);
	}
	
	public static DateTimeOffset parseDateTimeOffset(String value){
		throw new NotImplementedException();
	}
	
	public static Time parseTime(String value){
		try {
	        return new Time(timeFormat.parse(value).longValue());
        } catch (ParseException e) {
        	throw new ODataException("cannot parse time value '{0}' : {1}",value,e.getMessage(),e);
        }
	}
	
	public static String formatDateTime(Date dt){
		return Dates.format(dt,dateTimeFormats[1]);
	}
	
	public static String formatDateTimeOffset(DateTimeOffset dt){
		throw ODataErrors.notImplemented();
	}
	
	public static String formatTime(Time t){
		throw ODataErrors.notImplemented();
	}
    
    /**
     * Converts the String representation of the target WCF type to its
     * corresponding value.
     * 
     * @param value
     *            The value to convert.
     * @param adoNetType
     *            The target WCF type.
     * @return The converted value.
     */
    public static Object fromEdm(String value, String adoNetType) {
        if (value == null) {
            return null;
        }

        Object result = null;
        try {
            if (adoNetType.endsWith("Binary")) {
                result = Base64.decode(value);
            } else if (adoNetType.endsWith("Boolean")) {
                result = Boolean.valueOf(value);
            } else if (adoNetType.endsWith("DateTime")) {
                result = Dates.parse(value, dateTimeFormats);
            } else if (adoNetType.endsWith("DateTimeOffset")) {
                result = Dates.parse(value, dateTimeFormats);
            } else if (adoNetType.endsWith("Time")) {
                result = timeFormat.parseObject(value);
            } else if (adoNetType.endsWith("Decimal")) {
                result = decimalFormat.parseObject(value);
            } else if (adoNetType.endsWith("Single")) {
                result = singleFormat.parseObject(value);
            } else if (adoNetType.endsWith("Double")) {
                result = doubleFormat.parseObject(value);
            } else if (adoNetType.endsWith("Guid")) {
                result = value;
            } else if (adoNetType.endsWith("Int16")) {
                result = Short.valueOf(value);
            } else if (adoNetType.endsWith("Int32")) {
                result = Integer.valueOf(value);
            } else if (adoNetType.endsWith("Int64")) {
                result = Long.valueOf(value);
            } else if (adoNetType.endsWith("Byte")) {
                result = Byte.valueOf(value);
            } else if (adoNetType.endsWith("String")) {
                result = value;
            }
        } catch (Exception e) {
        	throw new ODataException("Cannot convert " + value + " from this EDM type " + adoNetType,e);
        }

        return result;
    }

    /**
     * Returns the literal form of the given value.
     * 
     * @param value
     *            The value to convert.
     * @param adoNetType
     *            The type of the value.
     * @return The literal form of the given value.
     * @see <a
     *      href="http://www.odata.org/docs/%5BMC-APDSU%5D.htm#z61934eae311a4af4b8f882c112248651">Abstract
     *      Type System</a>
     */
    public static String getLiteralForm(String value, String adoNetType) {
        if (value == null) {
            return null;
        }

        String result = null;
        
        if (adoNetType.endsWith("Binary")) {
            result = "'" + value + "'";
        } else if (adoNetType.endsWith("DateTime")) {
            result = "datetime'" + value + "'";
        } else if (adoNetType.endsWith("DateTimeOffset")) {
            result = "datetimeoffset'" + value + "'";
        } else if (adoNetType.endsWith("Time")) {
            result = "time'" + value + "'";
        } else if (adoNetType.endsWith("Guid")) {
            result = "guid'" + value + "'";
        } else if (adoNetType.endsWith("String")) {
            result = "'" + value + "'";
        }

        return result;
    }

    public static String toEdmString(Object value, EdmSimpleType type) {
        String adoNetType = type.getName();
        
        if (value == null && adoNetType == null) {
            return null;
        }

        String result = null;
        if (adoNetType.endsWith("Binary")) {
            if ((byte[].class).isAssignableFrom(value.getClass())) {
                result = toEdmBinary((byte[]) value);
            }
        } else if (adoNetType.endsWith("Boolean")) {
            if ((Boolean.class).isAssignableFrom(value.getClass())) {
                result = toEdmBoolean((Boolean) value);
            }
        } else if (adoNetType.endsWith("DateTime")) {
            if ((Date.class).isAssignableFrom(value.getClass())) {
                result = toEdmDateTime((Date) value);
            }
        } else if (adoNetType.endsWith("DateTimeOffset")) {
            if ((Date.class).isAssignableFrom(value.getClass())) {
                result = toEdmDateTime((Date) value);
            }
        } else if (adoNetType.endsWith("Time")) {
            if ((Long.class).isAssignableFrom(value.getClass())) {
                result = toEdmTime((Long) value);
            }
        } else if (adoNetType.endsWith("Decimal")) {
            if ((Double.class).isAssignableFrom(value.getClass())) {
                result = toEdmDecimal((Double) value);
            }
        } else if (adoNetType.endsWith("Single")) {
            if ((Float.class).isAssignableFrom(value.getClass())) {
                result = toEdmSingle((Float) value);
            } else if ((Double.class).isAssignableFrom(value.getClass())) {
                result = toEdmSingle((Double) value);
            }
        } else if (adoNetType.endsWith("Double")) {
            if ((Double.class).isAssignableFrom(value.getClass())) {
                result = toEdmDouble((Double) value);
            }
        } else if (adoNetType.endsWith("Guid")) {
            result = value.toString();
        } else if (adoNetType.endsWith("Int16")) {
            if ((Short.class).isAssignableFrom(value.getClass())) {
                result = toEdmInt16((Short) value);
            }
        } else if (adoNetType.endsWith("Int32")) {
            if ((Integer.class).isAssignableFrom(value.getClass())) {
                result = toEdmInt32((Integer) value);
            }
        } else if (adoNetType.endsWith("Int64")) {
            if ((Long.class).isAssignableFrom(value.getClass())) {
                result = toEdmInt64((Long) value);
            }
        } else if (adoNetType.endsWith("Byte")) {
            if ((Byte.class).isAssignableFrom(value.getClass())) {
                result = toEdmByte((Byte) value);
            }
        } else if (adoNetType.endsWith("String")) {
            result = value.toString();
        }

        if (result == null) {
            result = value.toString();
        }

        return result;
    }

    public static String toEdmBinary(byte[] value) {
        return Base64.encode(value, false);
    }

    public static String toEdmBoolean(boolean value) {
        return Boolean.toString(value);
    }

    public static String toEdmByte(byte value) {
        return Byte.toString(value);
    }

    public static String toEdmDateTime(Date value) {
        return Dates.format(value, dateTimeFormats[0]);
    }

    /**
     * Convert the given value to the String representation of a EDM Decimal
     * value.
     * 
     * @param value
     *            The value to convert.
     * @return The value converted as String object.
     */
    public static String toEdmDecimal(double value) {
        return decimalFormat.format(value);
    }

    /**
     * Convert the given value to the String representation of a EDM Double
     * value.
     * 
     * @param value
     *            The value to convert.
     * @return The value converted as String object.
     */
    public static String toEdmDouble(double value) {
        return doubleFormat.format(value);
    }

    /**
     * Convert the given value to the String representation of a EDM Int16
     * value.
     * 
     * @param value
     *            The value to convert.
     * @return The value converted as String object.
     */
    public static String toEdmInt16(short value) {
        return Short.toString(value);
    }

    /**
     * Convert the given value to the String representation of a EDM Int32
     * value.
     * 
     * @param value
     *            The value to convert.
     * @return The value converted as String object.
     */
    public static String toEdmInt32(int value) {
        return Integer.toString(value);
    }

    /**
     * Convert the given value to the String representation of a EDM Int64
     * value.
     * 
     * @param value
     *            The value to convert.
     * @return The value converted as String object.
     */
    public static String toEdmInt64(long value) {
        return Long.toString(value);
    }

    /**
     * Converts a value to the String representation of the target WCF type when
     * used a key in the URIs.
     * 
     * @param value
     *            The value to convert.
     * @param type
     *            The target WCF type.
     * @return The converted value.
     */
    public static String toEdmKey(Object value, EdmSimpleType type) {
        String adoNetType = type.getName();
        if (value == null && adoNetType == null) {
            return null;
        }

        String result = null;
        if (adoNetType.endsWith("Binary")) {
            if ((byte[].class).isAssignableFrom(value.getClass())) {
                result = toEdmBinary((byte[]) value);
            }
        } else if (adoNetType.endsWith("Boolean")) {
            if ((Boolean.class).isAssignableFrom(value.getClass())) {
                result = toEdmBoolean((Boolean) value);
            }
        } else if (adoNetType.endsWith("DateTime")) {
            if ((Date.class).isAssignableFrom(value.getClass())) {
                result = toEdmDateTime((Date) value);
            }
        } else if (adoNetType.endsWith("DateTimeOffset")) {
            if ((Date.class).isAssignableFrom(value.getClass())) {
                result = toEdmDateTime((Date) value);
            }
        } else if (adoNetType.endsWith("Time")) {
            if ((Long.class).isAssignableFrom(value.getClass())) {
                result = toEdmTime((Long) value);
            }
        } else if (adoNetType.endsWith("Decimal")) {
            if ((Double.class).isAssignableFrom(value.getClass())) {
                result = toEdmDecimal((Double) value);
            }
        } else if (adoNetType.endsWith("Single")) {
            if ((Float.class).isAssignableFrom(value.getClass())) {
                result = toEdmSingle((Float) value);
            } else if ((Double.class).isAssignableFrom(value.getClass())) {
                result = toEdmSingle((Double) value);
            }
        } else if (adoNetType.endsWith("Double")) {
            if ((Double.class).isAssignableFrom(value.getClass())) {
                result = toEdmDouble((Double) value);
            }
        } else if (adoNetType.endsWith("Guid")) {
            result = value.toString();
        } else if (adoNetType.endsWith("Int16")) {
            if ((Short.class).isAssignableFrom(value.getClass())) {
                result = toEdmInt16((Short) value);
            }
        } else if (adoNetType.endsWith("Int32")) {
            if ((Integer.class).isAssignableFrom(value.getClass())) {
                result = toEdmInt32((Integer) value);
            }
        } else if (adoNetType.endsWith("Int64")) {
            if ((Long.class).isAssignableFrom(value.getClass())) {
                result = toEdmInt64((Long) value);
            }
        } else if (adoNetType.endsWith("Byte")) {
            if ((Byte.class).isAssignableFrom(value.getClass())) {
                result = toEdmByte((Byte) value);
            }
        } else if (adoNetType.endsWith("String")) {
            result = "'" + value.toString() + "'";
        }

        if (result == null) {
            result = value.toString();
        }

        return result;
    }

    /**
     * Convert the given value to the String representation of a EDM Single
     * value.
     * 
     * @param value
     *            The value to convert.
     * @return The value converted as String object.
     */
    public static String toEdmSingle(double value) {
        return singleFormat.format(value);
    }

    /**
     * Convert the given value to the String representation of a EDM Single
     * value.
     * 
     * @param value
     *            The value to convert.
     * @return The value converted as String object.
     */
    public static String toEdmSingle(float value) {
        return singleFormat.format(value);
    }

    /**
     * Convert the given value to the String representation of a EDM Time value.
     * 
     * @param value
     *            The value to convert.
     * @return The value converted as String object.
     */
    public static String toEdmTime(long value) {
        return timeFormat.format(value);
    }

    /**
     * Returns the corresponding Java class or scalar type.
     * 
     * @param edmTypeName
     *            The type name.
     * @return The corresponding Java class or scalar type.
     */
    public static Class<?> toJavaClass(String edmTypeName) {
        Class<?> result = Object.class;
        if (edmTypeName.endsWith("Binary")) {
            result = byte[].class;
        } else if (edmTypeName.endsWith("Boolean")) {
            result = Boolean.class;
        } else if (edmTypeName.endsWith("DateTime")) {
            result = Date.class;
        } else if (edmTypeName.endsWith("DateTimeOffset")) {
            result = Date.class;
        } else if (edmTypeName.endsWith("Time")) {
            result = Long.class;
        } else if (edmTypeName.endsWith("Decimal")) {
            result = Double.class;
        } else if (edmTypeName.endsWith("Single")) {
            result = Double.class;
        } else if (edmTypeName.endsWith("Double")) {
            result = Double.class;
        } else if (edmTypeName.endsWith("Guid")) {
            result = String.class;
        } else if (edmTypeName.endsWith("Int16")) {
            result = Short.class;
        } else if (edmTypeName.endsWith("Int32")) {
            result = Integer.class;
        } else if (edmTypeName.endsWith("Int64")) {
            result = Long.class;
        } else if (edmTypeName.endsWith("Byte")) {
            result = Byte.class;
        } else if (edmTypeName.endsWith("String")) {
            result = String.class;
        }

        return result;
    }

    /**
     * Returns the name of the corresponding Java class or scalar type.
     * 
     * @param edmTypeName
     *            The type name.
     * @return The name of the corresponding Java class or scalar type.
     */
    public static String toJavaTypeName(String edmTypeName) {
        String result = "Object";
        if (edmTypeName.endsWith("Binary")) {
            result = "byte[]";
        } else if (edmTypeName.endsWith("Boolean")) {
            result = "boolean";
        } else if (edmTypeName.endsWith("DateTime")) {
            result = "Date";
        } else if (edmTypeName.endsWith("DateTimeOffset")) {
            result = "Date";
        } else if (edmTypeName.endsWith("Time")) {
            result = "long";
        } else if (edmTypeName.endsWith("Decimal")) {
            result = "double";
        } else if (edmTypeName.endsWith("Single")) {
            result = "double";
        } else if (edmTypeName.endsWith("Double")) {
            result = "double";
        } else if (edmTypeName.endsWith("Guid")) {
            result = "String";
        } else if (edmTypeName.endsWith("Int16")) {
            result = "short";
        } else if (edmTypeName.endsWith("Int32")) {
            result = "int";
        } else if (edmTypeName.endsWith("Int64")) {
            result = "long";
        } else if (edmTypeName.endsWith("Byte")) {
            result = "byte";
        } else if (edmTypeName.endsWith("String")) {
            result = "String";
        }

        return result;
    }
}
