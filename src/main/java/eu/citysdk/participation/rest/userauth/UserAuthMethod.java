package eu.citysdk.participation.rest.userauth;

import eu.citysdk.participation.model.UserEntry;

public interface UserAuthMethod {
	UserEntry execute(AuthMessageBody body);
}
