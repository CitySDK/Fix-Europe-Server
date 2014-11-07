package eu.citysdk.participation.rest.userauth;

import java.net.UnknownHostException;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import eu.citysdk.participation.dao.TokenEntryRepository;
import eu.citysdk.participation.dao.UserEntryRepository;
import eu.citysdk.participation.exception.UnauthorizedException;
import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.helper.SerializerHelper;
import eu.citysdk.participation.model.AccessTokenEntry;
import eu.citysdk.participation.model.GoogleAccessTokenEntry;
import eu.citysdk.participation.model.NullAccessTokenEntry;
import eu.citysdk.participation.model.UserEntry;
import eu.citysdk.participation.rest.hal.ServiceResponse;

/**
 * Google OAuth Sign In
 * 
 * @author Pedro Cruz
 * 
 */
public class GoogleSignInMethod implements UserAuthMethod {
	private static final String GOOGLE_VALIDATION_ENDPOINT = "https://www.googleapis.com/oauth2/v1/tokeninfo";
	private static final String GOOGLE_VALIDATION_PARAM = "access_token";
	private static Logger logger = LoggerFactory
			.getLogger(GoogleSignInMethod.class);

	private Mongo mongo;
	private Morphia morphia;
	private UserEntryRepository userRepo;
	private TokenEntryRepository tokenRepo;

	public GoogleSignInMethod() throws UnknownHostException, MongoException {
		mongo = new Mongo(MongoHelper.HOST, MongoHelper.PORT);
		morphia = new Morphia();
		morphia.map(UserEntry.class).map(AccessTokenEntry.class);

		userRepo = new UserEntryRepository(mongo, morphia);
		tokenRepo = new TokenEntryRepository(mongo, morphia);
	}

	@Override
	public UserEntry execute(AuthMessageBody body) {
		AccessTokenEntry gToken = getGoogleToken(body.getToken());
		if (gToken.isValidToken()) {
			logger.info("Valid token: {} ({})", gToken.getAccessToken(),
					((GoogleAccessTokenEntry) gToken).getUserId());

			UserEntry user = userRepo
					.findUserWithId(((GoogleAccessTokenEntry) gToken).getUserId());
			if (!user.isRegistered()) {
				user = new UserEntry();
				user.setUserId(((GoogleAccessTokenEntry) gToken).getUserId());
				user.setUserEmail(body.getEmail());
				
				userRepo.save(user);
				
				logger.info("Registered new user with ID: {}", user.getUserId());
			}
			
			if (user.hasToken()) {		
				logger.info("Replacing token...");
				tokenRepo.deleteToken(user.getUserToken().getAccessToken());
			}
			
			AccessTokenEntry token = new AccessTokenEntry();
			token.setAccessToken(gToken.getAccessToken());
			token.setExpiresIn(gToken.getExpiresIn());
			tokenRepo.save(token);
			userRepo.addTokenToUser(user, token);
			
			user.setUserToken(token);
			return user;
		}
		
		ServiceResponse response = new ServiceResponse();
		response.setMessage("Please provide a valid user or password");
		response.setCode(Status.UNAUTHORIZED.getStatusCode());

		logger.warn("Invalid credentials");
		throw new UnauthorizedException(SerializerHelper.builder.create()
				.toJson(response, ServiceResponse.class));
	}

	private AccessTokenEntry getGoogleToken(String accessToken) {
		logger.info("Validating token: {}", accessToken);

		Client client = Client.create();
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add(GOOGLE_VALIDATION_PARAM, accessToken);

		WebResource webResource = client.resource(GOOGLE_VALIDATION_ENDPOINT)
				.queryParams(queryParams);
		ClientResponse response = webResource.accept("application/json").get(
				ClientResponse.class);

		if (response.getStatus() == Status.OK.getStatusCode()) {
			String json = response.getEntity(String.class);
			GoogleAccessTokenEntry gToken = SerializerHelper.builder.create()
					.fromJson(json, GoogleAccessTokenEntry.class);
			gToken.setAccessToken(accessToken);
			gToken.setExpiresIn(gToken.getExpiresIn()); // to set expiration
														// date
			return gToken;
		}

		logger.warn("Invalid token: {}", accessToken);
		return new NullAccessTokenEntry();
	}
}
