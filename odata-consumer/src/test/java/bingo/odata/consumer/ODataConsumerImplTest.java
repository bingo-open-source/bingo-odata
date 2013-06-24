package bingo.odata.consumer;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import bingo.odata.ODataServices;
import bingo.odata.consumer.demo.DemoODataProducer;
import bingo.odata.consumer.exceptions.ConnectFailedException;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;

public class ODataConsumerImplTest {
	
	private static final String LOCALHOST_SERVICE = "http://localhost:8080/demo";

	ODataConsumer consumer = new ODataConsumerImpl(LOCALHOST_SERVICE);
	
	public static void main(String[] args) {
		new ODataConsumerImplTest().testRetrieveServiceMetadata();
	}

	@Test
	public void testRetrieveServiceMetadata() {
		try {
			ODataServices service = consumer.retrieveServiceMetadata();
			assertNotNull(service);
		} catch (Throwable e) {
			System.out.println("failed");
		}
	}

	@Test
	public void testRetrieveEntitySetString() {
		fail("Not yet implemented");
		ODataEntitySet entitySet = consumer.findEntitySet("Products");
		assertNotNull(entitySet);
	}

	@Test
	public void testRetrieveEntityStringObject() {
		ODataEntity entity = consumer.findEntity("Product", "123456");
		assertNotNull(entity);
		System.out.println(entity);
	}

	@Test
	public void testInsertEntityStringODataEntity() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertEntityStringMapOfStringObject() {
		Map<String, Object> product = new HashMap<String, Object>();
		product.put("ID", "987654321");
		product.put("name", "chenkai");
		product.put("description", "postbychenkai");
		
		int result = consumer.insertEntity("Product", product);
		assertEquals(1, result);
	}

	@Test
	public void testDeleteEntityStringObject() {
		int result = consumer.deleteEntity("Product", "123456");
		assertEquals(1, result);
	}

	@Test
	public void testUpdateEntityStringObjectMapOfStringObject() {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("name", "new name here");
		int result = consumer.updateEntity("Product", "123456", fields);
		assertEquals(1, result);
	}

	@Test
	public void testQueryEntityStringQueryFilter() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryEntityString() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveEntitySetEdmEntityTypeODataQueryInfo() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveEntityEdmEntityTypeODataKeyODataQueryInfo() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveProperty() {
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveNavigationProperty() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertEntityEdmEntityTypeODataEntity() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateEntityEdmEntityTypeODataKeyODataEntity() {
		fail("Not yet implemented");
	}

	@Test
	public void testMergeEntity() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteEntityEdmEntityTypeODataKey() {
		fail("Not yet implemented");
	}

	@Test
	public void testInvokeFunction() {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("rating", 22);
		String responseString = consumer.invodeFunction("Products", "GetProductsByRating", paramsMap);
		assertEquals("\"" + DemoODataProducer.INVOKED_FUNCTION + "\"", responseString);
	}

	@Test
	public void testQuery() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindFunctionImport() {
		fail("Not yet implemented");
	}

}
