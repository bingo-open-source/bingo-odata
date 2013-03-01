/*
 * Copyright 2012 the original author or authors.
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
package bingo.odata.producer;

import bingo.odata.ODataFormat;
import bingo.odata.ODataReaderContext;
import bingo.odata.ODataWriterContext;
import bingo.odata.ODataProtocol;
import bingo.odata.ODataQueryOptions;
import bingo.odata.ODataServices;
import bingo.odata.ODataUrlInfo;
import bingo.odata.ODataVersion;
import bingo.odata.ODataConstants.CustomOptions;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.edm.EdmFunctionImport;
import bingo.odata.model.ODataKey;

public class ODataProducerContext implements ODataWriterContext,ODataReaderContext {

	private final ODataProducer	producer;
	private final ODataProtocol protocol;
	private final ODataVersion	version;
	private final ODataFormat	format;
	private final ODataUrlInfo	urlInfo;
	private final ODataServices	services;
	private final boolean		minimal;
	
	private EdmEntitySet	    entitySet;
	private EdmEntityType	    entityType;
	private ODataKey	        entityKey;
	private EdmFunctionImport   functionImport;

	public ODataProducerContext(ODataProducer producer,ODataProtocol protocol,ODataVersion version,ODataFormat format,ODataUrlInfo urlInfo) {
		this.producer = producer;
		this.protocol = protocol;
		this.services = producer.retrieveServiceMetadata();
		this.version  = version;
		this.format   = format;
		this.urlInfo  = urlInfo;
		this.minimal  = "1".equals(urlInfo.getQueryOptions().getOption(CustomOptions.X_MINIMAL));
	}

	public ODataProducer getProducer() {
		return producer;
	}
	
	public ODataProtocol getProtocol() {
    	return protocol;
    }

	public ODataVersion getVersion() {
    	return version;
    }

	public ODataFormat getFormat() {
    	return format;
    }

	public ODataUrlInfo getUrlInfo() {
    	return urlInfo;
    }
	
	public ODataQueryOptions getQueryOptions(){
		return urlInfo.getQueryOptions();
	}

	public boolean isConsumer() {
	    return false;
    }

	public boolean isProducer() {
	    return true;
    }

	public ODataServices getServices() {
    	return services;
    }

	public EdmEntitySet getEntitySet() {
    	return entitySet;
    }

	public void setEntitySet(EdmEntitySet entitySet) {
    	this.entitySet = entitySet;
    }

	public EdmEntityType getEntityType() {
    	return entityType;
    }

	public void setEntityType(EdmEntityType entityType) {
    	this.entityType = entityType;
    }

	public ODataKey getEntityKey() {
    	return entityKey;
    }

	public void setEntityKey(ODataKey entityKey) {
    	this.entityKey = entityKey;
    }

	public EdmFunctionImport getFunctionImport() {
    	return functionImport;
    }

	public void setFunctionImport(EdmFunctionImport functionImport) {
    	this.functionImport = functionImport;
    }

	public boolean isMinimal() {
	    return minimal;
    }
}