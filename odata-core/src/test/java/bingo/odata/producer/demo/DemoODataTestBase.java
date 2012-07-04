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
package bingo.odata.producer.demo;

import org.junit.Before;

import bingo.odata.ODataFormat;
import bingo.odata.ODataUrlInfo;
import bingo.odata.ODataVersion;
import bingo.odata.server.mock.MockODataRequest;
import bingo.odata.server.mock.MockODataResponse;
import bingo.odata.server.requests.ODataRequestContext;

public abstract class DemoODataTestBase {
	
	protected static final DemoODataProvider producer = new DemoODataProvider();
	
	protected MockODataRequest    request;
	protected MockODataResponse   response;
	
	@Before
	public void setUp(){
		request  = new MockODataRequest();
		response = new MockODataResponse();
		
		request.setDataServiceVerion(ODataVersion.V3);
		request.setFormat(ODataFormat.Atom);
		request.setServiceRootPath("/demo");
		request.setServiceRootUrl("http://localhost/demo");
		request.setResourcePath("/");
	}
	
	protected ODataRequestContext context(){
		ODataUrlInfo url = new ODataUrlInfo(request.getServiceRootPath(), request.getServiceRootUrl(), request.getResourcePath(), request.getParameters());

		return new ODataRequestContext(producer,ODataVersion.V3,ODataFormat.Atom,url);
	}
}