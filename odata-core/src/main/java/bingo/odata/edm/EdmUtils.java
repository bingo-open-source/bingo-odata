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
package bingo.odata.edm;

import bingo.lang.Strings;

public class EdmUtils {

	protected EdmUtils(){
		
	}
	
	public static String getQualifiedName(EdmSchema schema,EdmType type) {
		if(type instanceof EdmNamedStructualType){
			return Strings.join(new String[]{schema.getNamespaceName(),((EdmNamedStructualType) type).getName()},'.');
		}else if(type instanceof EdmSimpleType){
			return "Edm." + ((EdmSimpleType)type).getKind().toString();
		}
		return Strings.EMPTY;
	}
	
}
