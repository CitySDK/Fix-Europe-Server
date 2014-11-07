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

import eu.citysdk.participation.dao.UserEntryRepository;
import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.model.UserEntry;

public class UserEntryTests {
	private static final String USERNAME = "user";
	private static final String USER_EMAIL = "email@email.com";

	private static Mongo mongo;
	private static Morphia morphia;
	private static UserEntryRepository userRepository;
	
	@BeforeClass
    public static void setup() throws UnknownHostException, MongoException {
		mongo = new Mongo(MongoHelper.HOST, MongoHelper.PORT);
		morphia = new Morphia();
		morphia.map(UserEntry.class);
		userRepository = new UserEntryRepository(mongo, morphia);
    }
	
	@AfterClass
	public static void tearDown() {
		mongo.close();
	}
	
	@Before
	public void init() {
		userRepository.deleteAll();
	}
	
	@Test
	public void testSaveUser() {
		UserEntry user = new UserEntry();
		user.setUserId(USERNAME);
		user.setUserEmail(USER_EMAIL);
		
		userRepository.save(user);
		
		user = userRepository.findUserWithId(USERNAME);
		
		assertEquals(USERNAME, user.getUserId());
		assertEquals(USER_EMAIL, user.getUserEmail());
	}
	
	@Test
	public void testFindAll() {
		for (int i = 0; i < 10; i++) {
			UserEntry entry = new UserEntry();
			entry.setUserEmail("" + i + "@gmail.com");
			entry.setUserId("" + i);
			
			userRepository.save(entry);
		}
		
		List<UserEntry> entries = userRepository.findAll();
		assertEquals(10, entries.size());
		
		int i = 0;
		for (UserEntry entry : entries) {
			assertEquals(entry.getUserId(), "" + i);
			assertEquals(entry.getUserEmail(), "" + i + "@gmail.com");
			i++;
		}
	}
	
	@Test
	public void testDeleteByUsername() {
		UserEntry user = new UserEntry();
		user.setUserId(USERNAME);
		user.setUserEmail(USER_EMAIL);
		
		userRepository.save(user);
		
		user = userRepository.findUserWithId(USERNAME);
		
		assertEquals(USERNAME, user.getUserId());
		assertEquals(USER_EMAIL, user.getUserEmail());
		
		userRepository.deleteByUsername(USERNAME);
		
		user = userRepository.findUserWithId(USERNAME);
		
		assertEquals("", user.getUserId());
		assertEquals("", user.getUserEmail());
	}
}
