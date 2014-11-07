package eu.citysdk.participation.rest.request;

import eu.citysdk.participation.model.RequestEntry;


/**
 * POST Request body
 * 
 * @author Pedro Cruz
 *
 */
public class RequestMessageBody {
	private RequestEntry request;
	private String user;
	
	public RequestMessageBody() {
		request = new RequestEntry();
		user = "";
	}
	
	public RequestEntry getRequest() {
		return request;
	}
	
	public String getUser() {
		return user;
	}
	
	public RequestMessageBody setRequest(RequestEntry request) {
		this.request = request;
		return this;
	}
	
	public RequestMessageBody setUser(String user) {
		this.user = user;
		return this;
	}
}
