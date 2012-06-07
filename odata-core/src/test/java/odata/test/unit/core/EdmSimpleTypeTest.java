package odata.test.unit.core;

import junit.framework.Assert;

import odata.edm.EdmSimpleType;

import org.junit.Test;


public class EdmSimpleTypeTest {

    @Test
    public void edmSimpleTypeTests() {
        Assert.assertTrue(EdmSimpleType.STRING.getJavaTypes().contains(String.class));
    }
}
