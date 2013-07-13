package bingo.odata.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import bingo.lang.Converts;
import bingo.lang.New;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataQueryInfoParser;
import bingo.odata.ODataQueryOptions;
import bingo.odata.ODataServices;
import bingo.odata.ODataUrlInfo;
import bingo.odata.consumer.ext.OrderByDirection;
import bingo.odata.consumer.ext.Page;
import bingo.odata.consumer.test.TestResource;
import bingo.odata.consumer.test.TestWithServerRunning;
import bingo.odata.consumer.test.mock.DemoModelAndData;
import bingo.odata.consumer.test.mock.DemoODataProducer;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntityBuilder;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataKeyImpl;
import bingo.odata.model.ODataNavigationProperty;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataValue;

public class ODataConsumerImplTest extends TestWithServerRunning {
	
//	ODataConsumer consumer = new ODataConsumerImpl(TestResource.serviceUrls.get("remoteReadWrite"), false);
	ODataConsumer consumer = new ODataConsumerImpl(TestResource.serviceUrls.get("local"), false);
	
	public static void main(String[] args) {
		new ODataConsumerImplTest().testRetrieveServiceMetadata();
	}
	
	@Before
	public void beforeMethod() {
		consumer.config().setLogPrintHttpMessageBody(true);
	}

	@Test
	public void testRetrieveServiceMetadata() {
		try {
			ODataServices service = consumer.retrieveServiceMetadata();
			assertNotNull(service);
		} catch (Throwable e) {
			fail();
			throw new RuntimeException(e);
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
		
		result = consumer.insertEntityByMap("Product", product);
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
		fields.put("Name", "new name here");
		int result = consumer.updateEntityByMap("Product", 0, fields);
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
		Map<String, Object> entity = consumer.findEntity("Product", 0);
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
	public void testFindEntitySet_withCondition() {
		List<Map<String, Object>> entitySet = consumer.findEntitySet("Products", "name=bread");
		assertNotNull(entitySet);
		assertEquals(3, entitySet.size());
	}

	@Test
	public void testQuery() {
		List<Map<String, Object>> entitySet = consumer.query("Products")
//				.where("Name=:name")
//				.param("name", "Bread")
//				.orderBy("ID", OrderByDirection.asc).orderBy("name", OrderByDirection.desc)
//				.expand("friend", "any")
				.page(1, 3)
				.select("Name")
				.exec();
		assertNotNull(entitySet);
		assertEquals(3, entitySet.size());
	}
	
	@Test
	public void testFindProperty() {
		String value = consumer.findPropertyForString("Product", "123456", "name");
		assertNotNull(value);
		assertEquals(DemoODataProducer.INVOKED_FUNCTION, value);
	}

//	@Test
//	public void testFindNavigationProperty() {
//		String value = consumer.findNavigationProperty("Product", "123456", "name", String.class);
//		assertNotNull(value);
//		assertEquals(DemoODataProducer.INVOKED_FUNCTION, value);
//	}

	@Test
	public void testCount() {
		long result = consumer.count("Products");
		assertEquals(3, result);
	}
	
	@Test
	public void testCount_withCondition() {
		long result = consumer.count("Products", "ID>?", 5, new Page(1, 3));
		assertEquals(3, result);
	}

	@Test
	public void testInvokeFunction_forString() {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("rating", 22);
		String responseString = consumer.invokeFunctionForString("getString", paramsMap, "Products");
		assertEquals(DemoODataProducer.INVOKED_FUNCTION, responseString);
	}
	
//	@Test
//	public void testInvokeFunction() {
//		Map<String, Object> paramsMap = new HashMap<String, Object>();
//		paramsMap.put("rating", 4);
//		List<Product> product = consumer.invokeFunctionForEntityList("GetProductsByRating", paramsMap, "Products", Product.class);
//		assertNotNull(product);
//		assertEquals(1, product.size());
//	}
	
	@Test
	public void testInvokeFunction_returnObject() {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("rating", 2);
		Product product = consumer.invokeFunctionForEntity("getEntity", paramsMap, "Products", Product.class);
		assertNotNull(product);
		assertEquals("张三", product.getName());
	}
	
	@Test
	public void testInvokeFunction_paramDate() {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("startTime", new Date(987654321000L));
		Date product = consumer.invokeFunctionForEntity("paramDate", paramsMap, "Products", Date.class);
		assertNotNull(product);
		assertEquals(987654321000L, product.getTime());
	}
	
	@Test
	public void testInvokeFunction_returnInt() {
		int product = consumer.invokeFunctionForType("getInt", null, "Products", Integer.class);
		assertNotNull(product);
		assertEquals(1024, product);
	}
	
	@Test
	public void testInvokeFunction_returnLong() {
		long product = consumer.invokeFunctionForType("getLong", null, "Products", Long.class);
		assertNotNull(product);
		assertEquals(9876543210L, product);
	}
	
	@Test
	public void testInvokeFunction_returnDouble() {
		double product = consumer.invokeFunctionForType("getDouble", null, "Products", Double.class);
		assertNotNull(product);
		assertEquals(3.1415926, product, 0.0001);
	}
	
	@Test
	public void testInvokeFunction_returnBoolean() {
		Boolean product = consumer.invokeFunctionForType("getBoolean", null, "Products", Boolean.class);
		assertNotNull(product);
		assertEquals(true, product);
	}
	
	@Test
	public void testInvokeFunction_returnDate() {
		Date product = consumer.invokeFunctionForType("getDate", null, "Products", Date.class);
		assertNotNull(product);
		assertEquals(new Date(123456789), product);
	}

	@Test
	public void testInvokeFunction_returnListObject() {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("rating", "参数");
		List<Product> product = consumer.invokeFunctionForEntityList("getEntitySet", paramsMap, "Products", Product.class);
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
		assertEquals("张三", entity.getPropertyValue("Name"));
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
