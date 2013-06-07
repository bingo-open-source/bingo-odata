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
package bingo.odata.producer;

import bingo.odata.ODataFormat;
import bingo.odata.ODataProtocol;

public class ODataProducerConfigImpl implements ODataProducerConfig {

	protected ODataProtocol protocol;
	protected ODataFormat   defaultFormat;
	protected boolean		printStackTrace;
	protected boolean       autoDetectFormat;
	
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

	public boolean isPrintStackTrace() {
		return printStackTrace;
	}

	public void setPrintStackTrace(boolean printStackTrace) {
		this.printStackTrace = printStackTrace;
	}

	public boolean isAutoDetectFormat() {
		return autoDetectFormat;
	}

	public void setAutoDetectFormat(boolean autoDetectFormat) {
		this.autoDetectFormat = autoDetectFormat;
	}
}
