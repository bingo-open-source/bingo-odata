package bingo.odata.consumer.handlers;

import java.util.Map;

import bingo.lang.Strings;
import bingo.meta.edm.EdmEntitySet;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataQueryInfoParser;
import bingo.odata.ODataResponseStatus;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.ext.Page;
import bingo.odata.consumer.requests.CountRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.util.ODataConsumerContextHelper;
import bingo.odata.consumer.util.ODataMetadataVerifier;
import bingo.odata.consumer.util.ODataQueryTranslator;
import bingo.odata.expression.BoolExpression;

public class CountHandler extends BaseHandler {

	public CountHandler(ODataConsumer consumer, ODataServices services,
			ODataMetadataVerifier verifier) {
		super(consumer, services, verifier);
	}
	
	public long count(String entitySet, String where, Object params, Page page) {
		
		String whereParamed = Strings.isBlank(where)? 
									null : ODataQueryTranslator.translateFilter(where, params, false);
		
		BoolExpression filter = Strings.isBlank(whereParamed)? 
									null : ODataQueryInfoParser.parseFilter(whereParamed);
		
		ODataQueryInfo queryInfo = new ODataQueryInfo(null, filter, null
				, null == page? null : page.getSkip(), null == page? null : page.getTop()
				, null, null, null, null);
		
		return count(services.findEntitySet(entitySet), queryInfo);
		
	}
	
	public long count(EdmEntitySet entitySet,	ODataQueryInfo queryInfo) {
		return count(entitySet.getName(), ODataQueryInfoParser.toQueryString(queryInfo));
	}
	
	public long count(String entitySet, String query) {
		
		if(config.isVerifyMetadata()) verifier.hasEntitySet(entitySet);
		
		ODataConsumerContext context = ODataConsumerContextHelper
				.initEntitySetContext(consumer, entitySet);
		
		Request request = new CountRequest(context, config.getProducerUrl())
				.setEntitySet(entitySet);

		if(Strings.isNotBlank(query)) request.addAdditionalQueryString(query);
		
		Response resp = request.send();
		
		if(resp.getStatus() == ODataResponseStatus.OK) {
			
			return resp.convertToRawLong(context);
			
		} else throw resp.convertToError(context);
	}
}
