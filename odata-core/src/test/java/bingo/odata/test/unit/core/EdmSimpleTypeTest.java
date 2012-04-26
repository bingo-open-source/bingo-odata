package bingo.odata.test.unit.core;

import junit.framework.Assert;

import org.junit.Test;

import bingo.odata.edm.EdmSimpleType;

public class EdmSimpleTypeTest {

    @Test
    public void edmSimpleTypeTests() {
        Assert.assertTrue(EdmSimpleType.STRING.getJavaTypes().contains(String.class));
    }
}
