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
package bingo.odata;

import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.odata.model.ODataKey;

public interface ODataContext {

	ODataVersion getVersion();

	ODataFormat getFormat();

	ODataUrlInfo getUrlInfo();
	
	ODataServices getServices();
	
	EdmEntitySet getEntitySet();
	
	EdmEntityType getEntityType();
	
	ODataKey getEntityKey();
	
	EdmFunctionImport getFunctionImport();

	boolean isConsumer();
	
	boolean isProducer();
}