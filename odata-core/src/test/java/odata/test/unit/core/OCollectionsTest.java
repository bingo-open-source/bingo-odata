package odata.test.unit.core;

import junit.framework.Assert;

import odata.OCollection;
import odata.OCollections;
import odata.OObject;
import odata.OSimpleObjects;
import odata.edm.EdmSimpleType;

import org.junit.Test;


public class OCollectionsTest {

    private static final String VALUE     = "value";
    private static final String HEX_VALUE = "0x76616c7565";

    @Test
    public void stringCollectionToStringTest() {
        OCollection<OObject> collection = OCollections.newBuilder(EdmSimpleType.STRING).add(OSimpleObjects.create(EdmSimpleType.STRING, VALUE)).build();
        String toString = collection.toString();
        Assert.assertTrue(toString.contains(VALUE));
    }

    @Test
    public void binaryCollectionToStringTest() {
        OCollection<OObject> collection = OCollections.newBuilder(EdmSimpleType.BINARY).add(OSimpleObjects.create(EdmSimpleType.BINARY, VALUE.getBytes())).build();
        String toString = collection.toString();
        Assert.assertTrue(toString.contains(HEX_VALUE));
    }
}