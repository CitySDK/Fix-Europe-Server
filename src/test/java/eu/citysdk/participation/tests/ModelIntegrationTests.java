package eu.citysdk.participation.tests;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.citysdk.participation.dao.RequestEntryRepository;
import eu.citysdk.participation.dao.ServiceEntryRepository;
import eu.citysdk.participation.dao.UserEntryRepository;
import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.model.RequestEntry;
import eu.citysdk.participation.model.ServiceEntry;
import eu.citysdk.participation.model.UserEntry;

public class ModelIntegrationTests {
	private static final String USERNAME = "userone";
	private static final String USER_EMAIL = "emailone@email.com";

	private static final String[] REQUEST_IDS = { "14g9s0qsp3eb4mebbfpj",
			"3hj74jj5678v0orf9rko", "2cgta8j1qodo99ns4fii" };
	private static final String[] SERVICE_CODES = { "201", "206", "161" };
	private static final String[] SERVICE_NAMES = { "Vandalism", "Forests", "Potholes" };
	private static final String REQUEST_NOTICE = "";

	private static Mongo mongo;

	private static Morphia userMorphia;
	private static UserEntryRepository userRepository;

	private static Morphia requestMorphia;
	private static RequestEntryRepository requestRepository;

	private static Morphia serviceMorphia;
	private static ServiceEntryRepository serviceRepository;

	@BeforeClass
	public static void setup() throws UnknownHostException, MongoException {
		mongo = new Mongo(MongoHelper.HOST, MongoHelper.PORT);
		userMorphia = new Morphia();
		userMorphia.map(UserEntry.class);
		userRepository = new UserEntryRepository(mongo, userMorphia);

		requestMorphia = new Morphia();
		requestMorphia.map(RequestEntry.class);
		requestRepository = new RequestEntryRepository(mongo, requestMorphia);

		serviceMorphia = new Morphia();
		serviceMorphia.map(ServiceEntry.class);
		serviceRepository = new ServiceEntryRepository(mongo, serviceMorphia);
	}

	@AfterClass
	public static void tearDown() {
		mongo.close();
	}

	@Before
	public void init() {
		userRepository.deleteAll();
		requestRepository.deleteAll();
	}

	@Test
	public void testRelation() {
		UserEntry user = new UserEntry();
		user.setUserId(USERNAME);
		user.setUserEmail(USER_EMAIL);

		userRepository.save(user);

		user = userRepository.findUserWithId(USERNAME);

		assertEquals(USERNAME, user.getUserId());
		assertEquals(USER_EMAIL, user.getUserEmail());

		RequestEntry entry;
		int i = 0;
		for (String request : REQUEST_IDS) {
			ServiceEntry service = serviceRepository.findServiceByCode(SERVICE_CODES[i++]);
			
			entry = new RequestEntry();
			entry.setServiceRequestId(request);
			entry.setServiceRequestNotice(REQUEST_NOTICE);
			entry.setService(service);
			entry.setUser(user);

			requestRepository.save(entry);
		}

		i = 0;

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("user", user);

		List<RequestEntry> requests = requestRepository
				.findRequestWith(parameters, -1, 0);
		for (RequestEntry request : requests) {
			ServiceEntry service = serviceRepository.findServiceByCode(request.getService().getServiceCode());
			
			assertEquals(REQUEST_IDS[i], request.getServiceRequestId());
			assertEquals(REQUEST_NOTICE, request.getServiceRequestNotice());
			assertEquals(SERVICE_CODES[i], request.getService()
					.getServiceCode());
			assertEquals(SERVICE_NAMES[i++], service.getServiceName());
			assertEquals(user, request.getUser());
		}
	}
}
