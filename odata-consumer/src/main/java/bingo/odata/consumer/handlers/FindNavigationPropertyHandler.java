package bingo.odata.consumer.handlers;

import java.util.Map;

import bingo.lang.Converts;
import bingo.lang.Strings;
import bingo.lang.json.JSON;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmNavigationProperty;
import bingo.meta.edm.EdmProperty;
import bingo.odata.ODataResponseStatus;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.requests.FindNavigationPropertyRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.util.ODataConsumerContextHelper;
import bingo.odata.consumer.util.ODataConvertor;
import bingo.odata.consumer.util.ODataMetadataVerifier;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataNavigationProperty;
import bingo.odata.model.ODataProperty;

public class FindNavigationPropertyHandler extends BaseHandler {

	public FindNavigationPropertyHandler(ODataConsumer consumer, ODataServices services,
			ODataMetadataVerifier verifier) {
		super(consumer, services, verifier);
	}
	
	public <T> T findNavigationProperty(String entityType, Object key, String navigationProperty, Class<T> clazz) {
		String string = findNavigationPropertyForString(entityType, key, navigationProperty);
		
		if(Strings.startsWith(string, "{") && Strings.endsWith(string, "}")) {
			Map<String, Object> map = JSON.decodeToMap(string);
			return Converts.convert(map, clazz);
		} else {
			return Converts.convert(string, clazz);
		}
	}
	
	public ODataNavigationProperty findNavigationProperty(EdmEntityType entityType, ODataKey key, 
			EdmNavigationProperty property) {
		
		String string = findNavigationPropertyForString(entityType.getName(), 
										key.toKeyString(false), property.getName());
		
		return ODataConvertor.convertTo(ODataNavigationProperty.class, ODataConsumerContextHelper
				.initEntityTypeContext(consumer, entityType.getName(), key.toKeyString(false)), string);
	}
	
	public String findNavigationPropertyForString(String entityType, Object key, String property) {
		
		if(config.isVerifyMetadata()) verifier.hasEntityType(entityType, property);
		
		ODataConsumerContext context = ODataConsumerContextHelper
				.initEntityTypeContext(consumer, entityType, key);
		
		Request request = new FindNavigationPropertyRequest(context, config.getProducerUrl())
				.setEntitySet(context.getEntitySet().getName())
				.setId(qualifiedKey(key)).setNaviProperty(property);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToString(context);
			
		} else throw response.convertToError(context);
	}
}
