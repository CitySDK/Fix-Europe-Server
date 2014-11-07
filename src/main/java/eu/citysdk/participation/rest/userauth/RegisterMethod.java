package eu.citysdk.participation.rest.userauth;

import java.net.UnknownHostException;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.sun.jersey.api.client.ClientResponse.Status;

import eu.citysdk.participation.dao.UserEntryRepository;
import eu.citysdk.participation.exception.UserAlreadyRegisteredException;
import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.helper.SecurityHelper;
import eu.citysdk.participation.helper.SerializerHelper;
import eu.citysdk.participation.model.UserEntry;
import eu.citysdk.participation.rest.hal.ServiceResponse;

/**
 * User registration
 * 
 * @author Pedro Cruz
 * 
 */
public class RegisterMethod {
	private static final Logger logger = LoggerFactory.getLogger(RegisterMethod.class);
	
	private Mongo mongo;
	private Morphia morphia;
	private UserEntryRepository userRepo;

	public RegisterMethod() throws UnknownHostException, MongoException {
		mongo = new Mongo(MongoHelper.HOST, MongoHelper.PORT);
		morphia = new Morphia();
		morphia.map(UserEntry.class);

		userRepo = new UserEntryRepository(mongo, morphia);
	}

	public Response execute(AuthMessageBody body) {
		logger.info("Registering user {} ({})", body.getUser(), body.getEmail());
		
		if (userRepo.isUsernameRegistered(body.getUser())) {
			ServiceResponse response = new ServiceResponse();
			response.setCode(Status.CONFLICT.getStatusCode());
			response.setMessage("username already in use");

			logger.warn("Username {} already in use", body.getUser());
			throw new UserAlreadyRegisteredException(SerializerHelper.builder
					.create().toJson(response, ServiceResponse.class));
		}
		
		if (userRepo.isEmailRegistered(body.getEmail())) {
			ServiceResponse response = new ServiceResponse();
			response.setCode(Status.CONFLICT.getStatusCode());
			response.setMessage("E-mail address already in use");
			
			logger.warn("E-mail {} already in use", body.getEmail());
			throw new UserAlreadyRegisteredException(SerializerHelper.builder
					.create().toJson(response, ServiceResponse.class));
		}
		
		UserEntry user = new UserEntry();
		user.setUserId(body.getUser());
		user.setUserEmail(body.getEmail());
		user.setUserPassword(SecurityHelper.toSHA256(body.getPassword().getBytes()));
		userRepo.save(user);
			
		logger.info("User {} ({})", user.getUserId(), user.getUserEmail());
		
		ServiceResponse response = new ServiceResponse();
		response.setCode(Status.OK.getStatusCode());
		response.setMessage("Registration complete");

		return Response.ok(
				SerializerHelper.builder.create().toJson(response,
						ServiceResponse.class)).build();
	}
}
