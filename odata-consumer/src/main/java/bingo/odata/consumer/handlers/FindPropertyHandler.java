package bingo.odata.consumer.handlers;

import java.util.Map;

import bingo.lang.Converts;
import bingo.lang.Strings;
import bingo.lang.json.JSON;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmProperty;
import bingo.odata.ODataResponseStatus;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.requests.FindPropertyRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.util.ODataConsumerContextHelper;
import bingo.odata.consumer.util.ODataConvertor;
import bingo.odata.consumer.util.ODataMetadataVerifier;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataProperty;

public class FindPropertyHandler extends BaseHandler {

	public FindPropertyHandler(ODataConsumer consumer, ODataServices services,
			ODataMetadataVerifier verifier) {
		super(consumer, services, verifier);
	}
	
	public <T> T findProperty(String entityType, Object key, String property, Class<T> clazz) {
		String string = findPropertyForString(entityType, key, property);
		
		if(Strings.startsWith(string, "{") && Strings.endsWith(string, "}")) {
			Map<String, Object> map = JSON.decodeToMap(string);
			return Converts.convert(map, clazz);
		} else {
			return Converts.convert(string, clazz);
		}
	}
	
	public ODataProperty findProperty(EdmEntityType entityType, ODataKey key, 
			EdmProperty property) {
		
		String string = findPropertyForString(entityType.getName(), 
										key.toKeyString(false), property.getName());
		
		return ODataConvertor.convertTo(ODataProperty.class, ODataConsumerContextHelper
				.initEntityTypeContext(consumer, entityType.getName(), key.toKeyString(false)), string);
	}
	
	public String findPropertyForString(String entityType, Object key, String property) {
		
		if(config.isVerifyMetadata()) verifier.hasEntityType(entityType, property);
		
		ODataConsumerContext context = ODataConsumerContextHelper
				.initEntityTypeContext(consumer, entityType, key);
		
		Request request = new FindPropertyRequest(context, config.getProducerUrl())
				.setEntitySet(context.getEntitySet().getName())
				.setId(context.getEntityKey().toKeyString(false)).setProperty(property);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToProperty(context).getValue().toString();
			
		} else throw response.convertToError(context);
	}
}
