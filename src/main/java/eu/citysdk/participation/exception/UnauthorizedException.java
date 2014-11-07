package eu.citysdk.participation.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class UnauthorizedException extends WebApplicationException {
	private static final long serialVersionUID = 3570263906558073204L;

	public UnauthorizedException(String json) {
        super(Response.status(Status.UNAUTHORIZED)
        		.type(MediaType.APPLICATION_JSON)
                .entity(json)
                .build());
    }
}
