package eu.citysdk.participation.tests;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.citysdk.participation.dao.RequestEntryRepository;
import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.model.RequestEntry;

public class RequestEntryTests {
	private static final String REQUEST_ID = "14g9s0qsp3eb4mebbfpj";
	private static final String REQUEST_NOTICE = "";

	private static Mongo mongo;
	private static Morphia morphia;
	private static RequestEntryRepository requestRepository;
	
	@BeforeClass
    public static void setup() throws UnknownHostException, MongoException {
		mongo = new Mongo(MongoHelper.HOST, MongoHelper.PORT);
		morphia = new Morphia();
		morphia.map(RequestEntry.class);
		requestRepository = new RequestEntryRepository(mongo, morphia);
    }
	
	@AfterClass
	public static void tearDown() {
		mongo.close();
	}
	
	@Before
	public void init() {
		requestRepository.deleteAll();
	}
	
	@Test
	public void testSaveRequest() {
		RequestEntry request = new RequestEntry();
		request.setServiceRequestId(REQUEST_ID);
		request.setServiceRequestNotice(REQUEST_NOTICE);

		requestRepository.save(request);

		request = requestRepository.findRequestWithId(REQUEST_ID);
				
		assertEquals(REQUEST_ID, request.getServiceRequestId());
	}
	
	@Test
	public void testFindAll() {
		List<RequestEntry> entries = requestRepository.findAll();
		assertEquals(entries.size(), 0);
		
		int size = 10;
		for(int id = 0; id < size; id++) {
			RequestEntry entry = new RequestEntry();
			entry.setServiceRequestId("" + id);
			entry.setServiceRequestNotice(REQUEST_NOTICE);
			
			entries.add(entry);
		}
		
		requestRepository.saveAll(entries);
		assertEquals(entries.size(), size);
		
		for(int id = 0; id < size; id++) {
			RequestEntry entry = entries.get(id);
			assertEquals("" + id, entry.getServiceRequestId());
		}
	}

	@Test
	public void testDeleteEntry() {
		RequestEntry request = new RequestEntry();
		request.setServiceRequestId(REQUEST_ID);
		request.setServiceRequestNotice(REQUEST_NOTICE);

		requestRepository.save(request);

		request = requestRepository.findRequestWithId(REQUEST_ID);
				
		assertEquals(request.getServiceRequestId().equals(REQUEST_ID), true);
		
		requestRepository.deleteRequestById(REQUEST_ID);
		
		request = requestRepository.findRequestWithId(REQUEST_ID);
		
		assertEquals("", request.getServiceRequestId());
		assertEquals("", request.getServiceRequestNotice());
	}
}
