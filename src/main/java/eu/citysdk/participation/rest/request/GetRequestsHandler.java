package eu.citysdk.participation.rest.request;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.citysdk.participation.dao.RequestEntryRepository;
import eu.citysdk.participation.dao.UserEntryRepository;
import eu.citysdk.participation.helper.HttpParamsHelper;
import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.model.AccessTokenEntry;
import eu.citysdk.participation.model.RequestEntry;
import eu.citysdk.participation.model.RequestEntryCollection;
import eu.citysdk.participation.model.UserEntry;

/**
 * Handles the GET requests
 * 
 * @author Pedro Cruz
 * 
 */
public class GetRequestsHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(GetRequestsHandler.class);

	private Mongo mongo;
	private Morphia morphia;
	private RequestEntryRepository reqRepo;
	private UserEntryRepository userRepo;

	public GetRequestsHandler() throws UnknownHostException, MongoException {
		mongo = new Mongo(MongoHelper.HOST, MongoHelper.PORT);
		morphia = new Morphia();
		morphia.map(UserEntry.class).map(AccessTokenEntry.class);

		reqRepo = new RequestEntryRepository(mongo, morphia);
		userRepo = new UserEntryRepository(mongo, morphia);
	}

	/**
	 * Gets all the stored requests
	 * 
	 * @return a list of {@link eu.citysdk.participation.model.RequestEntry}
	 */
	public List<RequestEntry> getAllRequests() {
		return reqRepo.findAll();
	}

	/**
	 * Gets the request with a given id
	 * 
	 * @param id
	 *            the request id
	 * @return a {@link eu.citysdk.participation.model.RequestEntry}
	 */
	public RequestEntry getRequestWithId(String id) {
		return reqRepo.findRequestWithId(id);
	}

	/**
	 * Gets the request with the given set of parameters
	 * 
	 * @param pathParameters
	 *            the parameters
	 * @return a list of {@link eu.citysdk.participation.model.RequestEntry}
	 */
	public RequestEntryCollection getRequests(
			MultivaluedMap<String, String> pathParameters) {
		UserEntry user;

		int limit = pathParameters
				.containsKey(HttpParamsHelper.HTTP_PARAM_LIMIT) ? Integer
				.parseInt(pathParameters
						.getFirst(HttpParamsHelper.HTTP_PARAM_LIMIT)) : 10;
		int offset = pathParameters
				.containsKey(HttpParamsHelper.HTTP_PARAM_OFFSET) ? Integer
				.parseInt(pathParameters
						.getFirst(HttpParamsHelper.HTTP_PARAM_OFFSET)) : 0;

		Map<String, Object> filter = new HashMap<String, Object>(pathParameters);
		if (pathParameters.containsKey(HttpParamsHelper.HTTP_PARAM_USER)) {
			user = userRepo.findUserWithId(pathParameters
					.getFirst(HttpParamsHelper.HTTP_PARAM_USER));
			logger.info("Found user for request: {}", user.getUserId());

			filter.put(HttpParamsHelper.HTTP_PARAM_USER, user);
		}
		
		filter.remove("limit");
		filter.remove("offset");

		RequestEntryCollection collection = new RequestEntryCollection();
		collection.addAll(reqRepo.findRequestWith(filter, limit, offset));
		return collection;
	}
	
	/**
	 * Gets the count of all the stored requests
	 * @return a number of stored requests
	 */
	public long getRequestCount() {
		return reqRepo.count();
	}
	
	/**
	 * Gets the count of all the stored requests of a user
	 * @param userId the user id
	 * @return a number of stored requests
	 */
	public long getRequestCountOfUser(String userId) {
		UserEntry user = userRepo.findUserWithId(userId);
		return reqRepo.count(RequestEntry.SERVICE_REQUEST_USER, user);
	}
}
