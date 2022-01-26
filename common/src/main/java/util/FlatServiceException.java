package util;

import lombok.Getter;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Getter
@ApplicationException
public class FlatServiceException extends RuntimeException {
    private final String message;
    private final int responseCode;

    public FlatServiceException(String message, int responseCode) {
        this.message = message;
        this.responseCode = responseCode;
    }

    public WebApplicationException toWebApplicationException() {
        return new WebApplicationException(Response.status(responseCode).entity(message).build());
    }
}