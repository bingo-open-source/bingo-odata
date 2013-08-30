package bingo.odata.consumer.handlers;

import java.util.List;
import java.util.Map;

import bingo.meta.edm.EdmFunctionImport;
import bingo.odata.ODataResponseStatus;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumer;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.requests.FunctionRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.util.ODataConsumerContextHelper;
import bingo.odata.consumer.util.ODataMetadataVerifier;
import bingo.odata.model.ODataValue;

public class InvokeFunctionHandler extends BaseHandler {

	public InvokeFunctionHandler(ODataConsumer consumer,
			ODataServices services, ODataMetadataVerifier verifier) {
		super(consumer, services, verifier);
	}
	
	public String forRawResult(String funcName, Map<String, Object> parameters, String entitySet) {
		
		EdmFunctionImport func = verifier.hasFunction(entitySet, funcName);

		ODataConsumerContext context = ODataConsumerContextHelper
					.initFunctionContext(consumer, func, entitySet);
		
		Request request = new FunctionRequest(context, config.getProducerUrl())
					.setHttpMethod(null == func? null:func.getHttpMethod())
					.setEntitySet(entitySet).setFunction(funcName).setFuncParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.getString();
			
		} else throw response.convertToError(context);
	}
	
	public <T> T forEntity(String funcName, Map<String, Object> parameters, String entitySet, Class<T> clazz) {
		
		EdmFunctionImport func = verifier.hasFunction(entitySet, funcName);

		ODataConsumerContext context = ODataConsumerContextHelper
					.initFunctionContext(consumer, func, entitySet);
		
		Request request = new FunctionRequest(context, config.getProducerUrl()).setHttpMethod(func.getHttpMethod())
					.setEntitySet(entitySet).setFunction(funcName).setFuncParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToObject(clazz, func.getReturnType(), context);
			
		} else throw response.convertToError(context);
	}
	
	public ODataValue forODataValue(String funcName, Map<String, Object> parameters, String entitySet) {
		
		EdmFunctionImport func = verifier.hasFunction(entitySet, funcName);

		ODataConsumerContext context = ODataConsumerContextHelper
					.initFunctionContext(consumer, func, entitySet);
		
		Request request = new FunctionRequest(context, config.getProducerUrl()).setHttpMethod(func.getHttpMethod())
					.setEntitySet(entitySet).setFunction(funcName).setFuncParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToODataValue(func.getReturnType(), context);
			
		} else throw response.convertToError(context);
	}
	
	public void forVoid(String funcName, Map<String, Object> parameters, String entitySet) {
		
		EdmFunctionImport func = verifier.hasFunction(entitySet, funcName);
		
		ODataConsumerContext context = ODataConsumerContextHelper
				.initFunctionContext(consumer, func, entitySet);
		
		Request request = new FunctionRequest(context, config.getProducerUrl()).setHttpMethod(func.getHttpMethod())
				.setEntitySet(entitySet).setFunction(funcName).setFuncParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK 
				|| response.getStatus() == ODataResponseStatus.NoContent) {
			
			return;
			
		} else throw response.convertToError(context);
	}
	
	public <T> List<T> forEntityList(String funcName,
			Map<String, Object> parameters, String entitySet, Class<T> listClass) {
		
		EdmFunctionImport func = verifier.hasFunction(entitySet, funcName);

		ODataConsumerContext context = ODataConsumerContextHelper
					.initFunctionContext(consumer, func, entitySet);
	
		Request request = new FunctionRequest(context, config.getProducerUrl()).setHttpMethod(func.getHttpMethod())
					.setEntitySet(entitySet).setFunction(funcName).setFuncParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToObjectList(listClass, func.getReturnType(), context);
			
		} else throw response.convertToError(context);
	}
	
	public String forString(String funcName, Map<String, Object> parameters, String entitySet) {
		
		EdmFunctionImport func = null;
		
		if(config.isVerifyMetadata()) func = verifier.hasFunction(entitySet, funcName);
		
		ODataConsumerContext context = ODataConsumerContextHelper
					.initFunctionContext(consumer, func, entitySet);
		
		Request request = new FunctionRequest(context, config.getProducerUrl())
					.setHttpMethod(null == func? null:func.getHttpMethod())
					.setEntitySet(entitySet).setFunction(funcName).setFuncParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToString(context);
			
		} else throw response.convertToError(context);
	}
}
