package bingo.odata.consumer.requests;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import bingo.lang.Converts;
import bingo.lang.http.HttpContentTypes;
import bingo.meta.edm.EdmType;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataReader;
import bingo.odata.ODataReaderContext;
import bingo.odata.ODataConstants.ContentTypes;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public String getString() {
		try {
			return httpResponse.parseAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
