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

import bingo.odata.ODataContextBase;
import bingo.odata.ODataFormat;
import bingo.odata.ODataReaderContext;
import bingo.odata.ODataWriterContext;

public class ODataConsumerContext extends ODataContextBase implements ODataWriterContext,ODataReaderContext {
	
	private ODataFormat format = ODataFormat.Json;
	
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
	
}