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

import org.junit.Test;

import bingo.odata.ODataProtocol.Key;
import bingo.odata.ODataProtocol.Registry;

import static org.junit.Assert.*;

public class ODataProtocolTest {

	@Test
	public void testKeyHashCode(){
		Key key1 = new Key(null,null,null);
		Key key2 = new Key(null,null,null);
		assertEquals(key1.hashCode(), key2.hashCode());

		key1 = new Key(null, ODataFormat.Atom, ODataObjectKind.ServiceDocument);
		key2 = new Key(null, ODataFormat.Atom, ODataObjectKind.ServiceDocument);
		assertEquals(key1.hashCode(), key2.hashCode());
		
		key1 = new Key(ODataVersion.V1, ODataFormat.Atom, ODataObjectKind.ServiceDocument);
		key2 = new Key(ODataVersion.V1, ODataFormat.Atom, ODataObjectKind.ServiceDocument);
		assertEquals(key1.hashCode(), key2.hashCode());
	}
	
	@Test
	public void testRegistry(){
		Registry<Object> registry = new Registry<Object>();
		
		Object o1 = new Object();
		Object o2 = new Object();

		registry.add(ODataFormat.Atom, ODataObjectKind.ServiceDocument, o1);
		registry.add(ODataVersion.V1, ODataFormat.Atom, ODataObjectKind.ServiceDocument, o2);
		
		assertSame(o1,registry.get(null,ODataFormat.Atom, ODataObjectKind.ServiceDocument));
		assertSame(o1,registry.get(ODataVersion.V2, ODataFormat.Atom, ODataObjectKind.ServiceDocument));
		assertSame(o2,registry.get(ODataVersion.V1, ODataFormat.Atom, ODataObjectKind.ServiceDocument));
	}
}
