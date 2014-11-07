package eu.citysdk.participation.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class UserAlreadyRegisteredException extends WebApplicationException {
	private static final long serialVersionUID = 54149271821073384L;

	public UserAlreadyRegisteredException(String json) {
		super(Response.status(Status.CONFLICT)
        		.type(MediaType.APPLICATION_JSON)
                .entity(json)
                .build());
	}
}
