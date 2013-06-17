package bingo.odata.consumer.requests.builders;

import bingo.odata.consumer.requests.Request;

public abstract class RequestBuilderBase implements RequestBuilder {
	protected Request request;

	@Override
	public Request build() throws Throwable {
		return request;
	}

}
