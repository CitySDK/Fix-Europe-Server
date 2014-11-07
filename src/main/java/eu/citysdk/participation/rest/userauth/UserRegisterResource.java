package eu.citysdk.participation.rest.userauth;

import java.net.UnknownHostException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.MongoException;
import com.sun.jersey.api.client.ClientResponse.Status;

import eu.citysdk.participation.exception.UnauthorizedException;
import eu.citysdk.participation.helper.HttpPathHelper;
import eu.citysdk.participation.helper.SerializerHelper;
import eu.citysdk.participation.rest.hal.ServiceResponse;

@Path(HttpPathHelper.HTTP_PATH_REGISTER)
public class UserRegisterResource {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerUser(String json) {
		AuthMessageBody body = SerializerHelper.builder.create().fromJson(json,
				AuthMessageBody.class);
		if (body == null) {
			throw buildException("No user data");
		}
		
		RegisterMethod method;
		try {
			method = new RegisterMethod();
			return method.execute(body);
		} catch (UnknownHostException e) {
			throw buildException("Unable to register.");
		} catch (MongoException e) {
			throw buildException("Unable to register.");
		}
	}
	
	private UnauthorizedException buildException(String message) {
		ServiceResponse response = new ServiceResponse();
		response.setCode(Status.UNAUTHORIZED.getStatusCode());
		response.setMessage(message);

		throw new UnauthorizedException(SerializerHelper.builder.create()
				.toJson(response, ServiceResponse.class));
	}
}
