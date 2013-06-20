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

import com.sun.java_cup.internal.internal_error;

import bingo.lang.Assert;
import bingo.lang.Strings;
import bingo.lang.http.HttpHeaders;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmNavigationProperty;
import bingo.meta.edm.EdmProperty;
import bingo.odata.ODataConstants;
import bingo.odata.ODataObject;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataReader;
import bingo.odata.ODataReaderContext;
import bingo.odata.ODataResponseStatus;
import bingo.odata.ODataServices;
import bingo.odata.ODataVersion;
import bingo.odata.consumer.ext.ODataContent;
import bingo.odata.consumer.requests.DeleteEntityRequest;
import bingo.odata.consumer.requests.InsertEntityRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.requests.RetrieveEntityRequest;
import bingo.odata.consumer.requests.RetrieveEntitySetRequest;
import bingo.odata.consumer.requests.UpdateEntityRequest;
import bingo.odata.consumer.requests.builders.QueryBuilder;
import bingo.odata.consumer.requests.builders.QueryFilter;
import bingo.odata.consumer.requests.invoke.FunctionRequest;
import bingo.odata.consumer.requests.metadata.MetadataDocumentRequest;
import static bingo.odata.consumer.util.ODataConsumerContextHelper.*;
import bingo.odata.consumer.util.ODataMetadataVerifier;
import bingo.odata.exceptions.ODataNotImplementedException;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataKeyImpl;
import bingo.odata.model.ODataParameters;
import bingo.odata.model.ODataValue;
import bingo.odata.model.ODataValueImpl;

import static bingo.odata.ODataConstants.Headers.*;

public class ODataConsumerImpl implements ODataConsumer {
	private static final Log log = LogFactory.get(ODataConsumerImpl.class);
	
	private ODataConsumerConfig config = new ODataConsumerConfigImpl();
	
	private String serviceRoot;
	
	private ODataMetadataVerifier verifier;
	
	private ODataServices services;

	/**
	 * instantiate a consumer with the service root of the target odata producer.
	 * like:
	 * http://services.odata.org/V3/OData/OData.svc/
	 * @param serviceRoot
	 */
	public ODataConsumerImpl(String serviceRoot) {
		if(!Strings.endsWith(serviceRoot, "/")) {
			serviceRoot += "/";
		}
		this.serviceRoot = serviceRoot;
		services = retrieveServiceMetadata();
		verifier = new ODataMetadataVerifier(services);
	}

	/**
	 * get the config of this consumer.
	 */
	public ODataConsumerConfig config() {
		return config;
	}

	/**
	 * config this consumer.
	 * @param config
	 */
	public void config(ODataConsumerConfig config) {
		this.config = config;
	}
	
	/**
	 * get the current services using in consumer.
	 * @return
	 */
	public ODataServices services() {
		return services;
	}

	/**
	 * get the {@link ODataServices} from OData Producer.
	 * 
	 * {@link ODataServices} represents the Metadata Document.
	 */
	public ODataServices retrieveServiceMetadata() {
		ODataConsumerContext context = new ODataConsumerContext(config);
		
		Request request = new MetadataDocumentRequest(context, this.serviceRoot);
		
		ODataServices services = ODataServices.parse(getResponseInputStream(request));
		
		if(null != services) this.services = services;
		
		log.info("Retrieve Metadata Document successfully!");
		
		return services;
	}

	/**
	 * get the entity set from producer.
	 */
	public ODataEntitySet retrieveEntitySet(String entitySet) {
		ODataConsumerContext context = initEntitySetContext(this, entitySet);
		
		Request request = new RetrieveEntitySetRequest(context, serviceRoot).setEntitySet(entitySet);
		
		ODataReader<ODataEntitySet> reader = context.getProtocol().getReader(
						context.getVersion(), context.getFormat(), ODataObjectKind.EntitySet);
		try {
			ODataEntitySet oDataEntitySet = reader.read(
					(ODataReaderContext)context, new InputStreamReader(getResponseInputStream(request)));
			return oDataEntitySet;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * get a single entity by key from producer.
	 */
	public ODataEntity retrieveEntity(String entityType, Object key) {
		if(config.isVerifyMetadata()) verifier.hasEntityType(entityType);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType, key);
		
		Request request = new RetrieveEntityRequest(context, serviceRoot)
								.setEntitySet(context.getEntitySet().getName()).setId(key);
		
		ODataReader<ODataEntity> reader = context.getProtocol().getReader(
				context.getVersion(), context.getFormat(), ODataObjectKind.Entity);
		try {
			ODataEntity oDataEntity = reader.read(
					(ODataReaderContext)context, new InputStreamReader(getResponseInputStream(request)));
			return oDataEntity;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public int inserEntity(EdmEntityType entityType, ODataEntity entity) {
		return insertEntity(entityType.getName(), entity.toMap());
	}

	
	public int insertEntity(String entityType, ODataEntity entity) {
		return insertEntity(entityType, entity.toMap());
	}

	
	public int insertEntity(String entityType, Map<String, Object> object) {
		if(config.isVerifyMetadata()) verifier.hasFields(entityType, object);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType);
		
		Request request = new InsertEntityRequest(context, serviceRoot).setEntityType(context.getEntitySet().getName())
				.setEntity(object);
		Response response = request.send();
		if(response.getStatus() == ODataResponseStatus.Created) {
			return 1;
		} else {
			throw new RuntimeException("Create Failed!");//TODO
		}
	}

	private InputStream getResponseInputStream(Request request) {
		Response resp = request.send();
		if(resp.getStatus() == 200) {
			return resp.getInputStream();
		} else throw new RuntimeException("response code error!");// TODO
	}

	/**
	 * send a delete certain id entity command to producer. 
	 */
	public int deleteEntity(String entityType, Object id) {
		if(config.isVerifyMetadata()) verifier.hasEntityType(entityType);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType);
		
		Request request = new DeleteEntityRequest(context, serviceRoot)
								.setEntitySet(context.getEntitySet().getName()).setId(id);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			return 1;
		} else {
			throw new RuntimeException("Create Failed!");//TODO
		}
	}

	public int updateEntity(String entityType, Object id, Map<String, Object> updateFields) {
		if(config.isVerifyMetadata()) verifier.hasFields(entityType, updateFields);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType);
		
		Request request = new UpdateEntityRequest(context, serviceRoot)
					.setEntitySet(context.getEntitySet().getName()).setId(id).setFields(updateFields);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.NoContent) {
			return 1;
		} else {
			throw new RuntimeException("Create Failed!");//TODO
		}
	}

	public QueryBuilder queryEntity(String entityName, QueryFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public QueryBuilder queryEntity(String entityName) {
		// TODO Auto-generated method stub
		return null;
	}

	public ODataEntitySet retrieveEntitySet(EdmEntityType entityType,
			ODataQueryInfo queryInfo) {
		throw new ODataNotImplementedException("");
	}

	public long retrieveCount(EdmEntityType entityType, ODataQueryInfo queryInfo) {
		throw new ODataNotImplementedException("");
	}

	public ODataEntity retrieveEntity(EdmEntityType entityType, ODataKey key,
			ODataQueryInfo queryInfo) {
		throw new ODataNotImplementedException("");
	}

	public ODataValue retrieveProperty(EdmEntityType entitType, ODataKey key,
			EdmProperty property) {
		throw new ODataNotImplementedException("");
	}

	public ODataValue retrieveNavigationProperty(EdmEntityType entitType,
			ODataKey key, EdmNavigationProperty property) {
		throw new ODataNotImplementedException("");
	}

	public int insertEntity(EdmEntityType entityType, ODataEntity entity) {
		return insertEntity(entityType.getName(), entity);
	}

	public int updateEntity(EdmEntityType entityType, ODataKey key,
			ODataEntity entity) {
		throw new ODataNotImplementedException("");
	}

	public int mergeEntity(EdmEntityType entityType, ODataKey key,
			ODataEntity entity) {
		throw new ODataNotImplementedException("");
	}

	public void deleteEntity(EdmEntityType entityType, ODataKey key) {
		Object id = null;
		if(key.isPrimitiveValue()) {
			id = key.getPrimitiveValue();
		} else if(key.isNamedValues()) {
			id = key.getNamedValues();
		} else throw new RuntimeException("invalid key.");
		deleteEntity(entityType.getName(), id);
	}

	public String invokeFunction(EdmFunctionImport func, ODataParameters parameters) {
		return invodeFunction(func.getEntitySet(), func.getName(), parameters.getParametersMap());
	}
	
	public String invodeFunction(String entitySet, String funcName, Map<String, Object> parameters) {
		if(config.isVerifyMetadata()) verifier.hasFunction(entitySet, funcName);
		
		ODataConsumerContext context = new ODataConsumerContext(config);
		
		Request request = new FunctionRequest(context, serviceRoot)
					.setEntitySet(entitySet).setFunction(funcName).setParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			return response.getString();
		} else {
			throw new RuntimeException("Create Failed!");//TODO
		}
	}

	public ODataContent query(ODataQueryInfo queryInfo) {
		throw new ODataNotImplementedException("");
	}

	public EdmFunctionImport findFunctionImport(String entitySetName,
			String functionName) {
		throw new ODataNotImplementedException("");
	}

	
	
}
