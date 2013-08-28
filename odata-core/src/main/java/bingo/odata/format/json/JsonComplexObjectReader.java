package bingo.odata.format.json;

import bingo.lang.exceptions.NotImplementedException;
import bingo.lang.json.JSONObject;
import bingo.odata.ODataReaderContext;
import bingo.odata.format.ODataJsonReader;
import bingo.odata.model.ODataComplexObject;

public class JsonComplexObjectReader extends ODataJsonReader<ODataComplexObject> {

	@Override
	protected ODataComplexObject read(ODataReaderContext context, JSONObject json) {
		throw new NotImplementedException();
	}
}
