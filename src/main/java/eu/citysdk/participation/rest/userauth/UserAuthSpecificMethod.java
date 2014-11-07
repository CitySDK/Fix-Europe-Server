package eu.citysdk.participation.rest.userauth;

import java.net.UnknownHostException;

import com.mongodb.MongoException;

/**
 * Factory method to get the appropriate Sign In method
 * 
 * @author Pedro Cruz
 * 
 */
public class UserAuthSpecificMethod {

	public static UserAuthMethod getMethod(String regType)
			throws UnknownHostException, MongoException {
			return UserAuthType.TYPE_BASIC.isEqual(regType) ? new BasicSignInMethod()
					: new GoogleSignInMethod();
	}
}
