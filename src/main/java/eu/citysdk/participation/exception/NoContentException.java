package eu.citysdk.participation.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class NoContentException extends WebApplicationException {
	private static final long serialVersionUID = -5431646022373025868L;

	public NoContentException(String json) {
        super(Response.status(Status.NO_CONTENT)
        		.type(MediaType.APPLICATION_JSON)
                .entity(json)
                .build());
    }
}
