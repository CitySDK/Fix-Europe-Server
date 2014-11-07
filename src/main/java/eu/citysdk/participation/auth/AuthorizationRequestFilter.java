package eu.citysdk.participation.auth;

import java.net.UnknownHostException;

import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.mongodb.MongoException;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import eu.citysdk.participation.exception.BadLoginException;
import eu.citysdk.participation.exception.UnauthorizedException;
import eu.citysdk.participation.helper.HttpHeaderHelper;
import eu.citysdk.participation.model.AccessTokenEntry;
import eu.citysdk.participation.rest.hal.ServiceResponse;

public class AuthorizationRequestFilter implements ContainerRequestFilter {
	private static Logger logger = LoggerFactory.getLogger(AuthorizationRequestFilter.class);
	
	private TokenAuth token;
	
	public TokenAuth getToken() {
		return token;
	}
	
	@Override
	public ContainerRequest filter(ContainerRequest request) {
		ServiceResponse response = new ServiceResponse();
		response.setCode(Status.UNAUTHORIZED.getStatusCode());
		response.setMessage(Status.UNAUTHORIZED.getReasonPhrase());
		
		String auth = request.getHeaderValue(HttpHeaderHelper.AUTHORIZATION);
		
		logger.info("Authorizing request");
		
		if (auth == null) {
			logger.warn("Not allowed: no access-token header");
			throw new UnauthorizedException(new Gson().toJson(response,
					ServiceResponse.class));
		}

		try {
			this.token = TokenAuth.decode(auth);
			if (!this.token.hasUser() && !this.token.hasToken()) {
				logger.warn("Not allowed: no user or token");
				response.setMessage("No user or token provided");
				throw new UnauthorizedException(new Gson().toJson(response,
						ServiceResponse.class));
			}

			logger.info("Checking: {} {}", this.token.getUser(), this.token.getToken());
			/*AccessTokenEntry token = this.token.checkToken();
			if (!token.isValidToken()) {
				if (token.isExpired()) {
					logger.warn("Token expired");
					this.token.revoke(token);
				}
				
				logger.warn("Not allowed: user or token invalid");
				response.setMessage("Invalid signin");
				throw new BadLoginException(new Gson().toJson(response,
						ServiceResponse.class));
			}
			*/
		} catch (MongoException e) {
			logger.warn("Unable to signin: {}", e.getMessage());
			e.printStackTrace();
			response.setMessage("Unable to signin");
			throw new UnauthorizedException(new Gson().toJson(response,
					ServiceResponse.class));
		} catch (UnknownHostException e) {
			logger.warn("Unable to signin: {}", e.getMessage());
			e.printStackTrace();
			response.setMessage("Unable to signin");
			throw new UnauthorizedException(new Gson().toJson(response,
					ServiceResponse.class));
		}

		logger.info("Allowed: {}", request.getRequestUri());
		return request;
	}
}
