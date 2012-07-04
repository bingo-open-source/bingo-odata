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
import bingo.lang.exceptions.InvalidValueException;
import bingo.odata.ODataConstants;
import bingo.odata.ODataError;
import bingo.odata.ODataErrors;
import bingo.odata.ODataFormat;
import bingo.odata.ODataRequest;
import bingo.odata.ODataUrlInfo;
import bingo.odata.ODataVersion;
import bingo.odata.ODataConstants.Headers;
import bingo.odata.ODataConstants.QueryOptions;
import bingo.odata.ODataConstants.Versions;
import bingo.utils.http.HttpContentTypes;
import bingo.utils.http.HttpHeader;
import bingo.utils.http.HttpHeaders;
import bingo.utils.http.HttpHeader.HeaderElement;

public class ODataRequestUtils {

	public static ODataUrlInfo createUrlInfo(ODataRequest request){
		return new ODataUrlInfo(request.getServiceRootPath(),request.getServiceRootUrl(), request.getResourcePath(), request.getParameters());
	}
	
	public static ODataVersion getAndCheckVersion(ODataRequest request,ODataVersion defaultDataServiceVersion) throws ODataError {
		ODataVersion dataServiceVersion    = dataServiceVersion(request);
		ODataVersion minDataServiceVersion = minDataServiceVersion(request);
		ODataVersion maxDataServiceVersion = maxDataServiceVersion(request);
		
		checkDataServiceVersion(dataServiceVersion);
		checkDataServiceVersion(minDataServiceVersion);
		checkDataServiceVersion(maxDataServiceVersion);
		
		ODataVersion version = defaultDataServiceVersion;
		
		if(null != dataServiceVersion){
			version = dataServiceVersion;
		}else if(null != maxDataServiceVersion && maxDataServiceVersion.isLessThan(version)){
			version = maxDataServiceVersion;
		}else if(null !=minDataServiceVersion && version.isLessThan(minDataServiceVersion)){
			version = minDataServiceVersion;
		}

		return version;
	}
	
	public static ODataVersion dataServiceVersion(ODataRequest request) throws ODataError {
		return dataServiceVersion(request,ODataConstants.Headers.DATA_SERVICE_VERSION);
	}
	
	public static ODataVersion minDataServiceVersion(ODataRequest request) throws ODataError {
		return dataServiceVersion(request,Headers.MIN_DATA_SERVICE_VERSION);
	}
	
	public static ODataVersion maxDataServiceVersion(ODataRequest request) throws ODataError {
		return dataServiceVersion(request,Headers.MAX_DATA_SERVICE_VERSION);
	}

	public static ODataFormat dataServiceFormat(ODataRequest request,ODataVersion version) throws ODataError {
		String format = request.getParameter(QueryOptions.FORMAT);
		
		if(!Strings.isEmpty(format)){
			return parseFormatFromQuery(format);
		}else{
			String accept = request.getHeader(HttpHeaders.ACCEPT);
			
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
		
        try {
	        return Enums.valueOf(ODataVersion.class, versionString);
        } catch (InvalidValueException e) {
        	throw ODataErrors.unsupportedDataServiceVersion(versionString);
        }
	}
	
	private static ODataFormat parseFormatFromQuery(String formatValue) throws ODataError {
        try {
	        return Enums.valueOf(ODataFormat.class,formatValue);
        } catch (InvalidValueException e) {
        	throw ODataErrors.unsupportedDataServiceFormat(formatValue);
        }
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
	
	private static void checkDataServiceVersion(ODataVersion v){
		if(null != v && (v.isLessThan(Versions.MIN_DATA_SERVICE_VERSION) || v.isGreaterThan(Versions.MAX_DATA_SERVICE_VERSION))){
			throw ODataErrors.unsupportedDataServiceVersion(v.getValue());
		}
	}
}