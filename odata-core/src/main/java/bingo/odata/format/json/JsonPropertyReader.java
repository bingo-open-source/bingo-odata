package bingo.odata.format.json;

import java.util.Map;

import bingo.lang.Maps;
import bingo.lang.json.JSONObject;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmProperty;
import bingo.odata.ODataError;
import bingo.odata.ODataErrors;
import bingo.odata.ODataReaderContext;
import bingo.odata.format.ODataJsonReader;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataPropertyBuilder;
import bingo.odata.model.ODataPropertyImpl;

public class JsonPropertyReader extends ODataJsonReader<ODataProperty> {

	@Override
	protected ODataProperty read(ODataReaderContext context, JSONObject json) {
		if(json.isNull() || json.isArray()){
			throw ODataErrors.badRequest("invalid json content");
		}

		Map<String,Object> map = json.map();
		
		if(map.size() == 1 && Maps.containsKeyIgnoreCase(map, "d")) {
			map = (Map<String,Object>)map.get("d");
		}
		
		if(map.size() == 1) {
			String propName = map.keySet().iterator().next();
			
			EdmEntityType edmEntityType = context.getEntityType();
			
			EdmProperty edmProperty = edmEntityType.findProperty(propName);
			
			return new ODataPropertyImpl(edmProperty, map.get(propName));
			
		} else throw ODataErrors.badRequest("invalid json content");
	}

}
