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

import java.util.List;

import bingo.lang.Assert;
import bingo.odata.ODataContextBase;
import bingo.odata.ODataFormat;
import bingo.odata.ODataReaderContext;
import bingo.odata.ODataVersion;
import bingo.odata.ODataWriterContext;
import bingo.odata.consumer.requests.behaviors.Behavior;

public class ODataConsumerContext extends ODataContextBase implements ODataWriterContext,ODataReaderContext {
	
	private ODataFormat format;
	private ODataVersion version;
	private int maxResults;
	private List<Behavior> behaviors;
	private Integer connectTimeout;
	private boolean isLogPrintHttpMessageBody;
	
	public ODataConsumerContext(ODataConsumerConfig config) {
		Assert.notNull(config);
		this.setProtocol(config.getProtocol());
		this.setVersion(config.getVersion());
		this.setFormat(config.getDefaultFormat());
		this.setBehaviors(config.getClientBehaviors());
		this.setLogPrintHttpMessageBody(config.isLogPrintHttpMessageBody());
		this.setConnectTimeout(config.getConnectTimeout());
	}
	
	public boolean isConsumer() {
		return true;
	}

	public boolean isProducer() {
		return false;
	}

	public boolean isMinimal() {
		return false;
	}

	/**
	 * set the default request format to JSON.
	 * @return
	 */
	@Override
	public ODataFormat getFormat() {
		return this.format;
	}

	@Override
	public ODataVersion getVersion() {
		return this.version;
	}

	public void setFormat(ODataFormat format) {
		this.format = format;
	}

	public void setVersion(ODataVersion version) {
		this.version = version;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public List<Behavior> getBehaviors() {
		return behaviors;
	}

	public void setBehaviors(List<Behavior> behaviors) {
		this.behaviors = behaviors;
	}

	public boolean isLogPrintHttpMessageBody() {
		return isLogPrintHttpMessageBody;
	}

	public void setLogPrintHttpMessageBody(boolean isLogPrintHttpMessageBody) {
		this.isLogPrintHttpMessageBody = isLogPrintHttpMessageBody;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	
}