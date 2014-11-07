package eu.citysdk.participation.model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import eu.citysdk.participation.rest.hal.HalMessage;
import eu.citysdk.participation.rest.hal.HalResource;

/**
 * User's information
 * 
 * @author Pedro Cruz
 *
 */
@Entity(noClassnameStored=true)
public class UserEntry extends BaseEntry implements HalResource {
	public transient static final String USER_ID = "user_id";
	public transient static final String USER_TOKEN = "user_token";
	public transient static final String USER_EMAIL = "user_email";
	public transient static final String USER_PASS = "user_pass";
	
	@Property(USER_EMAIL)
	private transient String userEmail;
	@Property(USER_PASS)
	private transient String userPassword;
	@Property(USER_ID)
	private String userId;
	@Reference(USER_TOKEN)
	private AccessTokenEntry userToken;
	
	@SerializedName("_embedded")
	private HalMessage embedded;
	
	public UserEntry() {
		this.userId = "";
		this.userEmail = "";
		this.userPassword = "";
		this.userToken = null;
		this.embedded = null;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public AccessTokenEntry getUserToken() {
		return userToken;
	}

	public void setUserToken(AccessTokenEntry userToken) {
		this.userToken = userToken;
	}

	public boolean isAuthenticated() {
		return true;
	}
	
	public boolean isRegistered() {
		return true;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	
	public String getUserPassword() {
		return userPassword;
	}
	
	public void setUserPassword(String password) {
		this.userPassword = password;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public void setEmbedded(HalMessage resource) {
		this.embedded = resource;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null 
				|| (obj != null && !(obj instanceof UserEntry)))
			return false;
		
		UserEntry user = (UserEntry) obj;
		return user.userId.equals(this.userId);
	}

	@Override
	public JsonObject getMinimalRepresentation() {
		JsonObject user = new JsonObject();
		user.addProperty(USER_ID, this.userId);
		user.add(USER_TOKEN, this.userToken.getMinimalRepresentation());
		return user;
	}

	public boolean hasToken() {
		return this.userToken != null;
	}
}
