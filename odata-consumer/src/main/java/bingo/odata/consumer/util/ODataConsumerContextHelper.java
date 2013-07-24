package bingo.odata.consumer.util;

import bingo.lang.Strings;
import bingo.meta.edm.EdmCollectionType;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmType;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.model.ODataKeyImpl;

public class ODataConsumerContextHelper {
	
	public static ODataConsumerContext initEntitySetContext(ODataConsumer consumer, String entitySet) {
		return initContext(consumer, entitySet, null, null);
	}
	
	public static ODataConsumerContext initEntityTypeContext(ODataConsumer consumer, String entityType) {
		return initEntityTypeContext(consumer, entityType, null);
	}
	
	public static ODataConsumerContext initEntityTypeContext(ODataConsumer consumer, String entityType, Object key) {
		return initContext(consumer, null, entityType, key);
	}
	
	public static ODataConsumerContext initContext(ODataConsumer consumer, String entitySet, String entityType, 
				Object key) {
		ODataConsumerContext context = new ODataConsumerContext(consumer.config());
		
		if(Strings.isNotBlank(entityType)) {
			context.setEntityType(consumer.cachedGetServiceMetadata().findEntityType(entityType));
			context.setEntitySet(consumer.cachedGetServiceMetadata().findEntitySet(context.getEntityType()));
		} else if(Strings.isNotBlank(entitySet)) {
			context.setEntitySet(consumer.cachedGetServiceMetadata().findEntitySet(entitySet));
			context.setEntityType(consumer.cachedGetServiceMetadata().findEntityType(context.getEntitySet().getEntityType().getName()));
		}
		
		if(!Strings.isBlank(key)){
			context.setEntityKey(new ODataKeyImpl(key));
		}
		
		return context;
	}
	
	public static ODataConsumerContext initFunctionContext(ODataConsumer consumer, EdmFunctionImport func, String entitySet) {

		ODataConsumerContext context = new ODataConsumerContext(consumer.config());

		initFunctionContext(consumer.cachedGetServiceMetadata(), context, func, entitySet);
		
		return context;
	}
	
	private static void initFunctionContext(ODataServices services, ODataConsumerContext context, EdmFunctionImport func, String entitySet) {
		context.setFunctionImport(func);
		
		if(Strings.isBlank(entitySet)) {
			entitySet = func.getEntitySet();
		}
		
		if(Strings.isNotBlank(entitySet)) {
			EdmEntitySet edmEntitySet = services.findEntitySet(entitySet);
			
			EdmEntityType edmEntityType = services.findEntityType(edmEntitySet.getEntityType());
			
			context.setEntityType(edmEntityType);
			context.setEntitySet(edmEntitySet);
		}
	}

	private static EdmEntityType tryGetEntityTypeFromEdmType(EdmType returnType) {
		if(returnType.isCollection()) {
			EdmCollectionType collectionType = returnType.asCollection();
			EdmType eleEdmType = collectionType.getElementType();
			return tryGetEntityTypeFromEdmType(eleEdmType);
		}
		if(returnType.isEntity()) {
			EdmEntityType entityType = returnType.asEntity();
			return entityType;
		}
		return null;
	}
}
