package eu.citysdk.participation.rest.userauth;

import java.net.UnknownHostException;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.sun.jersey.api.client.ClientResponse.Status;

import eu.citysdk.participation.dao.TokenEntryRepository;
import eu.citysdk.participation.dao.UserEntryRepository;
import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.helper.SerializerHelper;
import eu.citysdk.participation.model.AccessTokenEntry;
import eu.citysdk.participation.model.UserEntry;
import eu.citysdk.participation.rest.hal.ServiceResponse;

public class SignOutMethod {
	private static Logger logger = LoggerFactory.getLogger(SignOutMethod.class);
	
	private Mongo mongo;
	private Morphia morphia;
	private UserEntryRepository userRepo;
	private TokenEntryRepository tokenRepo;

	public SignOutMethod() throws UnknownHostException, MongoException {
		mongo = new Mongo(MongoHelper.HOST, MongoHelper.PORT);
		morphia = new Morphia();
		morphia.map(UserEntry.class).map(AccessTokenEntry.class);

		userRepo = new UserEntryRepository(mongo, morphia);
		tokenRepo = new TokenEntryRepository(mongo, morphia);
	}

	public Response execute(AuthMessageBody body) {		
		UserEntry user = userRepo.findUserWithId(body.getUser());
		userRepo.removeTokenFromUser(user);
		tokenRepo.deleteToken(body.getToken());
		
		logger.info("User {} signed out {}", user.getUserId(), body.getToken());
		
		ServiceResponse response = new ServiceResponse();
		response.setCode(Status.OK.getStatusCode());
		response.setMessage("Signed out");
		return Response.ok(
				SerializerHelper.builder.create().toJson(response,
						ServiceResponse.class)).build();
	}

}
