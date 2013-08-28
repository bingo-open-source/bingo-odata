package bingo.odata.consumer.handlers;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import bingo.lang.Assert;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.util.ODataMetadataVerifier;

public class Handlers {
	private static Map<String, Object> handlers = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public static <T extends BaseHandler> T get(Class<T> clazz, ODataConsumer consumer) {
		String keyString = generateKey(consumer.getConfig().getProducerUrl(), clazz.getSimpleName()); 
		
		Object valueObject = handlers.get(keyString);
		
		if(null == valueObject) {
			try {
				Constructor<T> constructor = clazz.getDeclaredConstructor(ODataConsumer.class, 
												ODataServices.class, ODataMetadataVerifier.class);
				T t = constructor.newInstance(consumer, consumer.getServiceMetadata(), 
												new ODataMetadataVerifier(consumer.getServiceMetadata()));
				handlers.put(keyString, t);
				return t;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		return (T) valueObject;
	}
	
	private static String generateKey(String producerUrl, String handlerName) {
		Assert.notBlank(producerUrl);
		Assert.notBlank(handlerName);
		return producerUrl + "_" + handlerName;
	}
}
