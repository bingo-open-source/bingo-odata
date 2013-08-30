package bingo.odata.consumer.requests.behaviors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerImpl;
import bingo.odata.consumer.test.TestResource;
import bingo.odata.consumer.test.TestWithServerRunning;

import com.google.api.client.http.HttpRequest;

public class BehaviorTest extends TestWithServerRunning {
	
	boolean execute;
	@Test
	public void testBehaviorDoExecute() {
		execute = false;
		@SuppressWarnings("unused")
		ODataConsumer consumer = new ODataConsumerImpl(TestResource.serviceUrls.get("local"), new Behavior() {
			public HttpRequest transform(HttpRequest request) {
				execute = true;
				return request;
			}
		});
		assertTrue(execute);
	}
	
	int order;
	@Test
	public void testBehaviorOrder() {
		order = 0;
		@SuppressWarnings("unused")
		ODataConsumer consumer = new ODataConsumerImpl(TestResource.serviceUrls.get("local")
		, new Behavior() {
			public HttpRequest transform(HttpRequest request) {
				order++;
				assertEquals(1, order);
				return request;
			}
		}, new Behavior() {
			public HttpRequest transform(HttpRequest request) {
				order++;
				assertEquals(2, order);
				return request;
			}
		});
	}
}
