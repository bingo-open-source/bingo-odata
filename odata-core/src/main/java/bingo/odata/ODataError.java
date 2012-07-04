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

public class ODataError extends ODataException implements ODataObject {

	private static final long serialVersionUID = 2055517132583888032L;
	
	private final int     status;
	private final String	code;
	private final String	message;
	
	public ODataError(int status,String code, String message) {
		this.status  = status;
	    this.code    = code;
	    this.message = message;
    }
	
	public ODataError(int status,String code, String message,Throwable cause) {
		super(cause);
		this.status  = status;
	    this.code    = code;
	    this.message = message;
    }
	
	public int getStatus() {
    	return status;
    }

	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}