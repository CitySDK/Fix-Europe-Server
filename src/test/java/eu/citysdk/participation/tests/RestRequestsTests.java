package eu.citysdk.participation.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.code.morphia.Morphia;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.core.util.Base64;
import eu.citysdk.participation.dao.RequestEntryRepository;
import eu.citysdk.participation.dao.UserEntryRepository;
import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.model.RequestEntry;
import eu.citysdk.participation.model.UserEntry;
import eu.citysdk.participation.rest.hal.ServiceResponse;

public class RestRequestsTests {
	private static final String RESOURCE = "https://127.0.0.1:8443/participation/rest/requests";
	private static final String USER_PASS = "pcruz7:pass";
	private static final String[] REQUEST_ID = { "h15pptfbgp01uck4f1p8", "oqi6qgve1b2308ugo1np" };
	private static final String POST_REQUEST_ID = "14g9s0qsp3eb4mebbfpj";

	@BeforeClass
	public static void setup() {
		prepareSecurity();
		prepareDatabase();
	}

	private static void prepareSecurity() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}
		} };

		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				// System.out.println("Warning: URL Host: "+urlHostName+" vs. "+session.getPeerHost());
				return true;
			}
		};

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		} catch (Exception e) {

		}
	}

	private static void prepareDatabase() {
		try {
			Mongo mongo = new Mongo(MongoHelper.HOST, MongoHelper.PORT);
			Morphia morphia = new Morphia();
			morphia.map(UserEntry.class);
			
			UserEntryRepository userRepository = new UserEntryRepository(mongo,
					morphia);
			UserEntry user = new UserEntry();
			user.setUserId("pcruz7");
			user.setUserPassword("pass");
			userRepository.deleteAll();
			userRepository.save(user);

			morphia.map(RequestEntry.class);
			RequestEntryRepository requestRepository = new RequestEntryRepository(mongo,
					morphia);
			
			RequestEntry entry = new RequestEntry();
			entry.setServiceRequestId(REQUEST_ID[0]);
			entry.setUser(user);
			
			requestRepository.deleteAll();
			requestRepository.save(entry);
			
			entry = new RequestEntry();
			entry.setServiceRequestId(REQUEST_ID[1]);
			requestRepository.save(entry);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAllRequestsOk() {
		Client client = Client.create();

		WebResource webResource = client.resource(RESOURCE);
		ClientResponse response = webResource
				.accept("application/json")
				.get(ClientResponse.class);
		
		if (response.getStatus() == Status.OK.getStatusCode()) {
			String output = response.getEntity(String.class);

			Type listType = new TypeToken<ArrayList<RequestEntry>>() {
			}.getType();
			List<RequestEntry> list = new GsonBuilder()
					.excludeFieldsWithModifiers(Modifier.TRANSIENT)
					.setFieldNamingPolicy(
							FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
					.create().fromJson(output, listType);

			assertEquals(2, list.size());
			for (int i = 0; i < list.size(); i++) {
				assertEquals(REQUEST_ID[i], list.get(i).getServiceRequestId());
			}
		} else {
			fail("Status response is not 200-OK: " + response.getStatus());
		}
	}

	@Test
	public void testUserRequestsOk() {
		Client client = Client.create();

		WebResource webResource = client.resource(RESOURCE + "?user=pcruz7");

		ClientResponse response = webResource
				.accept("application/json")
				.header("authorization",
						"Basic " + new String(Base64.encode(USER_PASS)))
				.get(ClientResponse.class);

		if (response.getStatus() == Status.OK.getStatusCode()) {
			String output = response.getEntity(String.class);

			Type listType = new TypeToken<ArrayList<RequestEntry>>() {
			}.getType();
			List<RequestEntry> list = new GsonBuilder()
					.excludeFieldsWithModifiers(Modifier.TRANSIENT)
					.setFieldNamingPolicy(
							FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
					.create().fromJson(output, listType);

			assertEquals(1, list.size());
			assertEquals(REQUEST_ID[0], list.get(0).getServiceRequestId());
		} else {
			fail("Status response is not 200-OK: " + response.getStatus());
		}
	}

	@Test
	public void testPostRequestOk() {
		String request = "{ \"request\": { \"service_request_id\":\"" + POST_REQUEST_ID + "\", \"service_notice\":\"\" }, \"user\":\"pcruz7\" }";

		Client client = Client.create();
		WebResource webResource = client.resource(RESOURCE);

		ClientResponse response = webResource
				.accept("application/json")
				.header("authorization",
						"Basic " + new String(Base64.encode(USER_PASS)))
				.type(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, request);

		if (response.getStatus() == Status.OK.getStatusCode()) {
			String output = response.getEntity(String.class);

			ServiceResponse sr = new GsonBuilder()
					.excludeFieldsWithModifiers(Modifier.TRANSIENT)
					.setFieldNamingPolicy(
							FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
					.create().fromJson(output, ServiceResponse.class);

			assertEquals(Status.CREATED.getStatusCode(), sr.getCode());
			assertEquals("Request created", sr.getMessage());
		} else {
			fail("Status response is not 200-OK: " + response.getStatus());
		}
	}
}
