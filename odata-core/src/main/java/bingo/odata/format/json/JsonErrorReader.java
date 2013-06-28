package bingo.odata.format.json;

import java.io.Reader;
import java.util.Map;

import bingo.lang.Objects;
import bingo.lang.json.JSONObject;
import bingo.odata.ODataError;
import bingo.odata.ODataErrors;
import bingo.odata.ODataReader;
import bingo.odata.ODataReaderContext;
import bingo.odata.format.ODataJsonReader;

public class JsonErrorReader extends ODataJsonReader<ODataError> {

	@Override
	protected ODataError read(ODataReaderContext context, JSONObject json) {
		if(json.isNull() || json.isArray()){
			throw ODataErrors.badRequest("invalid json content");
		}
		
		Map<String,Object> map = json.map();
		
		if(map.entrySet().size() == 1 && map.keySet().iterator().next().equals("error")) {
			map = (Map<String,Object>)map.get("error");
		} else return null;
		
		ODataError error = new ODataError(Objects.toString(map.get("code")),
											Objects.toString(map.get("message")));
		return error;
	}
}