package bingo.odata.utils;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import bingo.meta.edm.EdmSimpleType;

public class InternalTypeUtilsTest {

	public static Date date = new Date(987654321L);
	public static String dateString;
	
	@Test
	public void testFormatDateTime() {
		System.out.println(date.getTime());
		dateString = InternalTypeUtils.formatDateTime(date);
		System.out.println(dateString);
		date = InternalTypeUtils.parseDateTime(dateString);
		System.out.println(date);
		System.out.println(date.getTime());
	}

	@Test
	public void testToEdmDateTime() {
		dateString = InternalTypeUtils.toEdmString(date, EdmSimpleType.DATETIME);
		System.out.println(dateString);
	}

}
