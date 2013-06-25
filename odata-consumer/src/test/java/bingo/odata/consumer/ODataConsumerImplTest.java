package bingo.odata.consumer;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import bingo.lang.New;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmEntityTypeRef;
import bingo.odata.ODataServices;
import bingo.odata.consumer.demo.DemoODataProducer;
import bingo.odata.consumer.exceptions.ConnectFailedException;
import bingo.odata.consumer.requests.behaviors.ClientBehavior;
import bingo.odata.consumer.requests.behaviors.OAuthAuthenticationBehavior;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntityBuilder;
import bingo.odata.model.ODataEntitySet;

public class ODataConsumerImplTest {
	
	ODataConsumer consumer = new ODataConsumerImpl(TestResource.serviceUrls.get("local"));
	
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
		ODataEntitySet entitySet = consumer.findEntitySet("Products");
		assertNotNull(entitySet);
		assertEquals(3, entitySet.getEntities().size());
	}

	@Test
	public void testRetrieveEntityStringObject() {
		ODataEntity entity = consumer.findEntity("Product", "123456");
		assertNotNull(entity);
		System.out.println(entity);
	}

	@Test
	public void testInsertEntityStringODataEntity() {
		Map<String, Object> product = new HashMap<String, Object>();
		product.put("ID", "987654321");
		product.put("name", "chenkai");
		product.put("description", "postbychenkai");
		
		EdmEntitySet entitySet = consumer.services().findEntitySet("Products");
		EdmEntityType entityType = consumer.services().findEntityType("Product");
		ODataEntity obj = new ODataEntityBuilder(entitySet, entityType).addProperties(product).build();
		int result = consumer.insertEntity("Product", obj);
		assertEquals(1, result);
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
		long result = consumer.count("Products");
		assertEquals(3, result);
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
