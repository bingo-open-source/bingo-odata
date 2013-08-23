package bingo.odata.consumer.handlers;

import bingo.odata.ODataResponseStatus;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.requests.DeleteEntityRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.util.ODataConsumerContextHelper;
import bingo.odata.consumer.util.ODataMetadataVerifier;

public class DeleteEntityHandler extends BaseHandler {

	public DeleteEntityHandler(ODataConsumer consumer, ODataServices services,
			ODataMetadataVerifier verifier) {
		super(consumer, services, verifier);
	}
	
	public boolean deleteEntity(String entityType, Object key) {
		
		if(config.isVerifyMetadata()) verifier.hasEntityType(entityType);
		
		ODataConsumerContext context = ODataConsumerContextHelper
										.initEntityTypeContext(consumer, entityType, key);
		
		Request request = new DeleteEntityRequest(context, config.getProducerUrl())
								.setEntitySet(context.getEntitySet().getName())
								.setId(context.getEntityKey().toKeyString(false));
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			return true;
		} else {
			throw response.convertToError(context);
		}
	}
}
