package bingo.odata.consumer.handlers;

import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.exceptions.ConnectFailedException;
import bingo.odata.consumer.requests.MetadataRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.util.ODataMetadataVerifier;

public class RetrieveServiceMetadataHandler extends BaseHandler {
	private static final Log log = LogFactory.get(RetrieveServiceMetadataHandler.class);

	public RetrieveServiceMetadataHandler(ODataConsumer consumer) {
		super(consumer);
	}
	
	public ODataServices retrieveServiceMetadata() throws ConnectFailedException{
		ODataConsumerContext context = new ODataConsumerContext(config);
		
		Request request = new MetadataRequest(context, this.config.getProducerUrl());
		
//		request.setLog(false);
		
		Response resp = request.send();
		
		if(resp.getStatus() == 200) {
			ODataServices services = ODataServices.parse(resp.getInputStream());
			
			if(null != services) {
				this.services = services;
				this.consumer.services(services);
				this.verifier = new ODataMetadataVerifier(services);
			}
			
			log.info("Retrieve Metadata Document successfully!");
			
			return services;
			
		} else throw resp.convertToError(context);
	}
}
