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

import bingo.odata.ODataException;
import bingo.odata.ODataResponseStatus;

public class ODataRequestException extends ODataException {

	private static final long serialVersionUID = 7876393818243716132L;
	
	private final int status;
	
	public ODataRequestException() {
	    super();
	    this.status = ODataResponseStatus.InternalError;
    }

	public ODataRequestException(String message, Object... args) {
	    super(message, args);
	    this.status = ODataResponseStatus.InternalError;
    }
	
	public ODataRequestException(int status,String message, Object... args) {
	    super(message, args);
	    this.status = status;
    }

	public ODataRequestException(String message, Throwable cause) {
	    super(message, cause);
	    this.status = ODataResponseStatus.InternalError;
    }
	
	public ODataRequestException(int status,String message, Throwable cause) {
	    super(message, cause);
	    this.status = status;
    }

	public ODataRequestException(String message) {
	    super(message);
	    this.status = ODataResponseStatus.InternalError;
    }
	
	public ODataRequestException(int status,String message) {
	    super(message);
	    this.status = status;
    }

	public ODataRequestException(Throwable cause) {
	    super(cause);
	    this.status = ODataResponseStatus.InternalError;
    }
	
	public ODataRequestException(int status,Throwable cause) {
	    super(cause);
	    this.status = status;
    }
	
	public int getStatus(){
		return status;
	}
}