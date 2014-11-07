package eu.citysdk.participation.base;

import com.google.gson.annotations.SerializedName;

/**
 * CitySDK Participation request
 * 
 * @author Pedro Cruz
 *
 */
public class Request {
	private String serviceRequestId;
	private String description;
	private String requestedDatetime;
	private String updatedDatetime;
	private String status;
	private String address;
	@SerializedName("lat")
	private String latitude;
	@SerializedName("long")
	private String longitude;
	@SerializedName("service_code")
	private String serviceCode;
	
	public Request() {
		this.serviceRequestId = "";
		this.description = "";
	}
	
	public String getServiceRequestId() {
		return serviceRequestId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getRequestedDatetime() {
		return requestedDatetime;
	}
	
	public String getUpdatedDatetime() {
		return updatedDatetime;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public String getServiceCode() {
		return serviceCode;
	}
	
	public void setServiceRequestId(String serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setRequestedDatetime(String requestedDatetime) {
		this.requestedDatetime = requestedDatetime;
	}
	
	public void setUpdatedDatetime(String updatedDatetime) {
		this.updatedDatetime = updatedDatetime;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getStatus() {
		return status;
	}

	public String getAddress() {
		return address;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
