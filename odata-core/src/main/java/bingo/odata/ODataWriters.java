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

import static bingo.odata.ODataFormat.*;
import static bingo.odata.ODataObjectKind.*;

import java.util.HashMap;
import java.util.Map;

import bingo.odata.format.atom.AtomServiceDocumentWriter;
import bingo.odata.format.json.JsonServiceDocumentWriter;
import bingo.odata.format.xml.XmlErrorWriter;
import bingo.odata.format.xml.XmlMetadataDocumentWriter;

public final class ODataWriters {
	
	private static final Map<ODataFormat, Map<ODataObjectKind, ODataWriter<?>>> registry = new HashMap<ODataFormat, Map<ODataObjectKind,ODataWriter<?>>>();
	
	static {
		//Atom
		add(Atom,ServiceDocument, new AtomServiceDocumentWriter());
		add(Atom,MetadataDocument,new XmlMetadataDocumentWriter());
		add(Atom,Error,			  new XmlErrorWriter());
		
		//Json
		add(Json,ServiceDocument, new JsonServiceDocumentWriter());
		add(Json,MetadataDocument,new XmlMetadataDocumentWriter());
		add(Json,Error,			  new XmlErrorWriter());	
		
		//Verbose Json
		add(VerboseJson,ServiceDocument, new JsonServiceDocumentWriter());
		add(VerboseJson,MetadataDocument,new XmlMetadataDocumentWriter());
		add(VerboseJson,Error,			 new XmlErrorWriter());		
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends ODataObject> ODataWriter<T> of(ODataFormat format,ODataObjectKind kind) {
		Map<ODataObjectKind, ODataWriter<?>> formatWriters = registry.get(format) ;
		
		if(null != formatWriters){
			return (ODataWriter<T>)formatWriters.get(kind);
		}
		
		return null;
	}
	
	private static void add(ODataFormat format,ODataObjectKind kind,ODataWriter<?> writer) {
		Map<ODataObjectKind, ODataWriter<?>> writers = registry.get(format) ;
		
		if(null == writers){
			writers = new HashMap<ODataObjectKind, ODataWriter<?>>();
			registry.put(format, writers);
		}
		
		writers.put(kind, writer);
	}
	
	private ODataWriters() {

	}
}