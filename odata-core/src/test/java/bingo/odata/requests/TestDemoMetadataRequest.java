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
package bingo.odata.requests;

import org.junit.Test;

import bingo.lang.Assert;
import bingo.odata.ODataFormat;
import bingo.odata.producer.demo.DemoODataTestBase;
import bingo.odata.requests.metadata.MetadataDocumentRequestHandler;
import bingo.odata.requests.metadata.ServiceDocumentRequestHandler;

public class TestDemoMetadataRequest extends DemoODataTestBase {
	
	private ServiceDocumentRequestHandler serviceDocumentHandler   = new ServiceDocumentRequestHandler();
	private MetadataDocumentRequestHandler metadataDocumentHandler = new MetadataDocumentRequestHandler();
	
	@Test
	public void testDemoServiceDocumentAtom() {
		request.setFormat(ODataFormat.Atom);
		
		serviceDocumentHandler.handle(producer, request, response);
		
		String content = response.getContent();
		
		System.out.println(content);
		
		Assert.notEmpty(content);
	}

	@Test
	public void testDemoMetadataDocument(){
		metadataDocumentHandler.handle(producer, request, response);
		
		String content = response.getContent();
		
		System.out.println(content);
		
		Assert.notEmpty(content);
	}
}
