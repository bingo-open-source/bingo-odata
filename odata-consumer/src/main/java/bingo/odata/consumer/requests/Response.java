package bingo.odata.consumer.requests;

import java.io.IOException;
import java.io.InputStream;

import com.google.api.client.http.HttpResponse;

public class Response {
	private HttpResponse httpResponse;
	
	public Response(HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
	}

	public String getContentType() {
		return httpResponse.getContentType();
	}

	public int getStatus() {
		return httpResponse.getStatusCode();
	}

	public Object getHeader(String name) {
		return httpResponse.getHeaders().get(name);
	}

	public InputStream getInputStream() throws IOException {
		return httpResponse.getContent();
	}

}
