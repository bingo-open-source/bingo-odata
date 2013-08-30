package bingo.odata.consumer.requests.builders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bingo.lang.Assert;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.odata.consumer.ODataConsumerImpl;
import bingo.odata.model.ODataValue;

public class FunctionInvokerImpl implements FunctionInvoker {
	
	private ODataConsumerImpl consumer;
	
	private String func;
	private String entity;
	private Map<String, Object> params = new HashMap<String, Object>();
	
	public FunctionInvokerImpl(ODataConsumerImpl oDataConsumerImpl) {
		this.consumer = oDataConsumerImpl;
	}

	public FunctionInvoker func(String funcName) {
		this.func = funcName;
		return this;
	}

	public FunctionInvoker entity(String name) {
		this.entity = name;
		return this;
	}

	public FunctionInvoker param(String name, Object value) {
		this.params.put(name, value);
		return this;
	}

	public FunctionInvoker params(Map<String, Object> params) {
		this.params.putAll(params);
		return this;
	}
	
	public void invoke() {
		consumer.invokeFunctionForVoid(func, params, 
				null == entity? null : getEntitySetName(entity));
	}

	public String invokeForStringResult() {
		return consumer.invokeFunctionForString(func, params, 
				null == entity? null : getEntitySetName(entity));
	}

	public <T> T invokeForTypeResult(Class<T> t) {
		return consumer.invokeFunctionForType(func, params, 
				null == entity? null : getEntitySetName(entity), t);
	}

	public <T> List<T> invokeForTypeListResult(Class<T> t) {
		return consumer.invokeFunctionForEntityList(func, params, 
				null == entity? null : getEntitySetName(entity), t);
	}

	public ODataValue invokeForODataValueResult() {
		return consumer.invokeFunctionForODataValue(func, params, 
				null == entity? null : getEntitySetName(entity));
	}

	private String getEntitySetName(String entity) {
		EdmEntityType entityType = consumer.getServiceMetadata().findEntityType(entity);
		Assert.notNull(entityType);
		EdmEntitySet entitySet = consumer.getServiceMetadata().findEntitySet(entityType);
		Assert.notNull(entitySet);
		return entitySet.getName();
	}
}
