package eu.citysdk.participation.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class NotModifiedException extends WebApplicationException {
	private static final long serialVersionUID = -5859325286105433285L;

	public NotModifiedException(String json) {
        super(Response.status(Status.NOT_MODIFIED)
        		.type(MediaType.APPLICATION_JSON)
                .entity(json)
                .build());
    }
}
