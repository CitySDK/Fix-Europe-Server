package eu.citysdk.participation;

import java.net.UnknownHostException;
import java.text.ParseException;

import com.mongodb.MongoException;
import eu.citysdk.participation.model.RequestEntry;
import eu.citysdk.participation.rest.request.PostRequestsHandler;

public class RequestInput {
	private PostRequestsHandler handler;
	private final String[] requests = { "2dtl2polmq4ln70psudr",
			"3n1dssa6q3p3di5jl397", "3hj74jj5678v0orf9rko",
			"1a01d8s75l8u0trotu4u", "37bli450elroa741b4qq",
			"2evl4kjngdpmfbgls3cc" };
	private final String user = "pcruz7";

	public RequestInput() throws UnknownHostException, MongoException {
		handler = new PostRequestsHandler();
	}

	private void inputRequests() throws ParseException {
		for (String r : requests) {
			RequestEntry request = new RequestEntry();
			request.setServiceRequestId(r);
			handler.createRequestFor(user, request);
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, MongoException, ParseException {
		RequestInput input = new RequestInput();
		input.inputRequests();
	}
}
