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
import static bingo.utils.http.HttpStatus.*;

public class ODataErrors {
	
	private static final ResourceBundle SR = ResourceBundle.getBundle("bingo.odata.errors");
	
	public static final String ErrorCode_UnsupportedDataServiceVersion = "UnsupportedDataServiceVersion";
	public static final String ErrorCode_UnsupportedDataServiceFormat  = "UnsupportedDataServiceFormat";
	public static final String ErrorCode_InvalidResourcePath		     = "InvalidResourcePath";
	public static final String ErrorCode_UnsupportedResourcePath		 = "UnsupportedResourcePath";
	public static final String ErrorCode_InternalServerError		     = "InternalServerError";
	public static final String ErrorCode_BadRequest				     = "BadRequest";
	public static final String ErrorCode_NotImplemented				 = "NotImplemented";
	public static final String ErrorCode_NotFound						 = "NotFound";
	public static final String ErrorCode_UnsupportedHttpMethod		 = "UnsupportedHttpMethod";
	
	public static ODataError unsupportedDataServiceVersion(String versionString) {
		return err(SC_BAD_REQUEST,ErrorCode_UnsupportedDataServiceVersion,versionString);
	}
	
	public static ODataError unsupportedDataServiceFormat(String formatString) {
		return err(SC_BAD_REQUEST,ErrorCode_UnsupportedDataServiceFormat,formatString);
	}
	
	public static ODataError internalServerError(String errorMessage) {
		return err(SC_INTERNAL_SERVER_ERROR,ErrorCode_InternalServerError,errorMessage);
	}
	
	public static ODataError invalidResourcePath(String path){
		return err(SC_BAD_REQUEST,ErrorCode_InvalidResourcePath,path);
	}
	
	public static ODataError badRequest(String errorMessage) {
		return err(SC_BAD_REQUEST,ErrorCode_BadRequest,errorMessage);
	}
	
	public static ODataError notImplemented() {
		return err(SC_NOT_IMPLEMENTED,ErrorCode_NotImplemented);
	}
	
	public static ODataError notFound(){
		return new ODataError(SC_NOT_FOUND, ErrorCode_NotFound, "");
	}
	
	public static ODataError unsupportedHttpMethod(String method){
		return err(SC_BAD_REQUEST,ErrorCode_UnsupportedHttpMethod,method);
	}
	
	public static ODataError unsupportedResourcePath(String path){
		return err(SC_NOT_IMPLEMENTED,ErrorCode_UnsupportedResourcePath,path);
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
