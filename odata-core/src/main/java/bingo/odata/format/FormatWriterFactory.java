package bingo.odata.format;

import java.util.List;

import javax.ws.rs.core.MediaType;

import bingo.odata.edm.EdmDataServices;
import bingo.odata.format.json.JsonCollectionFormatWriter;
import bingo.odata.format.json.JsonComplexObjectFormatWriter;
import bingo.odata.format.json.JsonEntryFormatWriter;
import bingo.odata.format.json.JsonFeedFormatWriter;
import bingo.odata.format.json.JsonPropertyFormatWriter;
import bingo.odata.format.json.JsonRequestEntryFormatWriter;
import bingo.odata.format.json.JsonServiceDocumentFormatWriter;
import bingo.odata.format.json.JsonSingleLinkFormatWriter;
import bingo.odata.format.json.JsonSingleLinksFormatWriter;
import bingo.odata.format.xml.AtomEntryFormatWriter;
import bingo.odata.format.xml.AtomFeedFormatWriter;
import bingo.odata.format.xml.AtomRequestEntryFormatWriter;
import bingo.odata.format.xml.AtomServiceDocumentFormatWriter;
import bingo.odata.format.xml.AtomSingleLinkFormatWriter;
import bingo.odata.format.xml.AtomSingleLinksFormatWriter;
import bingo.odata.format.xml.XmlPropertyFormatWriter;
import bingo.odata.producer.CollectionResponse;
import bingo.odata.producer.ComplexObjectResponse;
import bingo.odata.producer.EntitiesResponse;
import bingo.odata.producer.EntityResponse;
import bingo.odata.producer.PropertyResponse;
import bingo.odata.producer.exceptions.NotImplementedException;

public class FormatWriterFactory {

	private static interface FormatWriters {

		FormatWriter<EdmDataServices> getServiceDocumentFormatWriter();

		FormatWriter<EntitiesResponse> getFeedFormatWriter();

		FormatWriter<EntityResponse> getEntryFormatWriter();

		FormatWriter<PropertyResponse> getPropertyFormatWriter();

		FormatWriter<Entry> getRequestEntryFormatWriter();

		FormatWriter<SingleLink> getSingleLinkFormatWriter();

		FormatWriter<SingleLinks> getSingleLinksFormatWriter();

		FormatWriter<ComplexObjectResponse> getComplexObjectFormatWriter();

		FormatWriter<CollectionResponse<?>> getCollectionFormatWriter();
	}

	@SuppressWarnings("unchecked")
	public static <T> FormatWriter<T> getFormatWriter(Class<T> targetType, List<MediaType> acceptTypes, String format, String callback) {

		FormatType type = null;

		// if format is explicitly specified, use that
		if (format != null)
			type = FormatType.parse(format);

		// if header accepts json, use that
		if (type == null && acceptTypes != null) {
			for (MediaType acceptType : acceptTypes) {
				if (acceptType.equals(MediaType.APPLICATION_JSON_TYPE)) {
					type = FormatType.JSON;
					break;
				}
			}
		}

		// else default to atom
		if (type == null)
			type = FormatType.ATOM;

		FormatWriters formatWriters = type.equals(FormatType.JSON) ? new JsonWriters(callback) : new AtomWriters();

		if (targetType.equals(EdmDataServices.class))
			return (FormatWriter<T>) formatWriters.getServiceDocumentFormatWriter();

		if (targetType.equals(EntitiesResponse.class))
			return (FormatWriter<T>) formatWriters.getFeedFormatWriter();

		if (targetType.equals(EntityResponse.class))
			return (FormatWriter<T>) formatWriters.getEntryFormatWriter();

		if (targetType.equals(PropertyResponse.class))
			return (FormatWriter<T>) formatWriters.getPropertyFormatWriter();

		if (Entry.class.isAssignableFrom(targetType))
			return (FormatWriter<T>) formatWriters.getRequestEntryFormatWriter();

		if (SingleLink.class.isAssignableFrom(targetType))
			return (FormatWriter<T>) formatWriters.getSingleLinkFormatWriter();

		if (SingleLinks.class.isAssignableFrom(targetType))
			return (FormatWriter<T>) formatWriters.getSingleLinksFormatWriter();

		if (targetType.equals(ComplexObjectResponse.class)) {
			return (FormatWriter<T>) formatWriters.getComplexObjectFormatWriter();
		}

		if (targetType.equals(CollectionResponse.class)) {
			return (FormatWriter<T>) formatWriters.getCollectionFormatWriter();
		}

		throw new IllegalArgumentException("Unable to locate format writer for " + targetType.getName() + " and format " + type);

	}

	public static class JsonWriters implements FormatWriters {

		private final String	callback;

		public JsonWriters(String callback) {
			this.callback = callback;
		}

		public FormatWriter<EdmDataServices> getServiceDocumentFormatWriter() {
			return new JsonServiceDocumentFormatWriter(callback);
		}

		public FormatWriter<EntitiesResponse> getFeedFormatWriter() {
			return new JsonFeedFormatWriter(callback);
		}

		public FormatWriter<EntityResponse> getEntryFormatWriter() {
			return new JsonEntryFormatWriter(callback);
		}

		public FormatWriter<PropertyResponse> getPropertyFormatWriter() {
			return new JsonPropertyFormatWriter(callback);
		}

		public FormatWriter<Entry> getRequestEntryFormatWriter() {
			return new JsonRequestEntryFormatWriter(callback);
		}

		public FormatWriter<SingleLink> getSingleLinkFormatWriter() {
			return new JsonSingleLinkFormatWriter(callback);
		}

		public FormatWriter<SingleLinks> getSingleLinksFormatWriter() {
			return new JsonSingleLinksFormatWriter(callback);
		}

		public FormatWriter<ComplexObjectResponse> getComplexObjectFormatWriter() {
			return new JsonComplexObjectFormatWriter(callback);
		}

		public FormatWriter<CollectionResponse<?>> getCollectionFormatWriter() {
			return new JsonCollectionFormatWriter(callback);
		}
	}

	public static class AtomWriters implements FormatWriters {

		public FormatWriter<EdmDataServices> getServiceDocumentFormatWriter() {
			return new AtomServiceDocumentFormatWriter();
		}

		public FormatWriter<EntitiesResponse> getFeedFormatWriter() {
			return new AtomFeedFormatWriter();
		}

		public FormatWriter<EntityResponse> getEntryFormatWriter() {
			return new AtomEntryFormatWriter();
		}

		public FormatWriter<PropertyResponse> getPropertyFormatWriter() {
			return new XmlPropertyFormatWriter();
		}

		public FormatWriter<Entry> getRequestEntryFormatWriter() {
			return new AtomRequestEntryFormatWriter();
		}

		public FormatWriter<SingleLink> getSingleLinkFormatWriter() {
			return new AtomSingleLinkFormatWriter();
		}

		public FormatWriter<SingleLinks> getSingleLinksFormatWriter() {
			return new AtomSingleLinksFormatWriter();
		}

		public FormatWriter<ComplexObjectResponse> getComplexObjectFormatWriter() {
			throw new NotImplementedException();
		}

		public FormatWriter<CollectionResponse<?>> getCollectionFormatWriter() {
			throw new NotImplementedException();
		}

	}

}
