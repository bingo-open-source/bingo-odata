package bingo.odata.consumer.requests.builders;

import java.util.List;
import java.util.Map;

import bingo.odata.model.ODataValue;

public interface FunctionInvoker {
	
	FunctionInvoker				func(String name);
	
	FunctionInvoker 			entity(String name);
	
	FunctionInvoker 			param(String name, Object value);
	
	FunctionInvoker 			params(Map<String, Object> params);
	
	void 						invoke();
	
	String						invokeForStringResult();
	
	<T> T						invokeForTypeResult(Class<T> t);
	
	<T> List<T>					invokeForTypeListResult(Class<T> t);
	
	ODataValue					invokeForODataValueResult();
}
