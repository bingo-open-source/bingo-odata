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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import bingo.lang.Objects;
import bingo.lang.builder.HashCodeBuilder;

public class ODataProtocol {

	protected final ODataVersion     minSupportedVersion;
	protected final ODataVersion     maxSupportedVersion;
	protected final ODataVersion     defaultVersion;
	protected final ODataFormat      defaultFormat;
	protected final Set<ODataFormat> supportedFormats;
	
	protected final Registry<ODataReader<?>> readers;
	protected final Registry<ODataWriter<?>> writers;
	
	protected ODataProtocol(ODataVersion minSupportedVersion, 
							  ODataVersion maxSupportedVersion, 
							  Set<ODataFormat> supportedFormats,
							  ODataVersion defaultVersion, ODataFormat defaultFormat,
							  Registry<ODataReader<?>> readers,
							  Registry<ODataWriter<?>> writers) {
	    super();
	    this.minSupportedVersion = minSupportedVersion;
	    this.maxSupportedVersion = maxSupportedVersion;
	    this.defaultVersion = defaultVersion;
	    this.defaultFormat = defaultFormat;
	    this.supportedFormats = supportedFormats;
	    this.readers = readers;
	    this.writers = writers;
    }

	@SuppressWarnings("unchecked")
	public <T extends ODataObject> ODataWriter<T> getWriter(ODataVersion version,ODataFormat format,ODataObjectKind kind) {
		return (ODataWriter<T>)writers.get(version,format,kind);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ODataObject> ODataReader<T> getReader(ODataVersion version,ODataFormat format,ODataObjectKind kind) {
		return (ODataReader<T>)readers.get(version,format,kind);
	}
	
	public boolean isFormatSupported(ODataFormat format){
		return supportedFormats.contains(format);
	}
	
	public boolean isVersionSupported(ODataVersion version){
		return minSupportedVersion.isLessThanOrEqualsTo(version) && maxSupportedVersion.isGreaterThanOrEqualsTo(version);
	}

	public ODataVersion getMinSupportedVersion() {
    	return minSupportedVersion;
    }

	public ODataVersion getMaxSupportedVersion() {
    	return maxSupportedVersion;
    }

	public ODataVersion getDefaultVersion() {
    	return defaultVersion;
    }
	
	public ODataFormat getDefaultFormat() {
    	return defaultFormat;
    }
	
	static final class Registry<T> {
		private final Map<Key, T> map = new HashMap<Key, T>();
		
		public Registry<T> add(ODataFormat format,ODataObjectKind kind,T object){
			map.put(new Key(null, format, kind),object);
			return this;
		}
		
		public Registry<T> add(ODataVersion version,ODataFormat format,ODataObjectKind kind,T object){
			map.put(new Key(version, format, kind), object);
			return this;
		}
		
		public T get(ODataVersion version,ODataFormat format,ODataObjectKind kind) {
			T o = map.get(new Key(version, format, kind));
			
			return null == o ? map.get(new Key(null,format,kind)) : o;
		}
	}
	
	static final class Key {
		private final ODataVersion     version;
		private final ODataFormat      format;
		private final ODataObjectKind  kind;
		private final int 			   hashCode;
		
		public Key(ODataVersion version, ODataFormat format, ODataObjectKind kind) {
	        this.version = version;
	        this.format = format;
	        this.kind = kind;
	        this.hashCode = new HashCodeBuilder().append(version).append(format).append(kind).build();
        }
		
		@Override
        public int hashCode() {
	        return hashCode;
        }

		@Override
        public boolean equals(Object obj) {
			Key k = (Key)obj;
			return Objects.equals(version, k.version) && Objects.equals(format, k.format) && Objects.equals(kind,k.kind);
		}
	}
}