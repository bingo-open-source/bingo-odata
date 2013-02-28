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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import bingo.lang.Strings;
import bingo.lang.http.HttpStatus;

public class ODataErrors extends HttpStatus {
	
	private static final ResourceBundle SR = ResourceBundle.getBundle("bingo.odata.errors");
	
	public static final String	        EC_UNSUPPORTED_DATASERVICE_VERSION	= "UnsupportedDataServiceVersion";
	public static final String	        EC_UNSUPPORTED_DATASERVICE_FORMAT	= "UnsupportedDataServiceFormat";
	public static final String	        EC_INVALID_RESOURCE_PATH	       = "InvalidResourcePath";
	public static final String	        EC_UNSUPPORTED_RESOURCE_PATH	   = "UnsupportedResourcePath";
	public static final String	        EC_INTERNAL_SERVER_ERROR	       = "InternalServerError";
	public static final String	        EC_BAD_REQUEST	                   = "BadRequest";
	public static final String	        EC_NOT_IMPLEMENTED	               = "NotImplemented";
	public static final String	        EC_NOT_FOUND	                   = "NotFound";
	public static final String	        EC_FORBIDDEN	                   = "Forbidden";
	public static final String	        EC_NOT_AUTHORIZED	               = "NotAuthorized";
	public static final String	        EC_UNSUPPORTED_HTTP_METHOD	       = "UnsupportedHttpMethod";
	
	public static ODataError unsupportedDataServiceVersion(String versionString) {
		return err(SC_BAD_REQUEST,EC_UNSUPPORTED_DATASERVICE_VERSION,versionString);
	}
	
	public static ODataError unsupportedDataServiceFormat(String formatString) {
		return err(SC_BAD_REQUEST,EC_UNSUPPORTED_DATASERVICE_FORMAT,formatString);
	}
	
	public static ODataError internalServerError(String errorMessage) {
		return err(SC_INTERNAL_SERVER_ERROR,EC_INTERNAL_SERVER_ERROR,errorMessage);
	}
	
	public static ODataError invalidResourcePath(String path){
		return err(SC_BAD_REQUEST,EC_INVALID_RESOURCE_PATH,path);
	}
	
	public static ODataError badRequest(String errorMessage) {
		return err(SC_BAD_REQUEST,EC_BAD_REQUEST,errorMessage);
	}
	
	public static ODataError badRequest(String template,Object... args) {
		return err(SC_BAD_REQUEST,EC_BAD_REQUEST,Strings.format(template, args));
	}
	
	public static ODataError notImplemented() {
		return err(SC_NOT_IMPLEMENTED,EC_NOT_IMPLEMENTED);
	}
	
	public static ODataError notImplemented(String message) {
		return new ODataError(SC_NOT_IMPLEMENTED,EC_NOT_IMPLEMENTED,message);
	}
	
	public static ODataError notImplemented(String template,Object... args) {
		return new ODataError(SC_NOT_IMPLEMENTED,EC_NOT_IMPLEMENTED,Strings.format(template, args));
	}
	
	public static ODataError notFound(){
		return new ODataError(SC_NOT_FOUND, EC_NOT_FOUND, "");
	}
	
	public static ODataError notFound(String message){
		return new ODataError(SC_NOT_FOUND, EC_NOT_FOUND, message);
	}
	
	public static ODataError notFound(String template,Object... args){
		return new ODataError(SC_NOT_FOUND, EC_NOT_FOUND, Strings.format(template, args));
	}
	
	public static ODataError unsupportedHttpMethod(String method){
		return err(SC_BAD_REQUEST,EC_UNSUPPORTED_HTTP_METHOD,method);
	}
	
	public static ODataError unsupportedResourcePath(String path){
		return err(SC_NOT_IMPLEMENTED,EC_UNSUPPORTED_RESOURCE_PATH,path);
	}
	
	private static final ODataError err(int status,String code,Object... args){
		return new ODataError(status, code, msg(code,args)); 
	}
	
	private static final String msg(String key,Object... args){
		String template = null;
		try {
	        template = SR.getString(key);
        } catch (MissingResourceException e) {
	        return "!Missing!" + key;
        }
        return Strings.format(template, args);
	}
}
