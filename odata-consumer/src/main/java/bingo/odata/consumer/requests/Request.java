package bingo.odata.consumer.requests;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bingo.lang.Strings;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class Request {

	private static final Log log = LogFactory.get(Request.class);
	
	protected static final String URL_FRAGMENT_DIVIDER = "/";
	
	protected String accept;
	protected Map<String, String> headers = new HashMap<String, String>();
	protected Map<String, String> parameters = new HashMap<String, String>();
	protected String method = HttpMethods.GET;
	protected String serviceRoot;
	protected String resourcePath;
	
	public Request() {}
	
	public Request(String serviceRoot, String resourcePath) {
		this.serviceRoot = serviceRoot;
		this.resourcePath = resourcePath;
	}

	public String getAccept() {
		return accept;
	}

	public Request setAccept(String accept) {
		this.accept = accept;
		return this;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public Request setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
		return this;
	}
	
	public Request addParameter(String name, String value) {
		parameters.put(name, value);
		return this;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getServiceRoot() {
		return serviceRoot;
	}

	public void setServiceRoot(String serviceRoot) {
		this.serviceRoot = serviceRoot;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getQueryString() {
		Object[] keys = parameters.keySet().toArray();
		StringBuilder builder = new StringBuilder();
		for (Object key : keys) {
			builder.append(key).append("=").append(parameters.get(key)).append("&");
		}
		return builder.substring(0, builder.length() - 1);
	}

	public String getHeader(String name) {
		return headers.get(name);
	}
	
	public void addHeader(String name, String value) {
		headers.put(name, value);
	}

	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	public Response send() {
		final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		
		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();
		
		HttpRequest req = buildRequest(requestFactory);
		
//		if(log.isDebugEnabled())  // TODO open after complete
			report(req);
		
		try {
			Response response = new Response(req.execute());
			log.info("Received Response:" + response);
			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	protected HttpRequest buildRequest(HttpRequestFactory requestFactory) {
		HttpRequest req = null;
		try {
			req = requestFactory.buildRequest(getMethod(), new GenericUrl(), null);
			req.setUrl(genUrl());
			req.setHeaders(genHeaders());
			req.setContent(genContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return req;
	}

	protected HttpHeaders genHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.putAll(headers);
		httpHeaders.setAccept(accept);
		return httpHeaders; 
	}

	protected HttpContent genContent() {
		return null;
	}

	protected GenericUrl genUrl() {
		String str = serviceRoot + getResourcePath() + "?" + getQueryString();
		GenericUrl url = new GenericUrl(str);
		return url;
	}

	private void report(HttpRequest req) {
		String blank = " ", nextLine = "\n", tab = "\t", nt = nextLine + tab;
		String str = "Send Request: " + 
					nextLine + 
					nt + req.getRequestMethod() + blank + req.getUrl().toString() +
					nt + req.getHeaders().toString() + 
					nextLine;
		// TODO add content
		log.info(str);
	}
	
	protected String addQueryString(String url) {
		String queryString = getQueryString();
		if(Strings.isNotBlank(queryString)) {
			if(!Strings.contains(url, "?")) {
				url += "?";
			} else {
				url += "&";
			}
			url += queryString;
		}
		return url;
	}
}
