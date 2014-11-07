package eu.citysdk.participation.rest.userauth;

import java.net.UnknownHostException;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.spi.container.ResourceFilters;

import eu.citysdk.participation.auth.AuthFilter;
import eu.citysdk.participation.exception.UnauthorizedException;
import eu.citysdk.participation.helper.HttpPathHelper;
import eu.citysdk.participation.helper.ResourceHelper;
import eu.citysdk.participation.helper.SerializerHelper;
import eu.citysdk.participation.model.UserEntry;
import eu.citysdk.participation.rest.hal.HalEmbedded;
import eu.citysdk.participation.rest.hal.HalMessage;
import eu.citysdk.participation.rest.hal.RequestLinks;
import eu.citysdk.participation.rest.hal.ServiceResponse;

/**
 * Resource to authenticate/register the users
 * 
 * @author Pedro Cruz
 * 
 */
@Path(HttpPathHelper.HTTP_PATH_AUTH)
public class UserAuthResource {
	private static final Logger logger = LoggerFactory
			.getLogger(UserAuthResource.class);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response signIn(String json) throws UnauthorizedException {
		try {
			AuthMessageBody body = SerializerHelper.builder.create().fromJson(
					json, AuthMessageBody.class);
			if (body == null) {
				throw buildException("No sign in data");
			}
				
			logger.info("Authenticating/Registering {} ({})", body.getUser(),
					body.getType());
			UserEntry user = UserAuthSpecificMethod.getMethod(body.getType()).execute(
					body);
			
			HalEmbedded embedded = new HalEmbedded(ResourceHelper.REQUESTS);
			embedded.setLinks(new RequestLinks());
			
			HalMessage message = new HalMessage();
			message.setBaseResource(user);
			message.setEmbeddedResource(embedded);
			
			return Response.ok(
					SerializerHelper.builder.create().toJson(message,
							HalMessage.class)).build(); 
		} catch (UnknownHostException e) {
			throw buildException("Please try again later.");
		} catch (MongoException e) {
			throw buildException("Please try again later.");
		}
	}

	@ResourceFilters(AuthFilter.class)
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response signOut(String json) {
		try {
			AuthMessageBody body = SerializerHelper.builder.create().fromJson(json,
					AuthMessageBody.class);
			if (body == null) {
				throw buildException("No sign in data");
			}
			
			logger.info("Signing out {} ({})", body.getUser(),
					body.getToken());
			return new SignOutMethod().execute(body);
		} catch (UnknownHostException e) {
			ServiceResponse response = new ServiceResponse();
			response.setCode(Status.UNAUTHORIZED.getStatusCode());
			response.setMessage("Please try again later.");

			throw new UnauthorizedException(SerializerHelper.builder.create()
					.toJson(response, ServiceResponse.class));
		} catch (MongoException e) {
			ServiceResponse response = new ServiceResponse();
			response.setCode(Status.UNAUTHORIZED.getStatusCode());
			response.setMessage("Please try again later.");

			throw new UnauthorizedException(SerializerHelper.builder.create()
					.toJson(response, ServiceResponse.class));
		}
	}
	
	public UnauthorizedException buildException(String message) {
		ServiceResponse response = new ServiceResponse();
		response.setCode(Status.UNAUTHORIZED.getStatusCode());
		response.setMessage(message);

		return new UnauthorizedException(SerializerHelper.builder.create()
				.toJson(response, ServiceResponse.class));
	}
}
