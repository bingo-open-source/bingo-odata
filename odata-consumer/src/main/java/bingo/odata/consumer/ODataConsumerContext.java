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
package bingo.odata.consumer;

import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.odata.ODataFormat;
import bingo.odata.ODataProtocol;
import bingo.odata.ODataQueryOptions;
import bingo.odata.ODataReaderContext;
import bingo.odata.ODataRequest;
import bingo.odata.ODataServices;
import bingo.odata.ODataUrlInfo;
import bingo.odata.ODataVersion;
import bingo.odata.ODataWriterContext;
import bingo.odata.model.ODataKey;

public class ODataConsumerContext implements ODataWriterContext,ODataReaderContext {
	
	private static final String PRINT_STACK_TRACE_OPTION = "x$printStackTrace";

	private ODataRequest  request;
	private ODataConsumer	consumer;
	private ODataProtocol protocol;
	private ODataVersion	version;
	private ODataFormat	format;
	private ODataUrlInfo	urlInfo;
	private ODataServices	services;
	private boolean		minimal;
	private boolean       printStackTrace;


	private EdmEntitySet	    entitySet;
	private EdmEntityType	    entityType;
	private ODataKey	        entityKey;
	private EdmFunctionImport   functionImport;
	
	public ODataConsumerContext() {
	}

	public ODataConsumerContext(ODataRequest request,ODataConsumer consumer,ODataProtocol protocol,ODataVersion version,ODataFormat format,ODataUrlInfo urlInfo) {
		this.request  = request;
		this.consumer = consumer;
		this.protocol = protocol;
		this.services = consumer.retrieveServiceMetadata();
		this.version  = version;
		this.format   = format;
		this.urlInfo  = urlInfo;
		this.minimal  = null == urlInfo ? false : urlInfo.getQueryOptions().isXMinimal();
		
		if("1".equals(urlInfo.getQueryOptions().getOption(PRINT_STACK_TRACE_OPTION))){
			this.printStackTrace = true;
		}else{
			this.printStackTrace = consumer.config().isPrintStackTrace();
		}
	}
	
	public ODataRequest getRequest() {
		return request;
	}

	public ODataConsumer getConsumer() {
		return consumer;
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
	
	public ODataFormat getFormatOrDefault(){
		return null == format ? ODataFormat.Default : format;
	}
	
	public ODataFormat getFormatOrConfig(){
		return null == format ? consumer.config().getDefaultFormat() : format;
	}	

	public ODataUrlInfo getUrlInfo() {
    	return urlInfo;
    }
	
	public ODataQueryOptions getQueryOptions(){
		return urlInfo.getQueryOptions();
	}

	public boolean isConsumer() {
	    return true;
    }

	public boolean isProducer() {
	    return false;
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

	public boolean isPrintStackTrace() {
		return printStackTrace;
	}	
	public void setRequest(ODataRequest request) {
		this.request = request;
	}

	public void setConsumer(ODataConsumer consumer) {
		this.consumer = consumer;
	}

	public void setProtocol(ODataProtocol protocol) {
		this.protocol = protocol;
	}

	public void setVersion(ODataVersion version) {
		this.version = version;
	}

	public void setFormat(ODataFormat format) {
		this.format = format;
	}

	public void setUrlInfo(ODataUrlInfo urlInfo) {
		this.urlInfo = urlInfo;
	}

	public void setServices(ODataServices services) {
		this.services = services;
	}

	public void setMinimal(boolean minimal) {
		this.minimal = minimal;
	}

	public void setPrintStackTrace(boolean printStackTrace) {
		this.printStackTrace = printStackTrace;
	}
}