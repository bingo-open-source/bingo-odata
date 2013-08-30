package bingo.odata.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import bingo.lang.Converts;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataServices;
import bingo.odata.consumer.test.TestResource;
import bingo.odata.consumer.test.TestWithServerRunning;
import bingo.odata.consumer.test.mock.DemoODataProducer;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntityBuilder;
import bingo.odata.model.ODataKeyImpl;
import bingo.odata.model.ODataValue;

public class ODataConsumerImplTest extends TestWithServerRunning {
	
//	ODataConsumer consumer = new ODataConsumerImpl(TestResource.serviceUrls.get("remoteReadWrite"), false);
	ODataConsumer consumer = new ODataConsumerImpl(TestResource.serviceUrls.get("local"), false);
	
	public static void main(String[] args) {
		new ODataConsumerImplTest().testRetrieveServiceMetadata();
	}
	
	@Before
	public void beforeMethod() {
		consumer.getConfig().setLogPrintHttpMessageBody(true);
	}

	@Test
	public void testRetrieveServiceMetadata() {
		try {
			ODataServices service = consumer.getServiceMetadata();
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
		EdmEntitySet entitySet = consumer.getServiceMetadata().findEntitySet("Products");
		EdmEntityType entityType = consumer.getServiceMetadata().findEntityType("Product");
		ODataEntity obj = new ODataEntityBuilder(entitySet, entityType).addProperties(product).build();
		ODataEntity result = consumer.insert(obj);
		assertNotNull(result);
		
		Map<String, Object> result1 = consumer.insert("Product", product);
		assertNotNull(result1);
	}

	@Test
	public void testDeleteEntity() {
		boolean result = consumer.delete("Product", "123456");
		assertEquals(true, result);
	}

	@Test
	public void testDeleteEntity_usingOdataModel() {
		EdmEntityType entityType = consumer.getServiceMetadata().findEntityType("Product");
		boolean result = consumer.delete(entityType, new ODataKeyImpl("123456"));
		assertEquals(true, result);
	}
	
	@Test
	public void testUpdateEntity() {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("Name", "new name here");
		boolean result = consumer.update("Product", 0, fields);
		assertEquals(true, result);
	}

//	@Test PATCH method not supported!
//	public void testMergeEntity() {
//		Map<String, Object> fields = new HashMap<String, Object>();
//		fields.put("name", "new name here");
//		int result = consumer.mergeEntityByKey("Product", "123456", fields);
//		assertEquals(1, result);
//	}

	@Test
	public void testFind() {
		Map<String, Object> entity = consumer.find("Product", 0);
		assertNotNull(entity);
		System.out.println(entity);
	}
	
	@Test
	public void testQueryList() {
		List<Map<String, Object>> list = consumer.query("Product").list();
		assertNotNull(list);
		assertEquals(3, list.size());
	}
	
	@Test
	public void testQuery_withCondition() {
		List<Map<String, Object>> list = consumer.query("Product").where("name=bread").list();
		assertNotNull(list);
		assertEquals(3, list.size());
	}

	@Test
	public void testQuery() {
		List<Map<String, Object>> list = consumer.query("Product")
//				.where("Name=:name")
//				.param("name", "Bread")
//				.orderBy("ID", OrderByDirection.asc).orderBy("name", OrderByDirection.desc)
//				.expand("friend", "any")
				.page(1, 3)
				.select("Name")
				.list();
		assertNotNull(list);
		assertEquals(3, list.size());
	}
	
	@Test
	public void testFindProperty() {
		String value = consumer.findProperty("Product", "123456", "name");
		assertNotNull(value);
		assertEquals(DemoODataProducer.INVOKED_FUNCTION, value);
	}

	@Test
	public void testCount() {
		long result = consumer.query("Product").count();
		assertEquals(3, result);
	}
	
	@Test
	public void testCount_withCondition() {
		long result = consumer.query("Product").where("ID>?").param(5).page(1, 3).count();
		assertEquals(3, result);
	}

	@Test
	public void testInvokeFunction_forString() {
		String responseString = consumer.invokeFunction("getString").param("rating", 22)
				.entity("Product").invokeForStringResult();
		assertEquals(DemoODataProducer.INVOKED_FUNCTION, responseString);
	}

	@Test
	public void testInvokeFunction_returnObject() {
		Product product = consumer.invokeFunction("getEntity").param("rating", 2)
				.entity("Product").invokeForTypeResult(Product.class);
		assertNotNull(product);
		assertEquals("张三", product.getName());
	}
	
	@Test
	public void testInvokeFunction_paramDate() {
		Date product = consumer.invokeFunction("paramDate")
				.param("startTime", new Date(987654321000L))
				.entity("Product").invokeForTypeResult(Date.class);
		assertNotNull(product);
		assertEquals(987654321000L, product.getTime());
	}
	
	@Test
	public void testInvokeFunction_returnInt() {
		int product = consumer.invokeFunction("getInt")
				.entity("Product").invokeForTypeResult(Integer.class);
		assertNotNull(product);
		assertEquals(1024, product);
	}
	
	@Test
	public void testInvokeFunction_returnLong() {
		long product = consumer.invokeFunction("getLong")
				.entity("Product").invokeForTypeResult(Long.class);
		assertNotNull(product);
		assertEquals(9876543210L, product);
	}
	
	@Test
	public void testInvokeFunction_returnDouble() {
		double product = consumer.invokeFunction("getDouble")
				.entity("Product").invokeForTypeResult(Double.class);
		assertNotNull(product);
		assertEquals(3.1415926, product, 0.0001);
	}
	
	@Test
	public void testInvokeFunction_returnBoolean() {
		Boolean product = consumer.invokeFunction("getBoolean")
				.entity("Product").invokeForTypeResult(Boolean.class);
		assertNotNull(product);
		assertEquals(true, product);
	}
	
	@Test
	public void testInvokeFunction_returnDate() {
		Date product = consumer.invokeFunction("getDate")
				.entity("Product").invokeForTypeResult(Date.class);
		assertNotNull(product);
		assertEquals(new Date(123456789), product);
	}

	@Test
	public void testInvokeFunction_returnListObject() {
		List<Product> product = consumer.invokeFunction("getEntitySet").param("rating", "参数")
				.entity("Product").invokeForTypeListResult(Product.class);
		assertNotNull(product);
		assertEquals(3, product.size());
	}
	
	@Test
	public void testInvokeFunction_returnVoid() {
		try {
			consumer.invokeFunction("getEntitySet").param("rating", "参数")
			.entity("Product").invoke();
			Assert.assertTrue(true);
		} catch (Throwable e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testInvokeFunction_returnODataValue() {
		ODataValue product = consumer.invokeFunction("getEntity").param("rating", 2)
				.entity("Product").invokeForODataValueResult();
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
