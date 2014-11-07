package eu.citysdk.participation.rest.userauth;

import javax.ws.rs.core.Response.Status;

import eu.citysdk.participation.exception.UnauthorizedException;
import eu.citysdk.participation.helper.SerializerHelper;
import eu.citysdk.participation.model.UserEntry;
import eu.citysdk.participation.rest.hal.ServiceResponse;

/**
 * Nothing to be done, the request was incorrect
 * 
 * @author Pedro Cruz
 *
 */
public class NoMethod implements UserAuthMethod {
	
	@Override
	public UserEntry execute(AuthMessageBody body) {
		ServiceResponse response = new ServiceResponse();
		response.setMessage("Please provide a valid user or password");
		response.setCode(Status.UNAUTHORIZED.getStatusCode());
		
		throw new UnauthorizedException(SerializerHelper.builder
				.create().toJson(response, ServiceResponse.class));
	}
}
