package bingo.odata.test.unit.core;

import junit.framework.Assert;

import org.junit.Test;

import bingo.odata.OProperties;
import bingo.odata.OProperty;

public class OPropertiesTest {

    private static final String NAME      = "name";
    private static final String VALUE     = "value";
    private static final String HEX_VALUE = "0x76616c7565";

    @Test
    public void stringPropertyToStringTest() {
        OProperty<String> property = OProperties.simple(NAME, VALUE);
        String toString = property.toString();
        Assert.assertTrue(toString.contains(NAME));
        Assert.assertTrue(toString.contains(VALUE));
    }

    @Test
    public void binaryPropertyToStringTest() {
        OProperty<byte[]> property = OProperties.simple(NAME, VALUE.getBytes());
        String toString = property.toString();
        Assert.assertTrue(toString.contains(NAME));
        Assert.assertTrue(toString.contains(HEX_VALUE));
    }
}
