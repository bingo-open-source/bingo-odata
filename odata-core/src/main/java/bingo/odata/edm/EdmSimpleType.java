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

import java.util.LinkedHashMap;
import java.util.Map;

import bingo.lang.Enumerable;
import bingo.lang.Enumerables;

public class EdmSimpleType extends EdmType {
	
	private static final Map<EdmSimpleTypeKind, EdmSimpleType> map = new LinkedHashMap<EdmSimpleTypeKind, EdmSimpleType>();
	
	static {
		add(new EdmSimpleType(EdmSimpleTypeKind.Binary));
		add(new EdmSimpleType(EdmSimpleTypeKind.Boolean));
		add(new EdmSimpleType(EdmSimpleTypeKind.Byte));
		add(new EdmSimpleType(EdmSimpleTypeKind.DateTime));
		add(new EdmSimpleType(EdmSimpleTypeKind.DateTimeOffset));
		add(new EdmSimpleType(EdmSimpleTypeKind.Decimal));
		add(new EdmSimpleType(EdmSimpleTypeKind.Double));
		add(new EdmSimpleType(EdmSimpleTypeKind.Guid));
		add(new EdmSimpleType(EdmSimpleTypeKind.Int16));
		add(new EdmSimpleType(EdmSimpleTypeKind.Int32));
		add(new EdmSimpleType(EdmSimpleTypeKind.Int64));
		add(new EdmSimpleType(EdmSimpleTypeKind.SByte));
		add(new EdmSimpleType(EdmSimpleTypeKind.Single));
		add(new EdmSimpleType(EdmSimpleTypeKind.Stream));
		add(new EdmSimpleType(EdmSimpleTypeKind.String));
		add(new EdmSimpleType(EdmSimpleTypeKind.Time));
	}
	
	public static final Enumerable<EdmSimpleType> ALL = Enumerables.of(map.values());
	
	protected EdmSimpleType of(String kindName) {
		return of(EdmSimpleTypeKind.valueOf(kindName));
	}
	
	public EdmSimpleType of(EdmSimpleTypeKind kind) {
		return map.get(kind);
	}

	private final EdmSimpleTypeKind kind;
	
	public EdmSimpleType(EdmSimpleTypeKind kind){
		this.kind = kind;
	}

	public EdmSimpleTypeKind getKind() {
    	return kind;
    }
	
	private static void add(EdmSimpleType type){
		map.put(type.getKind(), type);
	}
}