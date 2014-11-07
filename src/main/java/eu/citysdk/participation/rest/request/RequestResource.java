package eu.citysdk.participation.rest.request;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.spi.container.ResourceFilters;

import eu.citysdk.participation.auth.AuthFilter;
import eu.citysdk.participation.base.Statistics;
import eu.citysdk.participation.exception.NoContentException;
import eu.citysdk.participation.exception.NotModifiedException;
import eu.citysdk.participation.helper.HttpParamsHelper;
import eu.citysdk.participation.helper.HttpPathHelper;
import eu.citysdk.participation.helper.ResourceHelper;
import eu.citysdk.participation.helper.SerializerHelper;
import eu.citysdk.participation.model.RequestEntry;
import eu.citysdk.participation.model.RequestEntryCollection;
import eu.citysdk.participation.rest.hal.HalEmbedded;
import eu.citysdk.participation.rest.hal.HalLink;
import eu.citysdk.participation.rest.hal.HalMessage;
import eu.citysdk.participation.rest.hal.RequestLinks;
import eu.citysdk.participation.rest.hal.ServiceResponse;
import eu.citysdk.participation.rest.hal.StatisticsLinks;
import eu.citysdk.participation.rest.statistics.GetStatisticsHandler;

/**
 * Requests REST resource
 * 
 * @author Pedro Cruz
 * 
 */
@ResourceFilters(AuthFilter.class)
@Path(HttpPathHelper.HTTP_PATH_REQUEST)
public class RequestResource {
	private static final Logger logger = LoggerFactory
			.getLogger(RequestResource.class);
	private static final GsonBuilder builder = SerializerHelper.builder;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequests(@Context UriInfo uriInfo) {
		try {
			MultivaluedMap<String, String> params = uriInfo
					.getQueryParameters();

			GetRequestsHandler handler = new GetRequestsHandler();
			RequestEntryCollection list = handler.getRequests(params);

			long count;
			if (params.containsKey(HttpParamsHelper.HTTP_PARAM_USER))
				count = handler.getRequestCountOfUser(params
						.getFirst(HttpParamsHelper.HTTP_PARAM_USER));
			else
				count = handler.getRequestCount();

			GetStatisticsHandler sHandler = new GetStatisticsHandler();
			// TODO: if there's a user in params it should get the stats of that
			// user
			Statistics stats = sHandler.getStats();

			String json = buildResponse(params, list, stats, count);

			logger.info("Got {} request(s)", list.size());
			logger.info(json);

			return Response.ok(json).build();
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
			ServiceResponse error = new ServiceResponse();
			error.setCode(Status.NO_CONTENT.getStatusCode());
			error.setMessage(e.getMessage());

			throw new NoContentException(builder.create().toJson(error,
					ServiceResponse.class));
		}
	}

	private String buildResponse(MultivaluedMap<String, String> params,
			RequestEntryCollection list, Statistics stats, long count) {
		HalEmbedded embedded = new HalEmbedded(ResourceHelper.STATS);
		embedded.setResource(stats);
		embedded.setLinks(new StatisticsLinks());

		long limit = params.containsKey(HttpParamsHelper.HTTP_PARAM_LIMIT) ? Integer
				.parseInt(params.getFirst(HttpParamsHelper.HTTP_PARAM_LIMIT))
				: 10;
		long offset = params.containsKey(HttpParamsHelper.HTTP_PARAM_OFFSET) ? Integer
				.parseInt(params.getFirst(HttpParamsHelper.HTTP_PARAM_OFFSET))
				: 0;
		
		if (limit == -1)
			limit = count;
		
		long remaining = count - ((offset * limit) + limit);
		logger.info("Remaining {} ({}) requests", remaining, count);

		RequestLinks links = new RequestLinks();
		if (remaining > 0) {
			String href = "rest" + HttpPathHelper.HTTP_PATH_REQUEST;
			href += "?" + HttpParamsHelper.HTTP_PARAM_LIMIT + "=" + limit;
			href += "&" + HttpParamsHelper.HTTP_PARAM_OFFSET + "=" + (++offset);

			HalLink next = new HalLink("next");
			next.setHref(href);

			links.add(next);
		}

		HalMessage message = new HalMessage();
		message.setBaseResource(list);
		message.setEmbeddedResource(embedded);
		message.setLinks(links);

		return builder.create().toJson(message);
	}

	@GET
	@Path(HttpPathHelper.HTTP_PATH_PARAM_ID)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequestsWithId(
			@PathParam(HttpParamsHelper.HTTP_PARAM_ID) String id) {
		try {
			if (id == null || (id != null && id.isEmpty()))
				throw new Exception("request id cannot be empty");

			GetRequestsHandler handler = new GetRequestsHandler();
			RequestEntry request = handler.getRequestWithId(id);

			logger.info("Got request with id {}", id);

			String json = builder.create().toJson(request);
			return Response.ok(json).build();
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
			ServiceResponse error = new ServiceResponse();
			error.setCode(Status.NO_CONTENT.getStatusCode());
			error.setMessage(e.getMessage());

			throw new NoContentException(builder.create().toJson(error,
					ServiceResponse.class));
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response postRequest(String json) {
		try {
			RequestMessageBody message = builder.create().fromJson(json,
					RequestMessageBody.class);

			if (message.getUser().isEmpty()
					|| message.getRequest().getServiceRequestId().isEmpty()) {
				throw new Exception("user and request cannot be empty");
			}

			PostRequestsHandler handler = new PostRequestsHandler();
			handler.createRequestFor(message.getUser(), message.getRequest());

			ServiceResponse ok = new ServiceResponse();
			ok.setCode(Status.CREATED.getStatusCode());
			ok.setMessage("Request created");

			logger.info("Request {} created for {}", message.getRequest()
					.getServiceRequestId(), message.getUser());

			return Response.ok(
					builder.create().toJson(ok, ServiceResponse.class)).build();
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
			ServiceResponse error = new ServiceResponse();
			error.setCode(Status.NOT_MODIFIED.getStatusCode());
			error.setMessage(e.getMessage());

			throw new NotModifiedException(builder.create().toJson(error,
					ServiceResponse.class));
		}
	}

	/*
	 * TODO:
	 */
	@PUT
	@Path(HttpPathHelper.HTTP_PATH_PARAM_ID)
	@Produces(MediaType.APPLICATION_JSON)
	public Response putRequest(
			@PathParam(HttpParamsHelper.HTTP_PARAM_ID) String id, String json) {
		return Response.ok().build();
	}

	/*
	 * TODO:
	 */
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRequest(
			@PathParam(HttpParamsHelper.HTTP_PARAM_ID) String id) {
		return Response.ok().build();
	}
}
