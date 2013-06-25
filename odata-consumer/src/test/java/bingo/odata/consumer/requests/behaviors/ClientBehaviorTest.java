package bingo.odata.consumer.requests.behaviors;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.api.client.http.HttpRequest;

import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerImpl;
import bingo.odata.consumer.TestResource;

public class ClientBehaviorTest {
	
	static boolean execute;
	@Test
	public void testBehaviorDoExecute() {
		execute = false;
		ODataConsumer consumer = new ODataConsumerImpl(TestResource.serviceUrls.get("local"), new ClientBehavior() {
			public HttpRequest transform(HttpRequest request) {
				execute = true;
				return request;
			}
		});
		assertTrue(execute);
	}
	
	static int order;
	@Test
	public void testBehaviorOrder() {
		order = 0;
		ODataConsumer consumer = new ODataConsumerImpl(TestResource.serviceUrls.get("local"), new ClientBehavior() {
			public HttpRequest transform(HttpRequest request) {
				order++;
				assertEquals(1, order);
				return request;
			}
		}, new ClientBehavior() {
			public HttpRequest transform(HttpRequest request) {
				order++;
				assertEquals(2, order);
				return request;
			}
		});
	}

}
