package bingo.odata.consumer.requests;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import bingo.lang.Strings;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;

public class Request {

	private static final Log log = LogFactory.get(Request.class);
	
	protected static final String URL_FRAGMENT_DIVIDER = "/";
	
	protected String contentType;
	protected Map<String, String> headers = new HashMap<String, String>();
	protected Map<String, String> parameters = new HashMap<String, String>();
	protected String method;
	protected String serviceRoot;
	protected String resourcePath;
	protected String queryString;
	
	public Request() {}
	
	public Request(String serviceRoot, String resourcePath) {
		this.serviceRoot = serviceRoot;
		this.resourcePath = resourcePath;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
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
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
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
	
	public Response send() throws IOException {
		final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		
		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();
		
		HttpRequest req = buildRequest(requestFactory);
		
//		if(log.isDebugEnabled())  // TODO open after complete
			report(req);
		
		return new Response(req.execute());
	}

	protected HttpRequest buildRequest(HttpRequestFactory requestFactory) {
		HttpRequest req = null;
		if(Strings.isBlank(method)) method = "GET";
		try {
			req = requestFactory.buildRequest(method, new GenericUrl(), null);
			req.setUrl(genUrl());
			req.setHeaders(genHeaders());
			req.getHeaders().setContentType(contentType);
			req.setContent(genContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return req;
	}

	protected HttpHeaders genHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.putAll(headers);
		return httpHeaders; 
	}

	protected HttpContent genContent() {
		return null;
	}

	protected GenericUrl genUrl() {
		String str = serviceRoot + resourcePath + "?" + queryString;
		GenericUrl url = new GenericUrl(str);
		return url;
	}

	private void report(HttpRequest req) {
		String blank = " ", nextLine = "\n", tab = "\t", nt = nextLine + tab;
		String str = nextLine + 
					nt + req.getRequestMethod() + blank + req.getUrl().toString() +
					nt + req.getHeaders().toString() + 
					nextLine;
		// TODO add content
		log.info(str);
	}
}
