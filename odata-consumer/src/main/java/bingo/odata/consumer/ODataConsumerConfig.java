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

import java.util.List;

import bingo.odata.ODataFormat;
import bingo.odata.ODataProtocol;
import bingo.odata.ODataVersion;
import bingo.odata.consumer.requests.behaviors.Behavior;

public interface ODataConsumerConfig {
	
	String 					getProducerUrl();
	
	ODataConsumerConfig 	setProducerUrl(String producerUrl);

	ODataProtocol 			getProtocol();
	
	ODataVersion 			getVersion();
	
	ODataFormat 			getDefaultFormat();

	List<Behavior> 			getClientBehaviors();
	
	ODataConsumerConfig 	addClientBehavior(Behavior behavior);
	
	ODataConsumerConfig 	setClientBehaviors(List<Behavior> behaviors);
	
	boolean 				isVerifyMetadata();
	
	boolean 				isLogPrintHttpMessageBody();
	
	ODataConsumerConfig		setLogPrintHttpMessageBody(boolean isLogPrintHttpMessageBody);
	
	boolean 				isAutoDetectFormat();
	
	Integer					getConnectTimeout();
}
