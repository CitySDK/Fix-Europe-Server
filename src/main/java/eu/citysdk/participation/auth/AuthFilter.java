package eu.citysdk.participation.auth;

import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

import eu.citysdk.participation.exception.UnauthorizedException;
import eu.citysdk.participation.helper.HttpPathHelper;
import eu.citysdk.participation.rest.hal.ServiceResponse;

/**
 * Filter to authenticate a given user when accessing a resource
 * 
 * @author Pedro Cruz
 * 
 */
public class AuthFilter implements ResourceFilter, ContainerRequestFilter {
	private static Logger logger = LoggerFactory.getLogger(AuthFilter.class);

	private static final String AUTH_URL = HttpPathHelper.HTTP_PATH_AUTH
			.replace("/", "");
	private static final String REGISTER_URL = HttpPathHelper.HTTP_PATH_REGISTER
			.replace("/", "");

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		if (!request.isSecure()) {
			logger.warn("Not allowed: not secure");
			ServiceResponse response = new ServiceResponse();
			response.setCode(Status.UNAUTHORIZED.getStatusCode());
			response.setMessage(Status.UNAUTHORIZED.getReasonPhrase());
			throw new UnauthorizedException(new Gson().toJson(response,
					ServiceResponse.class));
		}

		String method = request.getMethod();
		String path = request.getPath();

		logger.info(method + " " + request.getRequestUri());

		if (method.equals("POST") && path.toString().equals(REGISTER_URL)) {
			logger.info("Registering new user");
			return request;
		}

		if (method.equals("PUT") && path.toString().equals(AUTH_URL)) {
			logger.info("Signing out user");
			return new SignOutRequestFilter().filter(request);
		}

		return new AuthorizationRequestFilter().filter(request);
	}

	@Override
	public ContainerRequestFilter getRequestFilter() {
		return new AuthFilter();
	}

	@Override
	public ContainerResponseFilter getResponseFilter() {
		return new ContainerResponseFilter() {
			@Override
			public ContainerResponse filter(ContainerRequest request,
					ContainerResponse response) {
				return response;
			}
		};
	}
}
