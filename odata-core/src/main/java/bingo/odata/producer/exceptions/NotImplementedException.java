package bingo.odata.producer.exceptions;

import javax.ws.rs.core.Response;

public class NotImplementedException extends ODataException {

    private static final long serialVersionUID = 1L;

    public NotImplementedException() {
        super(Response.status(501).build());
    }

    public NotImplementedException(String message) {
        super(Response.status(501).entity(message).build());
    }

}