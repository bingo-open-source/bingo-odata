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
package bingo.odata.server.requests.data.retrieve;

import bingo.odata.ODataObjectKind;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataQueryInfoParser;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.data.ODataEntitySet;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.server.requests.ODataRequestContext;
import bingo.odata.server.requests.data.EntitySetRequestHandlerBase;

public class RetrieveEntitySetRequestHandler extends EntitySetRequestHandlerBase {

	@Override
    protected void doHandleEntitySet(ODataRequestContext context, ODataRequest request, ODataResponse response, EdmEntitySet entitySet) throws Throwable {

		ODataQueryInfo queryInfo = ODataQueryInfoParser.parse(context.getUrlInfo().getQueryOptions());
		
		ODataEntitySet data = context.getProvider().getDataProducer().queryEntitySet(entitySet, queryInfo);
		
		write(context,request,response,ODataObjectKind.EntitySet,data);
		
    }
}