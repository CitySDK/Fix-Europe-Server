package eu.citysdk.participation.auth;

import java.net.UnknownHostException;

import javax.xml.bind.DatatypeConverter;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.sun.jersey.core.util.Base64;

import eu.citysdk.participation.dao.TokenEntryRepository;
import eu.citysdk.participation.dao.UserEntryRepository;
import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.model.AccessTokenEntry;
import eu.citysdk.participation.model.NullAccessTokenEntry;
import eu.citysdk.participation.model.UserEntry;

/**
 * Basic Authorization values
 * 
 * @author Pedro Cruz
 * 
 */
public class TokenAuth {
	private static final int USER = 0;
	private static final int TOKEN = 1;

	private String user = "";
	private String token = "";

	private Mongo mongo;
	private Morphia morphia;
	private TokenEntryRepository tokenRepo;
	private UserEntryRepository userRepo;

	protected TokenAuth() {	
		user = "";
		token = "";
	}

	private TokenAuth(String user, String token) throws UnknownHostException, MongoException {
		this.user = user;
		this.token = token;

		mongo = new Mongo(MongoHelper.HOST, MongoHelper.PORT);
		morphia = new Morphia();
		morphia.map(UserEntry.class).map(AccessTokenEntry.class);

		tokenRepo = new TokenEntryRepository(mongo, morphia);
		userRepo = new UserEntryRepository(mongo, morphia);
	}

	public String getUser() {
		return user;
	}

	public String getToken() {
		return token;
	}

	public boolean hasUser() {
		return !user.isEmpty();
	}

	public boolean hasToken() {
		return !token.isEmpty();
	}

	/**
	 * Decode the basic auth and convert it to a @{BasicAuth}
	 * 
	 * @param auth
	 *            The string encoded authentication
	 * @return a @{BasicAuth} containing the user and password
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	public static TokenAuth decode(String auth) throws UnknownHostException, MongoException {
		byte[] decodedBytes = DatatypeConverter.parseBase64Binary(auth);

		if (decodedBytes == null || decodedBytes.length == 0)
			return new NullTokenAuth();

		String[] authValues = new String(decodedBytes).split(":", 2);

		if (authValues.length == 2) {
			return new TokenAuth(authValues[USER], authValues[TOKEN]);
		} else
			return new NullTokenAuth();
	}

	/**
	 * Gets the given token for the user
	 * 
	 * @return a {@link eu.citysdk.participation.model.AccessTokenEntry} or its null object
	 */
	public AccessTokenEntry checkToken() {
		AccessTokenEntry token = tokenRepo.findToken(this.getToken());
		if (token.isExpired())
			return new NullAccessTokenEntry();

		return token;

	}

	/**
	 * Revokes the given token
	 */
	public void revoke(AccessTokenEntry token) {
		UserEntry user = userRepo.findUserWithToken(this.getUser(), token);
		userRepo.removeTokenFromUser(user);
		tokenRepo.deleteToken(token.getAccessToken());
	}
}
