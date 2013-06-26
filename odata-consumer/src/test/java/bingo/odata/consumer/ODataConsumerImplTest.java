package bingo.odata.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import bingo.lang.Converts;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.odata.ODataServices;
import bingo.odata.consumer.demo.DemoODataProducer;
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
	public void testInsertEntity() {
		Map<String, Object> product = new HashMap<String, Object>();
		product.put("ID", "987654321");
		product.put("name", "chenkai");
		product.put("description", "postbychenkai");
		
		EdmEntitySet entitySet = consumer.services().findEntitySet("Products");
		EdmEntityType entityType = consumer.services().findEntityType("Product");
		ODataEntity obj = new ODataEntityBuilder(entitySet, entityType).addProperties(product).build();
		int result = consumer.insertEntity(entityType, obj);
		assertEquals(1, result);
		
		result = consumer.insertEntity("Product", product);
		assertEquals(1, result);
	}

	@Test
	public void testDeleteEntity() {
		int result = consumer.deleteEntityByKey("Product", "123456");
		assertEquals(1, result);
	}

	@Test
	public void testUpdateEntity() {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("name", "new name here");
		int result = consumer.updateEntityByKey("Product", "123456", fields);
		assertEquals(1, result);
	}

//	@Test PATCH method not supported!
//	public void testMergeEntity() {
//		Map<String, Object> fields = new HashMap<String, Object>();
//		fields.put("name", "new name here");
//		int result = consumer.mergeEntityByKey("Product", "123456", fields);
//		assertEquals(1, result);
//	}
	
	@Test
	public void testFindEntitySet() {
		ODataEntitySet entitySet = consumer.findEntitySet("Products");
		assertNotNull(entitySet);
		assertEquals(3, entitySet.getEntities().size());
	}

	@Test
	public void testFindEntity() {
		ODataEntity entity = consumer.findEntity("Product", "123456");
		assertNotNull(entity);
		System.out.println(entity);
	}

	@Test
	public void testFindProperty() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindNavigationProperty() {
		fail("Not yet implemented");
	}

	@Test
	public void testCount() {
		long result = consumer.count("Products");
		assertEquals(3, result);
	}

	@Test
	public void testInvokeFunction_simgleGetString() {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("rating", 22);
		String responseString = consumer.invokeFunction("Products", "getString", paramsMap);
		assertEquals("\"" + DemoODataProducer.INVOKED_FUNCTION + "\"", responseString);
	}
	
	@Test
	public void testInvokeFunction_returnObject() {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("rating", 2);
		Product product = consumer.invokeFunction("Products", "getEntity", paramsMap, Product.class);
		assertNotNull(product);
	}

	@Test
	public void testInvokeFunction_returnListObject() {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("rating", 2);
		List<Product> product = consumer.invokeFunctionForList("Products", "getEntitySet", paramsMap, Product.class);
		assertNotNull(product);
	}
	
	public class Product {
		private int id;
		private String name;
		private String description;
		private Date releaseDate;
		private Date discontinuedDate;
		private int rate;
		private double price;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Date getReleaseDate() {
			return releaseDate;
		}
		public void setReleaseDate(Date releaseDate) {
			this.releaseDate = releaseDate;
		}
		public Date getDiscontinuedDate() {
			return discontinuedDate;
		}
		public void setDiscontinuedDate(Date discontinuedDate) {
			this.discontinuedDate = discontinuedDate;
		}
		public int getRate() {
			return rate;
		}
		public void setRate(int rate) {
			this.rate = rate;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		@Override
		public String toString() {
			return Converts.convert(this, Map.class).toString();
		}
		
	}
}
