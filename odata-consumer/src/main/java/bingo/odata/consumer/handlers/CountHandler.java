package bingo.odata.consumer.handlers;

import bingo.odata.ODataResponseStatus;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.requests.CountRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.util.ODataConsumerContextHelper;
import bingo.odata.consumer.util.ODataMetadataVerifier;
import bingo.odata.consumer.util.ODataQueryTranslator;

public class CountHandler extends BaseHandler {

	public CountHandler(ODataConsumer consumer, ODataServices services,
			ODataMetadataVerifier verifier) {
		super(consumer, services, verifier);
	}
	public long count(String entitySet, String queryString, boolean resolveQueryString) {
		
		if(config.isVerifyMetadata()) verifier.hasEntitySet(entitySet);
		
		ODataConsumerContext context = ODataConsumerContextHelper
				.initEntitySetContext(consumer, entitySet);
		
		Request request = new CountRequest(context, config.getProducerUrl())
				.setEntitySet(entitySet);

		if(resolveQueryString) {
			queryString = ODataQueryTranslator.translateFilter(queryString);
		}
			
		request.addAdditionalQueryString(queryString);
		
		Response resp = request.send();
		
		if(resp.getStatus() == ODataResponseStatus.OK) {
			
			return resp.convertToRawLong(context);
			
		} else throw resp.convertToError(context);
	}
}
