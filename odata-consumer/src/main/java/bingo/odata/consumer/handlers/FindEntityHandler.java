package bingo.odata.consumer.handlers;

import java.util.Map;

import bingo.lang.Converts;
import bingo.meta.edm.EdmEntitySet;
import bingo.odata.ODataResponseStatus;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.requests.FindEntityRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.util.ODataConsumerContextHelper;
import bingo.odata.consumer.util.ODataConvertor;
import bingo.odata.consumer.util.ODataMetadataVerifier;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataKey;

public class FindEntityHandler extends BaseHandler {

	public FindEntityHandler(ODataConsumer consumer, ODataServices services,
			ODataMetadataVerifier verifier) {
		super(consumer, services, verifier);
	}
	
	public ODataEntity findEntity(EdmEntitySet entitySet, ODataKey key) {
		Map<String, Object> entityMap = findEntity(entitySet.getName(), key.toKeyString(false));
		ODataConsumerContext context = ODataConsumerContextHelper.initContext(
								consumer, entitySet.getName(), null, key.toKeyString(false));
		return ODataConvertor.convertTo(ODataEntity.class, context, entityMap);
	}
	
	public <T> T findEntity(Class<T> clazz, String entityType, Object key) {
		Map<String, Object> entityMap = findEntity(entityType, key);
		return Converts.convert(entityMap, clazz);
	}
	
	public Map<String, Object> findEntity(String entityType, Object key) {
		
		if(config.isVerifyMetadata()) verifier.hasEntityType(entityType);
		
		ODataConsumerContext context = ODataConsumerContextHelper
					.initEntityTypeContext(consumer, entityType, key);
		
		Request request = new FindEntityRequest(context, config.getProducerUrl())
					.setEntitySet(context.getEntitySet().getName())
					.setId(context.getEntityKey().toKeyString(false));
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToEntityMap(context);
			
		} else throw response.convertToError(context);
	}
}
