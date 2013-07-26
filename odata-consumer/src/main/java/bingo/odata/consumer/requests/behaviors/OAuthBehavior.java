package bingo.odata.consumer.requests.behaviors;


import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;

public class OAuthBehavior implements Behavior {
	
  static final String HEADER_PREFIX = "Bearer ";
  
  private String accessToken;

  public OAuthBehavior(String accessToken) {
	  this.accessToken = accessToken;
  }

  public HttpRequest transform(HttpRequest request) {
	  HttpHeaders headers = request.getHeaders();
	  headers.setAuthorization(HEADER_PREFIX + accessToken);
	  request.setHeaders(headers);
	  return request;
  }

}