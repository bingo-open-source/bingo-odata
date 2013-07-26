package bingo.odata.consumer.requests;

import static bingo.odata.ODataConstants.Headers.DATA_SERVICE_VERSION;
import static bingo.odata.ODataConstants.Headers.MAX_DATA_SERVICE_VERSION;
import static bingo.odata.ODataConstants.Headers.MIN_DATA_SERVICE_VERSION;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bingo.lang.Assert;
import bingo.lang.Collections;
import bingo.lang.Strings;
import bingo.lang.convert.InputStreamConverter;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.odata.ODataConstants;
import bingo.odata.ODataConstants.ContentTypes;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.exceptions.ConnectFailedException;
import bingo.odata.consumer.requests.behaviors.Behavior;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpEncoding;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class Request {

	private static final Log log = LogFactory.get(Request.class);
	
	protected static final String URL_FRAGMENT_DIVIDER = "/";
	
	protected String accept;
	protected Map<String, String> headers = new HashMap<String, String>();
	protected Map<String, String> parameters = new HashMap<String, String>();
	protected String additionalQueryString;
	protected String method = HttpMethods.GET;
	protected String serviceRoot;
	protected String resourcePath;
	protected ODataConsumerContext context;
	protected HttpRequest httpRequest;
	protected boolean isLog = true;
	
	public Request(ODataConsumerContext context, String serviceRoot) {
		Assert.notNull(context);
		Assert.notBlank(serviceRoot);
		this.serviceRoot = serviceRoot;
		this.context = context;
	}
	
	public Request(ODataConsumerContext context, String serviceRoot, String resourcePath) {
		this(context, serviceRoot);
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
	
	public Request addParameters(Map<String, String> parameters) {
		this.parameters.putAll(parameters);
		return this;
	}

	public String getMethod() {
		Assert.notBlank(method);
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
		String queryString = "";
		if(0 != parameters.size()) {
			Object[] keys = parameters.keySet().toArray();
			StringBuilder builder = new StringBuilder();
			for (Object key : keys) {
				builder.append(key).append("=").append(parameters.get(key)).append("&");
			}
			queryString = builder.substring(0, builder.length() - 1);
		}
		queryString = this.addQueryString(queryString, additionalQueryString);
		
		if(Strings.isNotBlank(queryString)) queryString = queryString.replaceAll(" ", "%20");
		return queryString;
	}
	
	public Request addAdditionalQueryString(String queryString) {
		additionalQueryString = this.addQueryString(this.additionalQueryString, queryString);
		return this;
	}

	public String getHeader(String name) {
		return headers.get(name);
	}
	
	public void addHeader(String name, String value) {
		headers.put(name, value);
	}

	public Object getParameter(String name) {
		return parameters.get(name);
	}
	
	public Response send() {
		beforeSend();
		
		final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		
		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();
		
		this.setUpDefaultHeaders();
		
		this.setUpDefaultParameters();
		
		httpRequest = buildRequest(requestFactory);
		
//		httpRequest.setConnectTimeout(500000);
		
		handleBehaviors(httpRequest);
		
		// let google http client not throw exception when response code > 300
		// treat all response as normal response here.
		httpRequest.setThrowExceptionOnExecuteError(false);

		if(log.isInfoEnabled() && isLog){
			log.info("Send Request:" + this);
		}
			
		try {
			Response response = new Response(httpRequest.execute());
			
			if(log.isInfoEnabled() && isLog) {
				log.info("Received Response:" + (this.context.isLogPrintHttpMessageBody()?
						response.toString(true) : response.toString(false)));
			}
			return response;
		} catch (IOException e) {
			throw new ConnectFailedException(httpRequest.getUrl().toString());
		}
	}

	protected void beforeSend() {
		// nothing.
	}

	private void handleBehaviors(HttpRequest req) {
		List<Behavior> behaviors = context.getBehaviors();
		if(!Collections.isEmpty(behaviors)) {
			for (Behavior behavior : behaviors) {
				if(null != behavior) req = behavior.transform(req);
			}
		}
	}

	private HttpRequest buildRequest(HttpRequestFactory requestFactory) {
		HttpRequest req = null;
		try {
			req = requestFactory.buildRequest(getMethod(), new GenericUrl(), null);
			req.setUrl(generateUrl());
			req.setHeaders(generateHeaders());
			req.setContent(generateContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return req;
	}

	protected HttpHeaders generateHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.putAll(headers);
		httpHeaders.setAccept(accept);
		httpHeaders.setContentType(ContentTypes.APPLICATION_JSON_UTF8);
		return httpHeaders; 
	}

	protected HttpContent generateContent() {
		return null;
	}

	protected GenericUrl generateUrl() {
		String query = getQueryString();
		String str = serviceRoot + getResourcePath() + (Strings.isNotBlank(query)? "?" + query : "");
		GenericUrl url = new GenericUrl(str);
		return url;
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

	protected void setUpDefaultHeaders() {
		this.setUpDefaultHeaders_odataVersion();
		this.setUpDefaultHeaders_accept();
	}
	
	protected void setUpDefaultHeaders_odataVersion() {
		this.addHeader(DATA_SERVICE_VERSION, context.getVersion().getValue());
		this.addHeader(MAX_DATA_SERVICE_VERSION, context.getVersion().getValue());
		this.addHeader(MIN_DATA_SERVICE_VERSION, context.getVersion().getValue());
	}
	
	protected void setUpDefaultHeaders_accept() {
		this.setAccept(context.getFormat().getContentType());
	}
	
	protected void setUpDefaultParameters() {
		this.setUpDefaultParameters_format();
	}
	
	protected void setUpDefaultParameters_format() {
		this.addParameter(ODataConstants.QueryOptions.FORMAT, context.getFormat().getValue());
	}
	
	protected static String addQueryString(String queryString1, String queryString2) {
		if(Strings.isBlank(queryString1)) return queryString2;
		if(Strings.isBlank(queryString2)) return queryString1;
		
		return queryString1 + "&" + queryString2;
	}

	@Override
	public String toString() {
		HttpHeaders httpHeaders = null;
		if(null == this.httpRequest) {
			httpHeaders = generateHeaders();
		} else {
			httpHeaders = this.httpRequest.getHeaders();
		}
		String blank = " ", nextLine = "\n", tab = "\t", nt = nextLine + tab;
		String str = nextLine + 
				nt + this.getMethod() + blank + this.generateUrl().toString() +
				nt + httpHeaders.toString();
		if(this.context.isLogPrintHttpMessageBody()) {
			HttpContent httpContent = null;
			if(null == this.httpRequest) {
				httpContent = generateContent();
			} else {
				httpContent = this.httpRequest.getContent();
			}
			if(httpContent instanceof ByteArrayContent) {
				String content = "";
				try {
					content = new InputStreamConverter().convertToString(((ByteArrayContent)httpContent).getInputStream());
				} catch (Throwable e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				if(Strings.isNotBlank(content)) str += nt + content;
			}
		}
		return str + nt;
	}

	public boolean isLog() {
		return isLog;
	}

	/**
	 * mainly used for test output.
	 * @param isLog
	 */
	public void setLog(boolean isLog) {
		this.isLog = isLog;
	}
}
