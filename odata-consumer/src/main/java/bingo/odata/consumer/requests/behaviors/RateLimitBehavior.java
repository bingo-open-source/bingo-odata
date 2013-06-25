package bingo.odata.consumer.requests.behaviors;

import com.google.api.client.http.HttpRequest;

/**
 * Creates a behavior that sleeps a specified amount of time before each client request.
 * @author CalvinChen
 *
 */
public class RateLimitBehavior implements ClientBehavior {

	private long waitMillis;

	public RateLimitBehavior(long waitMillis) {
		this.waitMillis = waitMillis;
	}

	public HttpRequest transform(HttpRequest request) {
		try {
			Thread.sleep(waitMillis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return request;
	}

}
