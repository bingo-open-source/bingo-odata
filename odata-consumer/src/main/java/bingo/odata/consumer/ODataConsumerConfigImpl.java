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

import java.util.ArrayList;
import java.util.List;

import bingo.odata.ODataFormat;
import bingo.odata.ODataProtocol;
import bingo.odata.ODataProtocols;
import bingo.odata.ODataVersion;
import bingo.odata.consumer.requests.behaviors.Behavior;

public class ODataConsumerConfigImpl implements ODataConsumerConfig {

	protected String			producerUrl;
	protected ODataProtocol 	protocol 					= ODataProtocols.DEFAULT;
	protected ODataFormat   	defaultFormat 				= ODataFormat.Json;
	protected ODataVersion  	version 					= ODataVersion.V3;
	protected boolean			isLogPrintHttpMessageBody	= false;
	protected boolean       	autoDetectFormat 			= false;
	protected boolean			verifyMetadata 				= true;
	private List<Behavior> 		behaviors 					= new ArrayList<Behavior>();
	private Integer				connectTimeout				= null;
	
	public ODataProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(ODataProtocol protocol) {
		this.protocol = protocol;
	}

	public ODataFormat getDefaultFormat() {
		return defaultFormat;
	}
	
	public void setDefaultFormat(ODataFormat defaultFormat) {
		this.defaultFormat = defaultFormat;
	}

	public boolean isLogPrintHttpMessageBody() {
		return isLogPrintHttpMessageBody;
	}

	public ODataConsumerConfig setLogPrintHttpMessageBody(boolean isLogPrintHttpMessageBody) {
		this.isLogPrintHttpMessageBody = isLogPrintHttpMessageBody;
		return this;
	}

	public boolean isAutoDetectFormat() {
		return autoDetectFormat;
	}

	public void setAutoDetectFormat(boolean autoDetectFormat) {
		this.autoDetectFormat = autoDetectFormat;
	}

	public ODataVersion getVersion() {
		return version;
	}

	public void setVersion(ODataVersion version) {
		this.version = version;
	}

	public boolean isVerifyMetadata() {
		return verifyMetadata;
	}

	public void setVerifyMetadata(boolean verifyMetadata) {
		this.verifyMetadata = verifyMetadata;
	}

	public List<Behavior> getClientBehaviors() {
		return behaviors;
	}

	public ODataConsumerConfig addClientBehavior(Behavior behavior) {
		behaviors.add(behavior);
		return this;
	}

	public ODataConsumerConfig setClientBehaviors(List<Behavior> behaviors) {
		this.behaviors = behaviors;
		return this;
	}

	public String getProducerUrl() {
		return producerUrl;
	}
	
	public ODataConsumerConfig setProducerUrl(String producerUrl) {
		this.producerUrl = producerUrl;
		return this;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}
	
	public ODataConsumerConfig setConnectTimeout(Integer millis) {
		this.connectTimeout = millis;
		return this;
	}
	
}
