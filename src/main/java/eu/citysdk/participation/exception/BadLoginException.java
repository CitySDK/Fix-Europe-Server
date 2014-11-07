package eu.citysdk.participation.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class BadLoginException extends WebApplicationException {
	private static final long serialVersionUID = -8005154274820899332L;
	
	public BadLoginException(String json) {
        super(Response.status(Status.UNAUTHORIZED)
        		.type(MediaType.APPLICATION_JSON)
                .entity(json)
                .build());
    }
}
