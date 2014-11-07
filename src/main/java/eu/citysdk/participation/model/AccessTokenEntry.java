package eu.citysdk.participation.model;

import java.beans.Expression;
import java.util.Date;

import org.joda.time.DateTime;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;
import com.google.gson.JsonObject;

import eu.citysdk.participation.rest.hal.HalResource;

/**
 * Access token model
 * 
 * @author Pedro Cruz
 *
 */
@Entity(noClassnameStored=true)
public class AccessTokenEntry extends BaseEntry implements HalResource {
	public transient static final String ACCESS_TOKEN = "access_token";
	public transient static final String ISSUED_AT = "issued_at";
	public transient static final String EXPIRES_AT = "expires_at";
	public transient static final String EXPIRES_IN = "expires_in";
	
	@Property(ACCESS_TOKEN)
	private String accessToken;
	@Property(ISSUED_AT)
	private transient Date issuedAt;
	@Property(EXPIRES_AT)
	private transient Date expiresAt;
	@Property(EXPIRES_IN)
	private String expiresIn;
	
	public AccessTokenEntry() {
		accessToken = "";
		issuedAt = new DateTime().toDate();
		expiresAt = new DateTime().toDate();
		expiresIn = "0";
	}

	public String getAccessToken() {
		return accessToken;
	}

	public Date getIssuedAt() {
		return issuedAt;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
		this.expiresAt = new DateTime().plusSeconds(Integer.parseInt(expiresIn)).toDate();
	}

	public Date getExpiresAt() {
		return expiresAt;
	}
	
	public boolean isExpired() {
		return expiresAt.before(new Date());
	}
	
	public boolean isValidToken() {
		if (!isExpired())
			return true;
		
		return false;
	}
	
	@Override
	public JsonObject getMinimalRepresentation() {
		JsonObject base = new JsonObject();
		base.addProperty(ACCESS_TOKEN, accessToken);
		base.addProperty(EXPIRES_IN, expiresIn);
		
		return base;
	}
}
