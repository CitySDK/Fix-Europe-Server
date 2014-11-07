package eu.citysdk.participation.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.joda.time.DateTime;

import com.sun.jersey.core.util.Base64;

import eu.citysdk.participation.model.AccessTokenEntry;

/**
 * Creates a token for a given user
 * 
 * @author Pedro Cruz
 *
 */
public class SecurityHelper {
	private static final String EXPIRATION_TIME = "3600";	// seconds

	public static AccessTokenEntry getToken(String user) {
		DateTime time = new DateTime();
		String accessToken = UUID.randomUUID().toString().toUpperCase() + "|" + user
				+ "|" + time.getMillis();
		accessToken = toSHA256(accessToken.getBytes());
		
		AccessTokenEntry token = new AccessTokenEntry();
		token.setAccessToken(accessToken);
		token.setExpiresIn(EXPIRATION_TIME);
		
		return token;
	}
	
	public static String toSHA256(byte[] convertme) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	
		return new String(Base64.encode(md.digest(convertme)));
	} 
}
