package bingo.odata.consumer.util;

import bingo.lang.Strings;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.model.ODataKeyImpl;

public class ODataConsumerContextHelper {
	
	public static ODataConsumerContext initEntitySetContext(ODataConsumer consumer, String entitySet) {
		ODataConsumerContext context = new ODataConsumerContext(consumer.config());
		context.setEntitySet(consumer.services().findEntitySet(entitySet));
		return context;
	}
	
	public static ODataConsumerContext initEntityTypeContext(ODataConsumer consumer, String entityType) {
		return initEntityTypeContext(consumer, entityType, null);
	}

	public static ODataConsumerContext initEntityTypeContext(ODataConsumer consumer, String entityType, Object key) {
		ODataConsumerContext context = new ODataConsumerContext(consumer.config());
		context.setEntityType(consumer.services().findEntityType(entityType));
		context.setEntitySet(consumer.services().findEntitySet(context.getEntityType()));
		if(Strings.isBlank(key)){
			context.setEntityKey(new ODataKeyImpl(key));
		}
		return context;
	}
}
