package bingo.odata.consumer.requests.behaviors;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.api.client.http.HttpRequest;

import bingo.lang.Arrays;
import bingo.lang.Collections;
import bingo.lang.Strings;
import bingo.lang.enumerable.ArrayEnumerable;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerImpl;
import bingo.odata.consumer.TestResource;

public class BehaviorTest {
	
	static boolean execute;
	@Test
	public void testBehaviorDoExecute() {
		execute = false;
		ODataConsumer consumer = new ODataConsumerImpl(TestResource.serviceUrls.get("local"), new Behavior() {
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
