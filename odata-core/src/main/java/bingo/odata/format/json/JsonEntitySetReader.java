package bingo.odata.format.json;

import java.util.List;
import java.util.Map;

import bingo.lang.Collections;
import bingo.lang.Maps;
import bingo.lang.json.JSON;
import bingo.lang.json.JSONObject;
import bingo.odata.ODataErrors;
import bingo.odata.ODataReaderContext;
import bingo.odata.format.ODataJsonReader;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataEntitySetBuilder;

public class JsonEntitySetReader extends ODataJsonReader<ODataEntitySet> {

	@SuppressWarnings("unchecked")
	@Override
	protected ODataEntitySet read(ODataReaderContext context, JSONObject json) {
		if(json.isNull() || json.isArray()){
			throw ODataErrors.badRequest("invalid json content");
		}
		
		ODataEntitySetBuilder builder = new ODataEntitySetBuilder(context.getEntitySet(), context.getEntityType());

		Map<String,Object> map = json.map();
		List<Map<String, Object>> list = null;
		
		// return format 1
		if(2 == map.size() && Maps.containsKeyIgnoreCase(map, "odata.metadata")
							&& Maps.containsKeyIgnoreCase(map, "value")) {
			list = (List<Map<String, Object>>) map.get("value");
		}
		
		// return format 2
		if(Collections.isEmpty(list)) {
			if(map.size() == 1 && Maps.containsKeyIgnoreCase(map, "d")) {
				map = (Map<String,Object>)map.get("d");
			}

			if(map.size() == 1 && Maps.containsKeyIgnoreCase(map, "results")) {
				list = (List<Map<String,Object>>)map.get("results");
			}
		}

		
		if(!Collections.isEmpty(list)) {
			JsonEntityReader reader = new JsonEntityReader();
			for (Map<String, Object> itemMap : list) {
				JSONObject itemJson = JSON.decode(JSON.encode(itemMap));
				ODataEntity oDataEntity = reader.read(context, itemJson);
				builder.addEntity(oDataEntity);
			}
		}
		
		return builder.build();
	}

}
