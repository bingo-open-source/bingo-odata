/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bingo.odata.consumer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import bingo.lang.Strings;
import bingo.lang.http.HttpHeaders;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmProperty;
import bingo.odata.ODataConstants;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataReader;
import bingo.odata.ODataReaderContext;
import bingo.odata.ODataResponseStatus;
import bingo.odata.ODataServices;
import bingo.odata.ODataVersion;
import bingo.odata.consumer.requests.DeleteEntityRequest;
import bingo.odata.consumer.requests.InsertEntityRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.requests.RetrieveEntityRequest;
import bingo.odata.consumer.requests.RetrieveEntitySetRequest;
import bingo.odata.consumer.requests.UpdateEntityRequest;
import bingo.odata.consumer.requests.metadata.MetadataDocumentRequest;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataKeyImpl;

public class ODataConsumerImpl extends ODataConsumerAdapter {
	private static final Log log = LogFactory.get(ODataConsumerImpl.class);
	
	private String serviceRoot;
	
	private boolean verifyMetadata = true;
	
	private ODataVersion odataVersion = ODataVersion.V3;
	
	private ODataServices services;

	public ODataConsumerImpl(String serviceRoot) {
		if(!Strings.endsWith(serviceRoot, "/")) {
			serviceRoot += "/";
		}
		this.serviceRoot = serviceRoot;
		services = retrieveServiceMetadata();
	}
	
	public void setVersion(ODataVersion version) {
		this.odataVersion = version;
	}

	@Override
	public ODataConsumerConfig config() {
		// TODO Auto-generated method stub
		return super.config();
	}

	@Override
	public ODataServices retrieveServiceMetadata() {
		ODataConsumerContext context = new ODataConsumerContext();
		Request request = new MetadataDocumentRequest(this.serviceRoot);
		addDefaultHeaders(request, context, true);
		ODataServices services = ODataServices.parse(getResponseInputStream(request));
		if(null != services) this.services = services;
		log.info("Retrieve Metadata Document successfully!");
		return services;
	}

	@Override
	public ODataEntitySet retrieveEntitySet(String entitySet) {
		ODataConsumerContext context = initRetrieveEntitySetContext(entitySet);
		Request request = new RetrieveEntitySetRequest(serviceRoot).setEntitySet(entitySet);
		addDefaultHeaders(request, context);
		ODataReader<ODataEntitySet> reader = 
				context.getProtocol().getReader(odataVersion, context.getFormat(), ODataObjectKind.EntitySet);
		try {
			ODataEntitySet oDataEntitySet = reader.read((ODataReaderContext)context, new InputStreamReader(getResponseInputStream(request)));
			return oDataEntitySet;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private ODataConsumerContext initRetrieveEntitySetContext(String entitySet) {
		ODataConsumerContext context = new ODataConsumerContext();
		context.setEntitySet(services.findEntitySet(entitySet));
		return context;
	}

	@Override
	public ODataEntity retrieveEntity(String entityType, Object key) {
		ODataConsumerContext context = initRetrieveEntityContext(entityType, key);
		Request request = new RetrieveEntityRequest(serviceRoot)
								.setEntitySet(context.getEntitySet().getName()).setId(key);
		addDefaultHeaders(request, context);
		ODataReader<ODataEntity> reader = 
				context.getProtocol().getReader(odataVersion, context.getFormat(), ODataObjectKind.Entity);
		try {
			ODataEntity oDataEntity = reader.read((ODataReaderContext)context, new InputStreamReader(getResponseInputStream(request)));
			return oDataEntity;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	private ODataConsumerContext initRetrieveEntityContext(String entityType, Object key) {
		ODataConsumerContext context = new ODataConsumerContext();
		context.setEntityType(services.findEntityType(entityType));
		context.setEntitySet(services.findEntitySet(context.getEntityType()));
		context.setEntityKey(new ODataKeyImpl(key));
		return context;
	}

	@Override
	public int insertEntity(String entityType, ODataEntity entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertEntity(String entityType, Map<String, Object> object) {
		ODataConsumerContext context = initEntityTypeContext(entityType);
		if(verifyMetadata) verifyMetadataInfo(entityType, object);
		Request request = new InsertEntityRequest(serviceRoot).setEntityType(context.getEntitySet().getName())
				.setEntity(object);
		addDefaultHeaders(request, context);
		Response response = request.send();
		if(response.getStatus() == ODataResponseStatus.Created) {
			return 1;
		} else {
			throw new RuntimeException("Create Failed!");//TODO
		}
	}
	private void verifyMetadataInfo(String entityType) {
		verifyMetadataInfo(entityType, null);
	}

	private void verifyMetadataInfo(String entityType, Map<String, Object> object) {
		EdmEntityType edmEntityType = services.findEntityType(entityType);
		if(null == edmEntityType) throw new RuntimeException("No such entity type:" + entityType);
		if(null != object) {
			Object[] keys = object.keySet().toArray();
			for (Object key : keys) {
				EdmProperty property = edmEntityType.findProperty(key.toString());
				if(null == property) throw new RuntimeException("No such property:" + key.toString());
			}
		}
	}

	private ODataConsumerContext initEntityTypeContext(String entityType) {
		ODataConsumerContext context = new ODataConsumerContext();
		context.setEntityType(services.findEntityType(entityType));
		context.setEntitySet(services.findEntitySet(context.getEntityType()));
		return context;
	}

	private InputStream getResponseInputStream(Request request) {
		Response resp = request.send();
		if(resp.getStatus() == 200) {
			return resp.getInputStream();
		} else throw new RuntimeException("response code error!");// TODO
	}

	public boolean isVerifyMetadata() {
		return verifyMetadata;
	}

	public void setVerifyMetadata(boolean verifyMetadata) {
		this.verifyMetadata = verifyMetadata;
	}
	
	private void addDefaultHeaders(Request request, ODataConsumerContext context) {
		addDefaultHeaders(request, context, false);
	}
	private void addDefaultHeaders(Request request, ODataConsumerContext context, boolean ignoreAccept) {
		request.addHeader(ODataConstants.Headers.DATA_SERVICE_VERSION, odataVersion.getValue());
		request.addHeader(ODataConstants.Headers.MAX_DATA_SERVICE_VERSION, odataVersion.getValue());
		request.addHeader(ODataConstants.Headers.MIN_DATA_SERVICE_VERSION, odataVersion.getValue());
		if(!ignoreAccept) {
//			request.addHeader(HttpHeaders.ACCEPT, context.getFormat().getContentType());
			request.setAccept(context.getFormat().getContentType());
			request.addParameter(ODataConstants.QueryOptions.FORMAT, context.getFormat().getValue());
		}
	}

	@Override
	public int deleteEntity(String entityType, Object id) {
		ODataConsumerContext context = initEntityTypeContext(entityType);
		if(verifyMetadata) verifyMetadataInfo(entityType);
		Request request = new DeleteEntityRequest(serviceRoot).setEntitySet(context.getEntitySet().getName()).setId(id);
		addDefaultHeaders(request, context);
		Response response = request.send();
		if(response.getStatus() == ODataResponseStatus.OK) {
			return 1;
		} else {
			throw new RuntimeException("Create Failed!");//TODO
		}
	}

	@Override
	public int updateEntity(String entityType, Object id, Map<String, Object> updateFields) {
		ODataConsumerContext context = initEntityTypeContext(entityType);
		if(verifyMetadata) verifyMetadataInfo(entityType);
		Request request = new UpdateEntityRequest(serviceRoot).setEntitySet(context.getEntitySet().getName()).setId(id)
					.setFields(updateFields);
		addDefaultHeaders(request, context);
		Response response = request.send();
		if(response.getStatus() == ODataResponseStatus.NoContent) {
			return 1;
		} else {
			throw new RuntimeException("Create Failed!");//TODO
		}
	}
	
	
}
