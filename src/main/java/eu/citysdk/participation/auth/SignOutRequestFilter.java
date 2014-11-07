package eu.citysdk.participation.auth;

import java.io.ByteArrayInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import eu.citysdk.participation.helper.SerializerHelper;
import eu.citysdk.participation.rest.userauth.AuthMessageBody;

public class SignOutRequestFilter implements ContainerRequestFilter {
	private static Logger logger = LoggerFactory.getLogger(SignOutRequestFilter.class);
	
	@Override
	public ContainerRequest filter(ContainerRequest request) {
		AuthorizationRequestFilter filter = new AuthorizationRequestFilter();
		request = filter.filter(request);
		
		TokenAuth tokenAuth = filter.getToken();
		AuthMessageBody body = new AuthMessageBody();
		body.setUser(tokenAuth.getUser());
		body.setToken(tokenAuth.getToken());
		
		String json = SerializerHelper.builder.create().toJson(body, AuthMessageBody.class);
		
		logger.info("Setting message body for sign out {}", json);
		
		request.setEntityInputStream(new ByteArrayInputStream(
						json.getBytes()));
		return request;
	}
}
