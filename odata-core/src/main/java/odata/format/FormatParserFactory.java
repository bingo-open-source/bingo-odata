package odata.format;

import javax.ws.rs.core.MediaType;

import odata.OCollection;
import odata.OComplexObject;
import odata.OObject;
import odata.format.json.JsonCollectionFormatParser;
import odata.format.json.JsonComplexObjectFormatParser;
import odata.format.json.JsonEntryFormatParser;
import odata.format.json.JsonFeedFormatParser;
import odata.format.json.JsonSingleLinkFormatParser;
import odata.format.xml.AtomEntryFormatParser;
import odata.format.xml.AtomFeedFormatParser;
import odata.format.xml.AtomSingleLinkFormatParser;
import odata.producer.exceptions.BadRequestException;
import odata.producer.exceptions.NotAcceptableException;


public class FormatParserFactory {

	private FormatParserFactory() {
	}

	private static interface FormatParsers {
		FormatParser<Feed> getFeedFormatParser(Settings settings);

		FormatParser<Entry> getEntryFormatParser(Settings settings);

		FormatParser<SingleLink> getSingleLinkFormatParser(Settings settings);

		FormatParser<OComplexObject> getComplexObjectFormatParser(Settings settings);

		FormatParser<OCollection<? extends OObject>> getCollectionFormatParser(Settings settings);

	}

	@SuppressWarnings("unchecked")
	public static <T> FormatParser<T> getParser(Class<T> targetType, FormatType type, Settings settings) {
		FormatParsers formatParsers = type.equals(FormatType.JSON) ? new JsonParsers() : new AtomParsers();

		if (Feed.class.isAssignableFrom(targetType)) {
			return (FormatParser<T>) formatParsers.getFeedFormatParser(settings);
		} else if (Entry.class.isAssignableFrom(targetType)) {
			return (FormatParser<T>) formatParsers.getEntryFormatParser(settings);
		} else if (SingleLink.class.isAssignableFrom(targetType)) {
			return (FormatParser<T>) formatParsers.getSingleLinkFormatParser(settings);
		} else if (OComplexObject.class.isAssignableFrom(targetType)) {
			return (FormatParser<T>) formatParsers.getComplexObjectFormatParser(settings);
		} else if (OCollection.class.isAssignableFrom(targetType)) {
			return (FormatParser<T>) formatParsers.getCollectionFormatParser(settings);
		}
		throw new IllegalArgumentException("Unable to locate format parser for " + targetType.getName() + " and format " + type);
	}

	public static <T> FormatParser<T> getParser(Class<T> targetType, MediaType contentType, Settings settings) throws BadRequestException {

		FormatType type;
		if (contentType.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
			type = FormatType.JSON;
		} else if (contentType.isCompatible(MediaType.APPLICATION_ATOM_XML_TYPE) || contentType.isCompatible(MediaType.APPLICATION_XML_TYPE)
		        && SingleLink.class.isAssignableFrom(targetType)) {
			type = FormatType.ATOM;
		} else {
			throw new NotAcceptableException("Unknown content type " + contentType);
		}

		return getParser(targetType, type, settings);
	}

	public static class JsonParsers implements FormatParsers {

		public FormatParser<Feed> getFeedFormatParser(Settings settings) {
			return new JsonFeedFormatParser(settings);
		}

		public FormatParser<Entry> getEntryFormatParser(Settings settings) {
			return new JsonEntryFormatParser(settings);
		}

		public FormatParser<SingleLink> getSingleLinkFormatParser(Settings settings) {
			return new JsonSingleLinkFormatParser(settings);
		}

		public FormatParser<OComplexObject> getComplexObjectFormatParser(Settings settings) {
			return new JsonComplexObjectFormatParser(settings);
		}

		public FormatParser<OCollection<? extends OObject>> getCollectionFormatParser(Settings settings) {
			return new JsonCollectionFormatParser(settings);
		}

	}

	public static class AtomParsers implements FormatParsers {

		public FormatParser<Feed> getFeedFormatParser(Settings settings) {
			return new AtomFeedFormatParser(settings.metadata, settings.entitySetName, settings.entityKey, settings.fcMapping);
		}

		public FormatParser<Entry> getEntryFormatParser(Settings settings) {
			return new AtomEntryFormatParser(settings.metadata, settings.entitySetName, settings.entityKey, settings.fcMapping);
		}

		public FormatParser<SingleLink> getSingleLinkFormatParser(Settings settings) {
			return new AtomSingleLinkFormatParser();
		}

		public FormatParser<OComplexObject> getComplexObjectFormatParser(Settings settings) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public FormatParser<OCollection<? extends OObject>> getCollectionFormatParser(Settings settings) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

	}
}
