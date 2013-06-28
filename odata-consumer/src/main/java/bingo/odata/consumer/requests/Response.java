package bingo.odata.consumer.requests;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import bingo.lang.Converts;
import bingo.lang.Strings;
import bingo.lang.http.HttpContentTypes;
import bingo.meta.edm.EdmType;
import bingo.odata.ODataError;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataReader;
import bingo.odata.ODataReaderContext;
import bingo.odata.ODataConstants.ContentTypes;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.exceptions.ResolveFailedException;
import bingo.odata.model.ODataComplexObject;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataValue;
import bingo.odata.model.ODataValueBuilder;

import com.google.api.client.http.HttpResponse;

public class Response {
	private HttpResponse httpResponse;
	
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
			return httpResponse.getContent();
		} catch (IOException e) {
			throw new ResolveFailedException(e);
		}
	}
	
	public String getString() {
		try {
			return httpResponse.parseAsString();
		} catch (IOException e) {
			throw new ResolveFailedException(e);
		}
	}
	
	public String toString() {
		String blank = " ", next = "\n\t";
		StringBuilder builder = new StringBuilder(next)
					.append(next).append(httpResponse.getRequest().getRequestMethod())
					.append(blank).append(httpResponse.getRequest().getUrl().toString())
					.append(blank).append(httpResponse.getStatusCode())
					.append(blank).append(httpResponse.getStatusMessage())
					.append(next);
		builder.append(httpResponse.getHeaders().toString()).append(next);
//		if(!httpResponse.getContentType().equals(ContentTypes.MULTIPART_FORM_DATA)) {
//			try {
//				builder.append(httpResponse.parseAsString());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		return builder.toString();
	}
	
	public <T> T convertToObject(Class<T> clazz, EdmType edmType, ODataConsumerContext context) {
		ODataReader<ODataEntity> reader = context.getProtocol().getReader(
				context.getVersion(), context.getFormat(), ODataObjectKind.Entity);
		try {
			ODataEntity entity = reader.read((ODataReaderContext)context, new InputStreamReader(this.getInputStream()));
			return Converts.convert(entity.toMap(), clazz);
		} catch (Throwable e) {
			throw new ResolveFailedException(e);
		}
	}

	public <T> List<T> convertToObjectList(Class<T> listClazz, EdmType edmType, ODataConsumerContext context) {
		ODataReader<ODataEntitySet> reader = context.getProtocol().getReader(
				context.getVersion(), context.getFormat(), ODataObjectKind.EntitySet);
		try {
			ODataEntitySet entitySet = reader.read((ODataReaderContext)context, new InputStreamReader(this.getInputStream()));
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
	
	public long convertToLong(ODataConsumerContext context) {
		String result = this.getString();
		try {
			return Long.parseLong(result);
		} catch (NumberFormatException e) {
			throw new ResolveFailedException(e);
		}
	}
}
