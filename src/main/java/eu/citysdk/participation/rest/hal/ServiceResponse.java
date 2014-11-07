package eu.citysdk.participation.rest.hal;

/**
 * General response
 * 
 * @author Pedro Cruz
 *
 */
public class ServiceResponse {
	private int code;
	private String message;
	
	public ServiceResponse() {
		this.code = -1;
		this.message = "";
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
