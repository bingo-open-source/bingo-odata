package odata.producer.resources;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;

import odata.ODataConstants;
import odata.OEntityId;
import odata.OEntityIds;
import odata.OEntityKey;
import odata.edm.EdmMultiplicity;
import odata.format.FormatParser;
import odata.format.FormatParserFactory;
import odata.format.FormatWriter;
import odata.format.FormatWriterFactory;
import odata.format.SingleLink;
import odata.format.SingleLinks;
import odata.producer.EntityIdResponse;
import odata.producer.ODataProducer;
import odata.producer.exceptions.NotFoundException;

import bingo.lang.enumerable.IteratedEnumerable;

public class LinksRequestResource extends BaseResource {

    private static final Logger log = Logger.getLogger(LinksRequestResource.class.getName());

    private final OEntityId     sourceEntity;
    private final String        targetNavProp;
    private final OEntityKey    targetEntityKey;

    public LinksRequestResource(OEntityId sourceEntity, String targetNavProp, OEntityKey targetEntityKey) {
        this.sourceEntity = sourceEntity;
        this.targetNavProp = targetNavProp;
        this.targetEntityKey = targetEntityKey;
    }

    @POST
    public Response createLink(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo, @Context ContextResolver<ODataProducer> producerResolver, String payload) {
        log.info(String.format("createLink(%s,%s,%s,%s)", sourceEntity.getEntitySetName(), sourceEntity.getEntityKey(), targetNavProp, targetEntityKey));

        ODataProducer producer = producerResolver.getContext(ODataProducer.class);

        OEntityId newTargetEntity = parseRequestUri(httpHeaders, uriInfo, payload);
        producer.createLink(sourceEntity, targetNavProp, newTargetEntity);
        return noContent();
    }

    @PUT
    public Response updateLink(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo, @Context ContextResolver<ODataProducer> producerResolver, String payload) {
        log.info(String.format("updateLink(%s,%s,%s,%s)", sourceEntity.getEntitySetName(), sourceEntity.getEntityKey(), targetNavProp, targetEntityKey));

        ODataProducer producer = producerResolver.getContext(ODataProducer.class);

        OEntityId newTargetEntity = parseRequestUri(httpHeaders, uriInfo, payload);
        producer.updateLink(sourceEntity, targetNavProp, targetEntityKey, newTargetEntity);
        return noContent();
    }

    private OEntityId parseRequestUri(HttpHeaders httpHeaders, UriInfo uriInfo, String payload) {
        FormatParser<SingleLink> parser = FormatParserFactory.getParser(SingleLink.class, httpHeaders.getMediaType(), null);
        SingleLink link = parser.parse(new StringReader(payload));
        return OEntityIds.parse(uriInfo.getBaseUri().toString(), link.getUri());
    }

    private Response noContent() {
        return Response.noContent().header(ODataConstants.Headers.DATA_SERVICE_VERSION, ODataConstants.DATA_SERVICE_VERSION_HEADER).build();
    }

    @DELETE
    public Response deleteLink(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo, @Context ContextResolver<ODataProducer> producerResolver) {
        log.info(String.format("deleteLink(%s,%s,%s,%s)", sourceEntity.getEntitySetName(), sourceEntity.getEntityKey(), targetNavProp, targetEntityKey));

        ODataProducer producer = producerResolver.getContext(ODataProducer.class);

        producer.deleteLink(sourceEntity, targetNavProp, targetEntityKey);
        return noContent();
    }

    @GET
    public Response getLinks(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo, @Context ContextResolver<ODataProducer> producerResolver, @QueryParam("$format") String format,
            @QueryParam("$callback") String callback) {

        log.info(String.format("getLinks(%s,%s,%s,%s)", sourceEntity.getEntitySetName(), sourceEntity.getEntityKey(), targetNavProp, targetEntityKey));

        ODataProducer producer = producerResolver.getContext(ODataProducer.class);

        EntityIdResponse response = producer.getLinks(sourceEntity, targetNavProp);

        StringWriter sw = new StringWriter();
        String serviceRootUri = uriInfo.getBaseUri().toString();
        String contentType;
        if (response.getMultiplicity() == EdmMultiplicity.MANY) {
            SingleLinks links = SingleLinks.create(serviceRootUri, response.getEntities());
            FormatWriter<SingleLinks> fw = FormatWriterFactory.getFormatWriter(SingleLinks.class, httpHeaders.getAcceptableMediaTypes(), format, callback);
            fw.write(uriInfo, sw, links);
            contentType = fw.getContentType();
        } else {
            OEntityId entityId = IteratedEnumerable.of(response.getEntities()).firstOrNull();
            if (entityId == null)
                throw new NotFoundException();

            SingleLink link = SingleLinks.create(serviceRootUri, entityId);
            FormatWriter<SingleLink> fw = FormatWriterFactory.getFormatWriter(SingleLink.class, httpHeaders.getAcceptableMediaTypes(), format, callback);
            fw.write(uriInfo, sw, link);
            contentType = fw.getContentType();
        }

        String entity = sw.toString();

        return Response.ok(entity, contentType).header(ODataConstants.Headers.DATA_SERVICE_VERSION, ODataConstants.DATA_SERVICE_VERSION_HEADER).build();
    }

}
