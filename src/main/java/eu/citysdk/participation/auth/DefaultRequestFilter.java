package eu.citysdk.participation.auth;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class DefaultRequestFilter implements ContainerRequestFilter {

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		return request;
	}
}
