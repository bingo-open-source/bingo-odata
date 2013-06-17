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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntitySetBuilder;
import bingo.odata.ODataContext;
import bingo.odata.ODataFormat;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataProtocols;
import bingo.odata.ODataQueryOptions;
import bingo.odata.ODataReader;
import bingo.odata.ODataReaderContext;
import bingo.odata.ODataRequest;
import bingo.odata.ODataServices;
import bingo.odata.ODataVersion;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.requests.builders.retrieve.RetrieveEntitySetRequest;
import bingo.odata.consumer.requests.metadata.MetadataDocumentRequest;
import bingo.odata.model.ODataEntitySet;

public class ODataConsumerImpl extends ODataConsumerAdapter {
	private String serviceRoot;
	
	private ODataVersion odataVersion = ODataVersion.V3;
	
	private ODataContext oDataContext = new ODataConsumerContext();

	public ODataConsumerImpl(String serviceRoot) {
		this.serviceRoot = serviceRoot;
	}
	
	public void setVersion(ODataVersion version) {
		this.odataVersion = version;
	}

	@Override
	public ODataConsumerConfig config() {
		// TODO Auto-generated method stub
		return super.config();
	}

	@Override
	public ODataServices retrieveServiceMetadata() {
		Request request = new MetadataDocumentRequest(this.serviceRoot);
		return ODataServices.parse(getInputStream(request));
	}

	@Override
	public ODataEntitySet retrieveEntitySet(String entitySet) {
		Request request = new RetrieveEntitySetRequest(serviceRoot).setEntitySet(entitySet);
		ODataReader<ODataEntitySet> reader = 
				ODataProtocols.LE_V3.getReader(odataVersion, ODataFormat.Xml, ODataObjectKind.EntitySet);
		try {
			ODataEntitySet oDataEntitySet = reader.read((ODataReaderContext)oDataContext, new InputStreamReader(getInputStream(request)));
			return oDataEntitySet;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private InputStream getInputStream(Request request) {
		try {
			Response resp = request.send();
			if(resp.getStatus() == 200) {
				return resp.getInputStream();
			} else throw new RuntimeException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
