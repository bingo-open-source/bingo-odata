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
package bingo.odata.exceptions;

import bingo.odata.ODataError;

import static bingo.odata.ODataErrors.*;

public class ODataNotFoundException extends ODataError {

	private static final long serialVersionUID = -8816614230856627697L;

	public ODataNotFoundException(String message) {
		super(SC_NOT_FOUND, EC_NOT_FOUND, message);
	}

	public ODataNotFoundException(String message, Throwable cause) {
		super(SC_NOT_FOUND, EC_NOT_FOUND, message, cause);
	}
}