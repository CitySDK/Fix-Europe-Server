package eu.citysdk.participation.model;


/**
 * Google's access token
 * 
 * @author Pedro Cruz
 * 
 */
public class GoogleAccessTokenEntry extends AccessTokenEntry {
	public static final String AUDIENCE = "audience";
	public static final String SCOPE = "scope";
	public static final String USER_ID = "user_id";

	private String scope;
	private String userId;
	private String audience;

	public GoogleAccessTokenEntry() {
		super();
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getUserId() {
		return userId;
	}

	public String getAudience() {
		return audience;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public String toPrettyToken() {
		return AUDIENCE + ":" + audience + "; " 
				+ SCOPE + ":" + scope + "; "
				+ USER_ID + ":" + userId + "; " 
				+ EXPIRES_IN + ":" + this.getExpiresIn();
	}
	
	@Override
	public boolean isValidToken() {
		return true;
	}
}
