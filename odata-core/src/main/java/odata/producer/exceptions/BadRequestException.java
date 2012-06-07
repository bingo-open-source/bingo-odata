package odata.producer.exceptions;

import javax.ws.rs.core.Response;

public class BadRequestException extends ODataException {

    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super(Response.status(400).build());
    }

    public BadRequestException(String message) {
        super(Response.status(400).entity(message).build(), message);
    }

}
