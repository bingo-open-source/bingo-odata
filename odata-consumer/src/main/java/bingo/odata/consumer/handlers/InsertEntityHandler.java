package bingo.odata.consumer.handlers;

import java.util.Map;

import bingo.odata.ODataResponseStatus;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.requests.InsertEntityRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.util.ODataConsumerContextHelper;
import bingo.odata.consumer.util.ODataMetadataVerifier;

public class InsertEntityHandler extends BaseHandler{
	
	public InsertEntityHandler(ODataConsumer consumer
								, ODataServices services
								, ODataMetadataVerifier verifier) {
		super(consumer, services, verifier);
	}
	
	public Map<String, Object> insertEntityByMap(String entityType, Map<String, Object> object) {
		
		if(config.isVerifyMetadata()) verifier.hasFields(entityType, object);
		
		ODataConsumerContext context = ODataConsumerContextHelper.initEntityTypeContext(consumer, entityType);
		
		Request request = new InsertEntityRequest(context, config.getProducerUrl())
							.setEntityType(context.getEntitySet().getName())
							.setEntity(object);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.Created) {
			return response.convertToEntity(context).toMap();
		} else {
			throw response.convertToError(context);
		}
	}
	
}
