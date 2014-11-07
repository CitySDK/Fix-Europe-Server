package eu.citysdk.participation.rest.statistics;

import java.net.UnknownHostException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;
import com.sun.jersey.api.client.ClientResponse.Status;

import eu.citysdk.participation.base.Statistics;
import eu.citysdk.participation.exception.NoContentException;
import eu.citysdk.participation.helper.HttpParamsHelper;
import eu.citysdk.participation.helper.SerializerHelper;
import eu.citysdk.participation.rest.hal.ServiceResponse;

@Path("/stats")
public class StatisticsResource {
	private static final Logger logger = LoggerFactory
			.getLogger(StatisticsResource.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStats(@QueryParam(HttpParamsHelper.HTTP_PARAM_USER) String user) {
		try {
			GetStatisticsHandler handler = new GetStatisticsHandler();
			Statistics stats = handler.getStats();
			
			logger.info("Fetched statistics ({}, {}, {}, {})",
					stats.getReportedToday(), stats.getReportedWeek(),
					stats.getClosedWeek(), stats.getClosedMonth());

			return Response.ok(
					SerializerHelper.builder.create().toJson(stats,
							Statistics.class)).build();
		} catch (UnknownHostException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
			ServiceResponse error = new ServiceResponse();
			error.setCode(Status.NOT_MODIFIED.getStatusCode());
			error.setMessage(e.getMessage());

			throw new NoContentException(SerializerHelper.builder.create()
					.toJson(error, ServiceResponse.class));
		} catch (MongoException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
			ServiceResponse error = new ServiceResponse();
			error.setCode(Status.NOT_MODIFIED.getStatusCode());
			error.setMessage(e.getMessage());

			throw new NoContentException(SerializerHelper.builder.create()
					.toJson(error, ServiceResponse.class));
		}
	}
}
