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
import bingo.odata.format.json.JsonNamedValueWriter;
import bingo.odata.format.json.JsonPropertyWriter;
import bingo.odata.format.json.JsonRawValueWriter;
import bingo.odata.format.json.JsonServiceDocumentWriter;
import bingo.odata.format.xml.XmlErrorWriter;
import bingo.odata.format.xml.XmlMetadataDocumentWriter;
import static bingo.odata.ODataFormat.*;
import static bingo.odata.ODataObjectKind.*;

public class ODataProtocols {
	
	public static final ODataProtocol V3      = v3();
	public static final ODataProtocol DEFAULT = V3;
	
	private static ODataProtocol v3(){
		ODataProtocolBuilder v3builder = new ODataProtocolBuilder(ODataVersion.V1, ODataVersion.V3, ODataVersion.V3, ODataFormat.Json);
		
		//supported format
		v3builder.addSupportedFormats(ODataFormat.Atom,ODataFormat.Json);
		
		//atom readers
		v3builder.addReader(Atom, Entity, new AtomEntityReader());
		
		//json readers
		v3builder.addReader(Json, Entity, new JsonEntityReader());
		
		//Atom writers
		v3builder.addWriter(Atom,ServiceDocument, new AtomServiceDocumentWriter());
		v3builder.addWriter(Atom,MetadataDocument,new XmlMetadataDocumentWriter());
		v3builder.addWriter(Atom,Error,			  new XmlErrorWriter());
		v3builder.addWriter(Atom,EntitySet,		  new AtomEntitySetWriter());
		v3builder.addWriter(Atom,Entity,		  new AtomEntityWriter());
		
		//Json writers
		v3builder.addWriter(Json,ServiceDocument, new JsonServiceDocumentWriter());
		v3builder.addWriter(Json,Error,			  new JsonErrorWriter());	
		v3builder.addWriter(Json,EntitySet,		  new JsonEntitySetWriter());
		v3builder.addWriter(Json,Entity,		  new JsonEntityWriter());
		v3builder.addWriter(Json,Property,		  new JsonPropertyWriter());
		v3builder.addWriter(Json,Raw,		  	  new JsonRawValueWriter());
		v3builder.addWriter(Json,NamedValue,      new JsonNamedValueWriter());
		
		return v3builder.build();
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
