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
package bingo.odata.server.requests;

import bingo.odata.ODataFormat;
import bingo.odata.ODataUrl;
import bingo.odata.ODataVersion;
import bingo.odata.producer.ODataProducer;

public class ODataRequestContext {

	private final ODataProducer       producer;
	private final ODataRequestMessage message;

	public ODataRequestContext(ODataProducer producer, ODataRequestMessage message) {
		this.producer = producer;
		this.message  = message;
	}

	public ODataProducer getProducer() {
		return producer;
	}

	public ODataVersion getVersion() {
		return message.getVersion();
	}

	public ODataFormat getFormat() {
		return message.getFormat();
	}

	public ODataUrl getUrl() {
    	return message.getUrl();
    }
}