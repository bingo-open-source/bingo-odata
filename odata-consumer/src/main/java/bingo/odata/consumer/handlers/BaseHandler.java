package bingo.odata.consumer.handlers;

import bingo.lang.Assert;
import bingo.lang.Strings;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerConfig;
import bingo.odata.consumer.util.ODataMetadataVerifier;

public class BaseHandler {
	protected ODataConsumer 			consumer;
	protected ODataServices			services;
	protected ODataMetadataVerifier 	verifier;
	protected ODataConsumerConfig		config;

	public BaseHandler(ODataConsumer consumer
								, ODataServices services
								, ODataMetadataVerifier verifier) {
		Assert.notNull(consumer);
		Assert.notNull(services);
		Assert.notNull(verifier);
		this.consumer = consumer;
		this.services = services;
		this.verifier = verifier;
		this.config = consumer.config();
	}
	
	protected BaseHandler(ODataConsumer consumer) {
		this.consumer = consumer;
		this.config = consumer.config();
	}
	
	protected String qualifiedKey(Object key) {
		if(Strings.startsWith(key.toString(), "'") && Strings.endsWith(key.toString(), "'")) return key.toString();
		return "'" + key + "'";
	}
}
