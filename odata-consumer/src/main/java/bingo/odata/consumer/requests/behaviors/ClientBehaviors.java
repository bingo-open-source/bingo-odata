package bingo.odata.consumer.requests.behaviors;

import com.google.api.client.http.HttpRequest;

/**
 * A static factory to create built-in {@link ClientBehavior} instances.
 */
public class ClientBehaviors {

  private ClientBehaviors() {}
  
  public static ClientBehavior oauthAuth(String accessToken) {
	  return new OAuthAuthenticationBehavior(accessToken);
  }

  /**
   * Creates a behavior that sleeps a specified amount of time before each client request.
   *
   * @param millis  the time to sleep in milliseconds
   * @return a behavior that sleeps a specified amount of time before each client request
   */
  public static ClientBehavior rateLimit(final long millis) {
    return new ClientBehavior() {
      @Override
      public HttpRequest transform(HttpRequest request) {
        try {
          Thread.sleep(millis);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        return request;
      }
    };
  }

}
