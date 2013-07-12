package bingo.odata.consumer.handlers;

import java.util.Map;

import bingo.odata.ODataResponseStatus;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.requests.MergeEntityRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.util.ODataConsumerContextHelper;
import bingo.odata.consumer.util.ODataMetadataVerifier;

public class MergeEntityHandler extends BaseHandler {

	public MergeEntityHandler(ODataConsumer consumer, ODataServices services,
			ODataMetadataVerifier verifier) {
		super(consumer, services, verifier);
	}
	
	public int mergeEntity(String entityType, Object key, Map<String, Object> updateFields) {
		
		if(config.isVerifyMetadata()) verifier.hasFields(entityType, updateFields);
		
		ODataConsumerContext context = ODataConsumerContextHelper
					.initEntityTypeContext(consumer, entityType, key);
		
		Request request = new MergeEntityRequest(context, config.getProducerUrl())
					.setEntitySet(context.getEntitySet().getName())
					.setId(context.getEntityKey().toKeyString(false)).setFields(updateFields);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.NoContent) {
			return 1;
		} else {
			throw response.convertToError(context);
		}
	}
}
