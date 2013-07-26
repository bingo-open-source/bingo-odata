package bingo.odata.consumer.requests;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import bingo.lang.Assert;
import bingo.lang.Converts;
import bingo.lang.Enumerables;
import bingo.lang.Enums;
import bingo.lang.Func1;
import bingo.lang.Maps;
import bingo.lang.Strings;
import bingo.lang.convert.InputStreamConverter;
import bingo.lang.exceptions.NotImplementedException;
import bingo.lang.http.HttpContentTypes;
import bingo.lang.json.JSON;
import bingo.lang.json.JSONObject;
import bingo.meta.edm.EdmSimpleType;
import bingo.meta.edm.EdmType;
import bingo.meta.edm.EdmTypeKind;
import bingo.odata.ODataConverts;
import bingo.odata.ODataError;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataReader;
import bingo.odata.ODataReaderContext;
import bingo.odata.ODataConstants.ContentTypes;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.exceptions.ResolveFailedException;
import bingo.odata.consumer.ext.ODataDateConvertor;
import bingo.odata.consumer.util.ODataConvertor;
import bingo.odata.consumer.util.OdataJudger;
import bingo.odata.model.ODataComplexObject;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntityBuilder;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataNamedValue;
import bingo.odata.model.ODataNavigationProperty;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataRawValue;
import bingo.odata.model.ODataRawValueImpl;
import bingo.odata.model.ODataValue;
import bingo.odata.model.ODataValueBuilder;

import com.google.api.client.http.HttpResponse;

public class Response {
	private HttpResponse httpResponse;
	private String responseBody;
	
	static {
		Converts.register(Date.class, new ODataDateConvertor());
	}
	
	public Response(HttpResponse httpResponse) {
		Assert.notNull(httpResponse);
		this.httpResponse = httpResponse;
	}

	public String getContentType() {
		return httpResponse.getContentType();
	}

	public int getStatus() {
		return httpResponse.getStatusCode();
	}

	public Object getHeader(String name) {
		return httpResponse.getHeaders().get(name);
	}

	public InputStream getInputStream() {
		try {
			if(null != responseBody) {
				return new ByteArrayInputStream(responseBody.getBytes());
			}
			return httpResponse.getContent();
		} catch (IOException e) {
			throw new ResolveFailedException(e);
		}
	}
	
	public String getString() {
		try {
			if(null != responseBody) {
				return responseBody;
			}
			return httpResponse.parseAsString();
		} catch (IOException e) {
			throw new ResolveFailedException(e);
		}
	}

	public String toString() {
		return toString(false);
	}
	public String toString(boolean showBody) {
		String blank = " ", next = "\n\t";
		StringBuilder builder = new StringBuilder(next)
					.append(next).append(httpResponse.getRequest().getRequestMethod())
					.append(blank).append(httpResponse.getRequest().getUrl().toString())
					.append(blank).append(httpResponse.getStatusCode())
					.append(blank).append(httpResponse.getStatusMessage())
					.append(next);
		builder.append(httpResponse.getHeaders().toString()).append(next);
		if(showBody) {
			try {
				if(Strings.isBlank(responseBody)) {
					responseBody = new InputStreamConverter().convertToString(httpResponse.getContent());
				}
				builder.append(responseBody);
			} catch (Throwable e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return builder.append(next).toString();
	}
	
	public <T> T convertToObject(Class<T> clazz, EdmType edmType, ODataConsumerContext context) {
		Assert.notNull(clazz);
		Assert.notNull(context);
		if(null != context.getFunctionImport()) {
			// through function invoking.
			// get ride of useless info.
			String jsonString = this.getString(), funcName = context.getFunctionImport().getName();
			Object object = extractValueFromFunctionResponse(jsonString, funcName);
			if(object instanceof Map) {
				Map<String, Object> jsonMap = (Map<String, Object>) object;
				if(edmType.isEntity() || edmType.isEntityRef()) {
					// normal entity return type.
					return convertToEntity(clazz, context, JSON.encode(jsonMap));
				} else { 
					// complex object return type.
					// and other type like int, boolean.
					return Converts.convert(jsonMap, clazz);
				}
			} else {
				return Converts.convert(object, clazz);
			}
		} else {
			// normal retrieve entity.
			return convertToEntity(clazz, context, this.getString());
		}
	}
	
	public static Object extractValueFromFunctionResponse(String jsonString, String funcName) {
		Assert.notBlank(jsonString);
		Assert.notBlank(funcName);
		JSONObject jsonObject = JSON.decode(jsonString);
		Map<String, Object> jsonMap = null;
		if(jsonObject.isMap()) {
			jsonMap = jsonObject.map();
			if(1 == jsonMap.size() && Maps.containsKeyIgnoreCase(jsonMap, "d")) {
				jsonMap = (Map<String, Object>) jsonMap.get("d");
			}
			if(1 == jsonMap.size() && Maps.containsKeyIgnoreCase(jsonMap, funcName)) {
				Object tempObject = jsonMap.get(funcName);
				if(tempObject instanceof Map) {
					 Object tempObject2 = ((Map<String, Object>)tempObject).get("results");
					 if(null != tempObject2 && ((Map<String, Object>)tempObject).size() == 1) {
						 return tempObject2;
					 }
				}
				return tempObject;
			}
			
			// compatibility for some odata producer.
			if(2 == jsonMap.size() && Maps.containsKeyIgnoreCase(jsonMap, "value")) {
				return jsonMap.get("value");
			}
		}
		throw new ResolveFailedException("Return Type");
	}	
	public static Map<String, Object> extractValueFromFunctionResponseForMap(String jsonString, String funcName) {
		return (Map<String, Object>) extractValueFromFunctionResponse(jsonString, funcName);
	}	
	public static List<Map<String, Object>> extractValueFromFunctionResponseForList(String jsonString, String funcName) {
		return (List<Map<String, Object>>) extractValueFromFunctionResponse(jsonString, funcName);
	}
	
	private <T> T convertToEntity(Class<T> clazz, ODataConsumerContext context, String string) {
		ODataEntity entity = ODataConvertor.convertTo(ODataEntity.class, context, string);
		return ODataConvertor.convertEntityToType(entity, clazz);
	}
	
	public <T> List<T> convertToObjectList(Class<T> listClazz, EdmType edmType, ODataConsumerContext context) {
		Assert.notNull(listClazz);
		Assert.notNull(context);
		if(null != context.getFunctionImport()) {
			// through function invoking.
			// get ride of useless info.
			String jsonString = this.getString(), funcName = context.getFunctionImport().getName();
			List<Map<String, Object>> listMap = extractValueFromFunctionResponseForList(jsonString, funcName);
			if(edmType.isComplex()
					|| edmType.getTypeKind() == EdmTypeKind.Reference
					|| (edmType.isCollection() && edmType.asCollection().getElementType().getTypeKind() == EdmTypeKind.Reference)) {
				// complex object return type.
				return ODataConvertor.convertMapListToTypeList(listMap, listClazz);
			} else { 
				// normal entity return type.
				return convertToEntitySet(listClazz, context, JSON.encode(listMap));
			}
		} else {
			// normal retrieve entity.
			return convertToEntitySet(listClazz, context, this.getString());
		}
	}
	private <T> List<T> convertToEntitySet(Class<T> listClazz, ODataConsumerContext context, String str) {
		ODataEntitySet entitySet = ODataConvertor.convertTo(ODataEntitySet.class, context, getString());
		return ODataConvertor.convertEntitySetToTypeList(entitySet, listClazz);
	}
	
	public ODataValue convertToODataValue(EdmType edmType, ODataConsumerContext context) {
		Assert.notNull(edmType);
		if(null != context.getFunctionImport()) {
			if(OdataJudger.isEntity(edmType)) {
				Map<String, Object> json = extractValueFromFunctionResponseForMap(
						getString(), context.getFunctionImport().getName());
				return ODataConvertor.convertToODataValue(ODataEntity.class, context, json);
			}
		}
		ODataValueBuilder builder = new ODataValueBuilder();
		try {
			if(edmType.isCollection()) {
				builder.entitySet(this.convertToEntitySet(context));
			} else if(edmType.isEntity() || edmType.isEntityRef()) {
				builder.entity(this.convertToEntity(context));
			} else if(edmType.isComplex()) {
				// TODO 
			} else if(edmType.isSimple()) {
				// TODO
			}
		} catch (Throwable e) {
			throw new ResolveFailedException(e);
		}
		return builder.build();
	}
	
	public ODataError convertToError(ODataConsumerContext context) {
		ODataError error = ODataConvertor.convertTo(ODataError.class, context, getString());
		error.setStatus(this.getStatus());
		return error;
	}
	
	public String convertToString(ODataConsumerContext context) {
		ODataNamedValue namedValue = ODataConvertor.convertTo(ODataNamedValue.class, context, getString());
		return ((ODataRawValueImpl)namedValue.getValue()).getValue().toString();
	}
	
	public ODataEntity convertToEntity(ODataConsumerContext context) {
		return ODataConvertor.convertTo(ODataEntity.class, context, getString());
	}
	
	public ODataEntitySet convertToEntitySet(ODataConsumerContext context) {
		return ODataConvertor.convertTo(ODataEntitySet.class, context, getString());
	}
	
	public ODataProperty convertToProperty(ODataConsumerContext context) {
		return ODataConvertor.convertTo(ODataProperty.class, context, getString());
	}
	
	public ODataNavigationProperty convertToNavigationProperty(ODataConsumerContext context) {
		return ODataConvertor.convertTo(ODataNavigationProperty.class, context, getString());
	}
	
	public long convertToRawLong(ODataConsumerContext context) {
		String result = this.getString();
		try {
			return Long.parseLong(result);
		} catch (NumberFormatException e) {
			throw new ResolveFailedException(e);
		}
	}
	
	public Map<String, Object> convertToEntityMap(ODataConsumerContext context) {
		String result = this.getString();
		return JSON.decodeToMap(result);
	}
}
