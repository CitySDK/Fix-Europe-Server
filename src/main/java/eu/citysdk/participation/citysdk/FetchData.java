package eu.citysdk.participation.citysdk;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import eu.citysdk.participation.base.NullRequest;
import eu.citysdk.participation.base.Request;
import eu.citysdk.participation.model.NullServiceEntry;
import eu.citysdk.participation.model.ServiceEntry;

/**
 * Fetches data from the CitySDK participation system
 * 
 * @author Pedro Cruz
 * 
 */
public class FetchData {
	private String endpoint;

	private static final Logger logger = LoggerFactory
			.getLogger(FetchData.class);

	private static final GsonBuilder builder = new GsonBuilder()
			.excludeFieldsWithModifiers(Modifier.TRANSIENT)
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

	public FetchData(String endpoint) {
		this.endpoint = endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * Fetches a service with a given code
	 * 
	 * @param code
	 *            the code of the service
	 * @return a {@link eu.citysdk.participation.model.ServiceEntry}
	 */
	public ServiceEntry fetchServiceWithCode(String code) {
		Client client = Client.create();
		String url = endpoint.replace("services.json", "services/" + code
				+ ".json");
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.get(ClientResponse.class);

		if (response.getClientResponseStatus().equals(Status.OK)) {
			String output = response.getEntity(String.class);

			logger.info("Fetched " + url);
			logger.debug(output);

			ServiceEntry service = builder.create().fromJson(output,
					ServiceEntry.class);

			return service;
		}

		return new NullServiceEntry();
	}

	/**
	 * Fetches a request with a given id
	 * 
	 * @param id
	 *            the id of the request
	 * @return a {@link eu.citysdk.participation.base.Request}
	 */
	public Request fetchRequestEntryWithId(String id) {
		Client client = Client.create();
		String url = endpoint.replace("requests.json", "requests/" + id
				+ ".json");
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.get(ClientResponse.class);

		if (response.getClientResponseStatus().equals(Status.OK)) {
			String output = response.getEntity(String.class);

			logger.info("Fetched " + url);
			logger.debug(output);

			Type type = new TypeToken<List<Request>>() {
			}.getType();
			List<Request> list = builder.create().fromJson(output, type);

			if (list.size() > 0) {
				return list.get(0);
			}
		}

		return new NullRequest();
	}
}
