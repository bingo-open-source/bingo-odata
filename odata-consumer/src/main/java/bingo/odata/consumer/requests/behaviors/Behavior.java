package bingo.odata.consumer.requests.behaviors;

import com.google.api.client.http.HttpRequest;

/**
 * Extension-point for modifying client http requests.
 */
public interface Behavior {

  /**
   * Transforms the current http request.
   *
   * @param request  the current http request
   * @return the modified http request
   */
	HttpRequest transform(HttpRequest request);

}
