package eu.citysdk.participation.rest.request;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.citysdk.participation.base.Request;
import eu.citysdk.participation.citysdk.FetchData;
import eu.citysdk.participation.dao.RequestEntryRepository;
import eu.citysdk.participation.dao.ServiceEntryRepository;
import eu.citysdk.participation.dao.UserEntryRepository;
import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.model.AccessTokenEntry;
import eu.citysdk.participation.model.RequestEntry;
import eu.citysdk.participation.model.ServiceEntry;
import eu.citysdk.participation.model.UserEntry;

/**
 * Handles the POST requests
 * 
 * @author Pedro Cruz
 * 
 */
public class PostRequestsHandler {
	private static final String REQUESTS_ENDPOINT = "http://web4.cm-lisboa.pt/citySDK/v1/requests.json";
	private static final String SERVICES_ENDPOINT = "http://web4.cm-lisboa.pt/citySDK/v1/services.json";

	private Mongo mongo;
	private Morphia morphia;
	private UserEntryRepository userRepo;
	private RequestEntryRepository reqRepo;
	private ServiceEntryRepository serRepo;

	private final Logger logger = LoggerFactory.getLogger(PostRequestsHandler.class);
	
	public PostRequestsHandler() throws UnknownHostException, MongoException {
		mongo = new Mongo(MongoHelper.HOST, MongoHelper.PORT);
		morphia = new Morphia();
		morphia.map(UserEntry.class).map(AccessTokenEntry.class).map(ServiceEntry.class);

		userRepo = new UserEntryRepository(mongo, morphia);
		reqRepo = new RequestEntryRepository(mongo, morphia);
		serRepo = new ServiceEntryRepository(mongo, morphia);
	}

	public void createRequestFor(String username, RequestEntry requestEntry)
			throws ParseException {
		FetchData data = new FetchData(REQUESTS_ENDPOINT);

		Request cRequest = data.fetchRequestEntryWithId(requestEntry
				.getServiceRequestId());

		UserEntry user = userRepo.findUserWithId(username);
		ServiceEntry service = serRepo.findServiceByCode(cRequest
				.getServiceCode());
		
		if (!service.isRegistered()) {
			data = new FetchData(SERVICES_ENDPOINT);
			service = data.fetchServiceWithCode(cRequest.getServiceCode());
			serRepo.save(service);
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ") {
			private static final long serialVersionUID = -6162551464502711349L;

			public Date parse(String source, ParsePosition pos) {
				return super.parse(source.replaceFirst(":(?=[0-9]{2}$)", ""),
						pos);
			}
		};

		logger.info("Creating request for {} ({})", user.getUserId(), user.getId());
		
		RequestEntry request = new RequestEntry();
		request.setServiceRequestId(requestEntry.getServiceRequestId());
		request.setServiceRequestNotice(requestEntry.getServiceRequestNotice());
		request.setService(service);
		request.setServiceRequestDatetime(df.parse(cRequest
				.getRequestedDatetime()));
		request.setServiceRequestLatitude(cRequest.getLatitude());
		request.setServiceRequestLongitude(cRequest.getLongitude());
		request.setServiceRequestDescription(cRequest.getDescription());
		request.setServiceRequestStatus(cRequest.getStatus());
		request.setUser(user);

		reqRepo.save(request);
	}
}
