package eu.citysdk.participation.rest.userauth;

import java.net.UnknownHostException;

import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.citysdk.participation.dao.TokenEntryRepository;
import eu.citysdk.participation.dao.UserEntryRepository;
import eu.citysdk.participation.exception.UnauthorizedException;
import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.helper.SecurityHelper;
import eu.citysdk.participation.helper.SerializerHelper;
import eu.citysdk.participation.model.AccessTokenEntry;
import eu.citysdk.participation.model.NullAccessTokenEntry;
import eu.citysdk.participation.model.UserEntry;
import eu.citysdk.participation.rest.hal.ServiceResponse;

/**
 * Previously registered user will sign in
 * 
 * @author Pedro Cruz
 * 
 */
public class BasicSignInMethod implements UserAuthMethod {
	private static final Logger logger = LoggerFactory
			.getLogger(BasicSignInMethod.class);

	private Mongo mongo;
	private Morphia morphia;
	private UserEntryRepository userRepo;
	private TokenEntryRepository tokenRepo;

	public BasicSignInMethod() throws UnknownHostException, MongoException {
		mongo = new Mongo(MongoHelper.HOST, MongoHelper.PORT);
		morphia = new Morphia();
		morphia.map(UserEntry.class).map(AccessTokenEntry.class);

		userRepo = new UserEntryRepository(mongo, morphia);
		tokenRepo = new TokenEntryRepository(mongo, morphia);
	}

	@Override
	public UserEntry execute(AuthMessageBody body) {
		logger.info("Basic sign in - credentials: {} {}", body.getUser(),
				body.getPassword());

		UserEntry user;
		if(body.getUser() != null && body.getUser() != "") {
			user = userRepo.findUserWithCredentials(body.getUser(),
				SecurityHelper.toSHA256(body.getPassword().getBytes()));
		} else {
			user = userRepo.findUserWithCredentials(body.getEmail(),
					SecurityHelper.toSHA256(body.getPassword().getBytes()));
		}
		if (user.isAuthenticated()) {
			AccessTokenEntry token = new NullAccessTokenEntry();
			if (user.hasToken()) {		
				token = tokenRepo.findToken(user.getUserToken().getAccessToken());
				
				if (!token.isValidToken()) {
					logger.info("Replacing token...");
					tokenRepo.deleteToken(user.getUserToken().getAccessToken());
				}
			}
			
			if (!token.isValidToken()) {
				token = SecurityHelper.getToken(user.getUserId());
				logger.info("Generated new token: {}", token.getAccessToken());
				tokenRepo.save(token);
				user = userRepo.addTokenToUser(user, token);
			}
			
			logger.info("Token: {}", user.getUserToken().getAccessToken());
			return user;
		} else {
			ServiceResponse response = new ServiceResponse();
			response.setMessage("Please provide a valid user or password");
			response.setCode(Status.UNAUTHORIZED.getStatusCode());

			logger.warn("Invalid credentials");
			throw new UnauthorizedException(SerializerHelper.builder.create()
					.toJson(response, ServiceResponse.class));
		}
	}
}
