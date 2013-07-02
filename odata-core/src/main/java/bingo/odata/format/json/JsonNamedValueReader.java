package bingo.odata.format.json;

import java.util.Map;

import bingo.lang.Objects;
import bingo.lang.Strings;
import bingo.lang.json.JSONObject;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmProperty;
import bingo.meta.edm.EdmSimpleType;
import bingo.meta.edm.EdmType;
import bingo.odata.ODataConverts;
import bingo.odata.ODataError;
import bingo.odata.ODataErrors;
import bingo.odata.ODataObject;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataReaderContext;
import bingo.odata.format.ODataJsonReader;
import bingo.odata.model.ODataNamedValue;
import bingo.odata.model.ODataNamedValueImpl;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataPropertyBuilder;
import bingo.odata.model.ODataPropertyImpl;
import bingo.odata.model.ODataRawValueImpl;

public class JsonNamedValueReader extends ODataJsonReader<ODataNamedValue> {

	@Override
	protected ODataNamedValue read(ODataReaderContext context, JSONObject json) {
		if(json.isNull() || json.isArray()){
			throw ODataErrors.badRequest("invalid json content");
		}

		Map<String,Object> map = json.map();
		
		if(map.entrySet().size() == 1 && map.keySet().iterator().next().equals("d")) {
			map = (Map<String,Object>)map.get("d");
		}
		
		if(map.size() == 1) {
			String name = map.keySet().iterator().next();
			
			Object value = map.get(name);
			
			EdmFunctionImport functionImport = context.getFunctionImport();
			
			if(null != functionImport && Strings.equals(name, functionImport.getName())) {
				
				EdmType returnType = functionImport.getReturnType();
				
				if(returnType.isSimple()) {
					
					EdmSimpleType returnSimpleType = returnType.asSimple();
					
					ODataObject oDataObject = new ODataRawValueImpl(returnSimpleType, value);
					
					ODataNamedValue namedValue = new ODataNamedValueImpl(name, ODataObjectKind.NamedValue, oDataObject);
					
					return namedValue;
				}
			}
		}
		throw ODataErrors.badRequest("invalid json content");
	}

}
