package bingo.odata.consumer.requests.builders;

import java.util.List;
import java.util.Map;

import bingo.odata.model.ODataValue;

public interface FunctionInvoker {

	FunctionInvoker 			invoke(String funcName);
	
	FunctionInvoker 			entity(String name);
	
	FunctionInvoker 			param(String name, Object value);
	
	FunctionInvoker 			params(Map<String, Object> params);
	
	String							getStringResult();
	
	<T> T							getTypeResult(Class<T> t);
	
	<T> List<T>						getTypeListResult(Class<T> t);
	
	ODataValue						getODataValueResult();
}
