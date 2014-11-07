package eu.citysdk.participation.auth;

import java.net.UnknownHostException;

import com.mongodb.MongoException;


/**
 * Null token auth
 * 
 * @author Pedro Cruz
 *
 */
public class NullTokenAuth extends TokenAuth {
	public NullTokenAuth() throws UnknownHostException, MongoException {
		super();
	}
	
	@Override
	public boolean hasUser() {
		return false;
	}
	
	@Override
	public boolean hasToken() {
		return false;
	}
}
