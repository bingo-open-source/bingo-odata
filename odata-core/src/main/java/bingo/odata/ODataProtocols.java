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

import java.util.HashSet;
import java.util.Set;

import bingo.lang.Assert;
import bingo.odata.ODataProtocol.Registry;
import bingo.odata.format.atom.AtomEntityReader;
import bingo.odata.format.atom.AtomEntitySetWriter;
import bingo.odata.format.atom.AtomEntityWriter;
import bingo.odata.format.atom.AtomServiceDocumentWriter;
import bingo.odata.format.json.JsonEntityReader;
import bingo.odata.format.json.JsonEntitySetWriter;
import bingo.odata.format.json.JsonEntityWriter;
import bingo.odata.format.json.JsonErrorWriter;
import bingo.odata.format.json.JsonServiceDocumentWriter;
import bingo.odata.format.xml.XmlErrorWriter;
import bingo.odata.format.xml.XmlMetadataDocumentWriter;
import static bingo.odata.ODataFormat.*;
import static bingo.odata.ODataObjectKind.*;

public class ODataProtocols {
	
	public static final ODataProtocol V2      = v2();
	public static final ODataProtocol DEFAULT = V2;
	
	private static ODataProtocol v2(){
		ODataProtocolBuilder v2builder = new ODataProtocolBuilder(ODataVersion.V1, ODataVersion.V2, ODataVersion.V2, ODataFormat.Atom);
		
		//supported format
		v2builder.addSupportedFormats(ODataFormat.Atom,ODataFormat.Json);
		
		//atom readers
		v2builder.addReader(Atom, Entity, new AtomEntityReader());
		
		//json readers
		v2builder.addReader(Json, Entity, new JsonEntityReader());
		
		//Atom writers
		v2builder.addWriter(Atom,ServiceDocument, new AtomServiceDocumentWriter());
		v2builder.addWriter(Atom,MetadataDocument,new XmlMetadataDocumentWriter());
		v2builder.addWriter(Atom,Error,			  new XmlErrorWriter());
		v2builder.addWriter(Atom,EntitySet,		  new AtomEntitySetWriter());
		v2builder.addWriter(Atom,Entity,		  new AtomEntityWriter());
		
		//Json writers
		v2builder.addWriter(Json,ServiceDocument, new JsonServiceDocumentWriter());
		v2builder.addWriter(Json,Error,			  new JsonErrorWriter());	
		v2builder.addWriter(Json,EntitySet,		  new JsonEntitySetWriter());
		v2builder.addWriter(Json,Entity,		  new JsonEntityWriter());
		
		return v2builder.build();
	}

	public static final class ODataProtocolBuilder implements bingo.lang.Builder<ODataProtocol> {
		
		private final ODataVersion		        minSupportedVersion;
		private final ODataVersion		        maxSupportedVersion;
		private final ODataVersion		        defaultVersion;
		private final ODataFormat		        defaultFormat;
		private final Set<ODataFormat>		    supportedFormats	= new HashSet<ODataFormat>();
		private final Registry<ODataReader<?>>	readers		    	= new Registry<ODataReader<?>>();
		private final Registry<ODataWriter<?>>	writers		    	= new Registry<ODataWriter<?>>();

		public ODataProtocolBuilder(ODataVersion minSupportedVersion, ODataVersion maxSupportedVersion, ODataVersion defaultVersion, ODataFormat defaultFormat) {
			Assert.notNull(minSupportedVersion);
			Assert.notNull(maxSupportedVersion);
			Assert.notNull(defaultVersion);
			Assert.notNull(defaultFormat);
			
	        this.minSupportedVersion = minSupportedVersion;
	        this.maxSupportedVersion = maxSupportedVersion;
	        this.defaultVersion = defaultVersion;
	        this.defaultFormat = defaultFormat;
        }
		
		public ODataProtocolBuilder addSupportedFormat(ODataFormat format){
			supportedFormats.add(format);
			return this;
		}

		public ODataProtocolBuilder addSupportedFormats(ODataFormat... formats){
			for(ODataFormat f : formats){
				supportedFormats.add(f);
			}
			return this;
		}
		
		public ODataProtocolBuilder addReader(ODataFormat format,ODataObjectKind kind,ODataReader<?> reader){
			readers.add(format, kind, reader);
			return this;
		}
		
		public ODataProtocolBuilder addReader(ODataVersion version,ODataFormat format,ODataObjectKind kind,ODataReader<?> reader){
			readers.add(version,format, kind, reader);
			return this;
		}
		
		public ODataProtocolBuilder addWriter(ODataFormat format,ODataObjectKind kind,ODataWriter<?> writer){
			writers.add(format, kind, writer);
			return this;
		}
		
		public ODataProtocolBuilder addWriter(ODataVersion version,ODataFormat format,ODataObjectKind kind,ODataWriter<?> writer){
			writers.add(version,format, kind, writer);
			return this;
		}

		public ODataProtocol build() {
	        return new ODataProtocol(minSupportedVersion, maxSupportedVersion, supportedFormats, defaultVersion, defaultFormat, readers, writers);
        }
	}
}
