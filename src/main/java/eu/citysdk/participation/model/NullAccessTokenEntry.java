package eu.citysdk.participation.model;

/**
 * Null access token object
 * 
 * @author Pedro Cruz
 *
 */
public class NullAccessTokenEntry extends AccessTokenEntry {
	public NullAccessTokenEntry() {
		super();
	}
	
	@Override
	public boolean isValidToken() {
		return false;
	}
}
