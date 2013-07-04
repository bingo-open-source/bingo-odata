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

import bingo.lang.Converts;
import bingo.lang.Enumerables;
import bingo.lang.Enums;
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
import bingo.odata.model.ODataComplexObject;
import bingo.odata.model.ODataEntity;
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
				responseBody = new InputStreamConverter().convertToString(httpResponse.getContent());
				builder.append(responseBody);
			} catch (Throwable e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return builder.append(next).toString();
	}
	
	public <T> T convertToObject(Class<T> clazz, EdmType edmType, ODataConsumerContext context) {
		if(null != context.getFunctionImport()) {
			// through function invoking.
			// get ride of useless info.
			String jsonString = this.getString(), funcName = context.getFunctionImport().getName();
			Map<String, Object> jsonMap = extractValueFromFunctionResponseForMap(jsonString, funcName);
			if(edmType.isComplex() || edmType.getTypeKind() == EdmTypeKind.Reference) {
				// complex object return type.
				return Converts.convert(jsonMap, clazz);
			} else { 
				// normal entity return type.
				return convertToEntity(clazz, context, JSON.encode(jsonMap));
			}
		} else {
			// normal retrieve entity.
			return convertToEntity(clazz, context, this.getString());
		}
	}
	
	public static Object extractValueFromFunctionResponse(String jsonString, String funcName) {
		JSONObject jsonObject = JSON.decode(jsonString);
		Map<String, Object> jsonMap = null;
		if(jsonObject.isMap()) {
			jsonMap = (Map<String, Object>) jsonObject.map().get("d");
			if(1 == jsonMap.size() && null != jsonMap.get(funcName)) {
				Object tempObject = jsonMap.get(funcName);
				if(tempObject instanceof Map) {
					 Object tempObject2 = ((Map<String, Object>)tempObject).get("results");
					 if(null != tempObject2 && ((Map<String, Object>)tempObject).size() == 1) {
						 return tempObject2;
					 }
				}
				return tempObject;
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
		ODataReader<ODataEntity> reader = context.getProtocol().getReader(
				context.getVersion(), context.getFormat(), ODataObjectKind.Entity);
		try {
			ODataEntity entity = reader.read((ODataReaderContext)context, new CharArrayReader(string.toCharArray()));
			return Converts.convert(entity.toMap(), clazz);
		} catch (Throwable e) {
			throw new ResolveFailedException(e);
		}
	}

	public <T> List<T> convertToObjectList(Class<T> listClazz, EdmType edmType, ODataConsumerContext context) {
		if(null != context.getFunctionImport()) {
			// through function invoking.
			// get ride of useless info.
			String jsonString = this.getString(), funcName = context.getFunctionImport().getName();
			List<Map<String, Object>> listMap = extractValueFromFunctionResponseForList(jsonString, funcName);
			if(edmType.isComplex()
					|| edmType.getTypeKind() == EdmTypeKind.Reference
					|| (edmType.isCollection() && edmType.asCollection().getElementType().getTypeKind() == EdmTypeKind.Reference)) {
				// complex object return type.
				List<T> result = new ArrayList<T>();
				for (Map<String, Object> map : listMap) {
					result.add(Converts.convert(map, listClazz));
				}
				return result;
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
		ODataReader<ODataEntitySet> reader = context.getProtocol().getReader(
				context.getVersion(), context.getFormat(), ODataObjectKind.EntitySet);
		try {
			ODataEntitySet entitySet = reader.read((ODataReaderContext)context, new CharArrayReader(str.toCharArray()));
			List<T> list = new ArrayList<T>();
			for (ODataEntity entity : entitySet.getEntities()) {
				list.add(Converts.convert(entity.toMap(), listClazz));
			}
			return list;
		} catch (Throwable e) {
			throw new ResolveFailedException(e);
		}
	}
	
	public ODataValue convertToODataValue(EdmType edmType, ODataConsumerContext context) {
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
		ODataReader<ODataError> reader = context.getProtocol().getReader(
				context.getVersion(), context.getFormat(), ODataObjectKind.Error);
		try {
			ODataError error = reader.read((ODataReaderContext)context, new InputStreamReader(this.getInputStream()));
			
			error.setStatus(this.getStatus());
			
			return error;
		} catch (Throwable e) {
			throw new ResolveFailedException(e);
		}
	}
	
	public String convertToString(ODataConsumerContext context) {
		ODataReader<ODataNamedValue> reader = context.getProtocol().getReader(
				context.getVersion(), context.getFormat(), ODataObjectKind.NamedValue);
		try {
			ODataNamedValue namedValue = reader.read((ODataReaderContext)context, new InputStreamReader(this.getInputStream()));
			
			return ((ODataRawValueImpl)namedValue.getValue()).getValue().toString();
			
		} catch (Throwable e) {
			throw new ResolveFailedException(e);
		}
	}
	
	public ODataEntity convertToEntity(ODataConsumerContext context) {
		ODataReader<ODataEntity> reader = context.getProtocol().getReader(
				context.getVersion(), context.getFormat(), ODataObjectKind.Entity);
		
		try {
			ODataEntity oDataEntity = reader.read(
					(ODataReaderContext)context, new InputStreamReader(this.getInputStream()));
			
			return oDataEntity;
		} catch (Throwable e) {
			throw new ResolveFailedException(e);
		}
	}
	
	public ODataEntitySet convertToEntitySet(ODataConsumerContext context) {
		ODataReader<ODataEntitySet> reader = context.getProtocol().getReader(
				context.getVersion(), context.getFormat(), ODataObjectKind.EntitySet);
		
		try {
			ODataEntitySet oDataEntitySet = reader.read(
					(ODataReaderContext)context, new InputStreamReader(this.getInputStream()));
			
			return oDataEntitySet;
		} catch (Throwable e) {
			throw new ResolveFailedException(e);
		}
	}
	
	public ODataProperty convertToProperty(ODataConsumerContext context) {
		ODataReader<ODataProperty> reader = context.getProtocol().getReader(
				context.getVersion(), context.getFormat(), ODataObjectKind.Property);
		try {
			
			return reader.read((ODataReaderContext)context, new InputStreamReader(this.getInputStream()));
			
		} catch (Throwable e) {
			throw new ResolveFailedException(e);
		}
	}
	
	public ODataNavigationProperty convertToNavigationProperty(ODataConsumerContext context) {
		ODataReader<ODataNavigationProperty> reader = context.getProtocol().getReader(
				context.getVersion(), context.getFormat(), ODataObjectKind.Property);
		try {
			
			return reader.read((ODataReaderContext)context, new InputStreamReader(this.getInputStream()));
			
		} catch (Throwable e) {
			throw new ResolveFailedException(e);
		}
	}
	
	public long convertToRawLong(ODataConsumerContext context) {
		String result = this.getString();
		try {
			return Long.parseLong(result);
		} catch (NumberFormatException e) {
			throw new ResolveFailedException(e);
		}
	}
}
