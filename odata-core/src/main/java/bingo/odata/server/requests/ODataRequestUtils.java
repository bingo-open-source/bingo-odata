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

import bingo.lang.Enums;
import bingo.lang.Strings;
import bingo.odata.ODataConstants;
import bingo.odata.ODataError;
import bingo.odata.ODataErrors;
import bingo.odata.ODataFormat;
import bingo.odata.ODataRequest;
import bingo.odata.ODataVersion;
import bingo.odata.ODataConstants.Headers;
import bingo.odata.ODataConstants.QueryOptions;
import bingo.utils.http.HttpContentTypes;
import bingo.utils.http.HttpHeader;
import bingo.utils.http.HttpHeader.HeaderElement;

public class ODataRequestUtils {
	
	public static ODataVersion dataServiceVersion(ODataRequest request) throws ODataError {
		return dataServiceVersion(request,ODataConstants.Headers.DataServiceVersion);
	}
	
	public static ODataVersion minDataServiceVersion(ODataRequest request) throws ODataError {
		return dataServiceVersion(request,Headers.MinDataServiceVersion);
	}
	
	public static ODataVersion maxDataServiceVersion(ODataRequest request) throws ODataError {
		return dataServiceVersion(request,Headers.MaxDataServiceVersion);
	}

	public static ODataFormat dataServiceFormat(ODataRequest request,ODataVersion version) throws ODataError {
		String format = request.getParameter(QueryOptions.Format);
		
		if(!Strings.isEmpty(format)){
			return parseFormatFromQuery(format);
		}else{
			String accept = request.getHeader(Headers.Accept);
			
			if(!Strings.isEmpty(accept)){
				return parseFormatFromHeader(version,accept);
			}
		}

		return null;
	}
	
	private static ODataVersion dataServiceVersion(ODataRequest request,String header) throws ODataError {
		String value = request.getHeader(header);
		
		if(Strings.isEmpty(value)){
			return null;
		}else{
			return parseVersionFromHeader(value);
		}
	}
	
	private static ODataVersion parseVersionFromHeader(String headerValue) throws ODataError {
		String[] values        = Strings.split(headerValue,';');
		String   versionString = values[0];
		
		ODataVersion version = Enums.valueOf(ODataVersion.class, versionString);
		
		if(null == version){
			throw ODataErrors.unsupportedDataServiceVersion(versionString);
		}
		
		return version;
	}
	
	private static ODataFormat parseFormatFromQuery(String formatValue) throws ODataError {
		ODataFormat format = Enums.valueOf(ODataFormat.class,formatValue);
		
		if(null == format){
			throw ODataErrors.unsupportedDataServiceFormat(formatValue);
		}
		
		return format;
	}
	
	private static ODataFormat parseFormatFromHeader(ODataVersion version, String acceptValue) throws ODataError {
		HttpHeader accept = new HttpHeader("", acceptValue);

		ODataFormat format = null;
		
		for(HeaderElement element : accept.getElements()){
			String mediaType = element.getName();
			
			if(HttpContentTypes.APPLICATION_ATOM_XML.equals(mediaType)){
				format = ODataFormat.Atom;
				break;
			}else if(HttpContentTypes.APPLICATION_JSON.equals(mediaType)){
				String jsonFormat = element.getParameter("odata");
				
				if(Strings.isEmpty(jsonFormat)){
					format = version.getMajor() >= 3 ? ODataFormat.Json : ODataFormat.VerboseJson;
				}else if(ODataFormat.VerboseJson.getValue().equals(jsonFormat)){
					format = ODataFormat.VerboseJson;
				}else {
					format = ODataFormat.Json;
				}
				break;
			}else if(HttpContentTypes.APPLICATION_XML.equals(mediaType)){
				format = ODataFormat.Xml;
			}else if(HttpContentTypes.WILDCARD.equals(mediaType)){
				format = ODataFormat.Atom;
			}
		}
		
		if(null == format){
			format = ODataFormat.Atom;
		}
		
		return format;
	}
}