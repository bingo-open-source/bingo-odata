package bingo.odata.consumer.requests.behaviors;

import com.google.api.client.http.HttpRequest;

/**
 * Extension-point for modifying client http requests.
 *
 * <p>The {@link ClientBehaviors} static factory class can be used to create built-in <code>OClientBehavior</code> instances.</p>
 */
public interface ClientBehavior {

  /**
   * Transforms the current http request.
   *
   * @param request  the current http request
   * @return the modified http request
   */
	HttpRequest transform(HttpRequest request);

}
