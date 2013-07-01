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
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataQueryInfoParser;
import bingo.odata.ODataQueryOptions;
import bingo.odata.ODataServices;
import bingo.odata.ODataUrlInfo;
import bingo.odata.consumer.demo.DemoModelAndData;
import bingo.odata.consumer.demo.DemoODataProducer;
import bingo.odata.consumer.ext.OrderByDirection;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntityBuilder;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataKeyImpl;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataValue;

public class ODataConsumerImplTest {
	
	ODataConsumer consumer = new ODataConsumerImpl(TestResource.serviceUrls.get("local"), false);
	
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
		consumer.retrieveServiceMetadata();
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
		int result = consumer.deleteEntity("Product", "123456");
		assertEquals(1, result);
	}

	@Test
	public void testDeleteEntity_usingOdataModel() {
		consumer.retrieveServiceMetadata();
		EdmEntitySet entitySet = consumer.services().findEntitySet("Products");
		EdmEntityType entityType = consumer.services().findEntityType("Product");
		int result = consumer.deleteEntity(entityType, new ODataKeyImpl("123456"));
		assertEquals(1, result);
	}
	
	@Test
	public void testUpdateEntity() {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("name", "new name here");
		int result = consumer.updateEntity("Product", "123456", fields);
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
	public void testFindEntity() {
		ODataEntity entity = consumer.findEntity("Product", "123456");
		assertNotNull(entity);
		System.out.println(entity);
	}
	
	@Test
	public void testFindEntitySet() {
		List<Map<String, Object>> entitySet = consumer.findEntitySet("Products");
		assertNotNull(entitySet);
		assertEquals(3, entitySet.size());
	}
	
	@Test
	public void testQuery() {
		List<Map<String, Object>> entitySet = consumer.query("Products")
				.where("name=:name").param("name", "bread").orderBy("id", OrderByDirection.asc).exec();
		assertNotNull(entitySet);
		assertEquals(3, entitySet.size());
	}
	
	@Test
	public void testFindEntitySet_withCondition() {
		List<Map<String, Object>> entitySet = consumer.findEntitySet("Products", "name=bread");
		assertNotNull(entitySet);
		assertEquals(3, entitySet.size());
	}

	@Test
	public void testFindProperty() {
		ODataProperty value = consumer.findProperty("Product", "123456", "name");
		assertNotNull(value);
		assertEquals(DemoODataProducer.INVOKED_FUNCTION, value.getValue().toString());
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
		String responseString = consumer.invokeFunction("getString", paramsMap, "Products");
		assertEquals("\"" + DemoODataProducer.INVOKED_FUNCTION + "\"", responseString);
	}
	
	@Test
	public void testInvokeFunction_returnObject() {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("rating", 2);
		Product product = consumer.invokeFunction("getEntity", paramsMap, "Products", Product.class);
		assertNotNull(product);
		assertEquals("Bread", product.getName());
	}

	@Test
	public void testInvokeFunction_returnListObject() {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("rating", 2);
		List<Product> product = consumer.invokeFunctionForList("getEntitySet", paramsMap, "Products", Product.class);
		assertNotNull(product);
		assertEquals(3, product.size());
	}
	
	@Test
	public void testInvokeFunction_returnODataValue() {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("rating", 2);
		ODataValue product = consumer.invokeFunctionForODataValue("getEntity", paramsMap, "Products");
		assertNotNull(product);
		assertEquals(ODataObjectKind.Entity, product.getKind());
		ODataEntity entity = (ODataEntity) product.getValue();
		assertEquals("Bread", entity.getPropertyValue("Name"));
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
