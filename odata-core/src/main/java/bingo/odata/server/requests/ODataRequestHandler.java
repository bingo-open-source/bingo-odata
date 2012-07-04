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

import java.util.Map;

import bingo.odata.ODataException;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;

public interface ODataRequestHandler {
	
	boolean matches(ODataRequestContext context,ODataRequest request,Map<String,String> params) throws ODataException;

	void handle(ODataRequestContext context,ODataRequest request,ODataResponse response) throws ODataException;
	
}