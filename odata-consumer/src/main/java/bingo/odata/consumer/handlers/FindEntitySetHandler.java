package bingo.odata.consumer.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bingo.lang.Converts;
import bingo.lang.Strings;
import bingo.meta.edm.EdmEntitySet;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataQueryInfoParser;
import bingo.odata.ODataResponseStatus;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.ext.Page;
import bingo.odata.consumer.requests.FindEntitySetRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.util.ODataConsumerContextHelper;
import bingo.odata.consumer.util.ODataConvertor;
import bingo.odata.consumer.util.ODataMetadataVerifier;
import bingo.odata.consumer.util.ODataQueryTranslator;
import bingo.odata.expression.BoolExpression;
import bingo.odata.expression.EntitySimpleProperty;
import bingo.odata.expression.OrderByExpression;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;

public class FindEntitySetHandler extends BaseHandler {

	public FindEntitySetHandler(ODataConsumer consumer, ODataServices services,
			ODataMetadataVerifier verifier) {
		super(consumer, services, verifier);
	}

	public <T> List<T> findEntitySet(Class<T> clazz, String entitySet, String where,
			Object params, String orderBy, String[] fields, String[] expand, Page page) {
		List<Map<String, Object>> list = findEntitySet(entitySet, where, params, orderBy, fields, expand, page);
		List<T> tList = new ArrayList<T>();
		for (Map<String, Object> map : list) {
			T t = Converts.convert(map, clazz);
			if(null != t) tList.add(t);
		}
		return tList;
	}
	public List<Map<String, Object>> findEntitySet(String entitySet, String where,
			Object params, String orderBy, String[] fields, String[] expand, Page page) {
		
		String whereParamed = Strings.isBlank(where)? 
									null : ODataQueryTranslator.translateFilter(where, params, false);
		
		BoolExpression filter = Strings.isBlank(whereParamed)? 
									null : ODataQueryInfoParser.parseFilter(whereParamed);
		
		List<OrderByExpression> orderByExpressions = Strings.isBlank(orderBy)?
									null : ODataQueryInfoParser.parseOrderBy(orderBy);
		 
		List<EntitySimpleProperty> select = null == fields || fields.length == 0?
									null : ODataQueryInfoParser.parseSelect(Strings.join(fields, ","));
		
		List<EntitySimpleProperty> expandList = null == expand || expand.length == 0?
									null : ODataQueryInfoParser.parseExpand(Strings.join(expand, ","));
		
		ODataQueryInfo queryInfo = new ODataQueryInfo(expandList, filter, orderByExpressions
				, null == page? null : page.getSkip(), null == page? null : page.getTop()
				, null, null, select, null);
		
		ODataEntitySet oDataEntitySet = findEntitySet(services.findEntitySet(entitySet), queryInfo);
		
		List<ODataEntity> entities = oDataEntitySet.getEntities().toList();
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		for (ODataEntity entity : entities) {
			list.add(entity.toMap());
		}
		
		return list;
	}
	
	public ODataEntitySet findEntitySet(EdmEntitySet entitySet,	ODataQueryInfo queryInfo) {
		String string = findEntitySet(entitySet.getName(), ODataQueryInfoParser.toQueryString(queryInfo));
		
		return ODataConvertor.convertTo(ODataEntitySet.class, ODataConsumerContextHelper
				.initEntitySetContext(consumer, entitySet.getName()), string);
	}
	
	public String findEntitySet(String entitySet,	String query) {
		
		if(config.isVerifyMetadata()) verifier.hasEntitySet(entitySet);
		
		ODataConsumerContext context = ODataConsumerContextHelper
				.initEntitySetContext(consumer, entitySet);
		
		Request request = new FindEntitySetRequest(context, config.getProducerUrl())
				.setEntitySet(entitySet);
		
		if(Strings.isNotBlank(query)) request.addAdditionalQueryString(query);
		
		Response response = request.send();

		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.getString();
			
		} else throw response.convertToError(context);
	}
}
