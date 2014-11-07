package eu.citysdk.participation.model;

import java.util.Date;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import com.google.gson.JsonObject;

import eu.citysdk.participation.rest.hal.HalResource;

/**
 * Basic request sent by the user
 * 
 * @author Pedro Cruz
 *
 */
@Entity(noClassnameStored=true)
public class RequestEntry extends BaseEntry implements HalResource {
	public static final transient String SERVICE_REQUEST_ID = "service_request_id";
	public static final transient String SERVICE_REQUEST_NOTICE = "service_request_notice";
	public static final transient String SERVICE_REQUEST_USER = "service_request_user";
	public static final transient String SERVICE_REQUEST_DESCRIPTION = "service_request_description";
	public static final transient String SERVICE_REQUEST_DATETIME = "service_request_datetime";
	public static final transient String SERVICE_REQUEST_STATUS = "service_request_status";
	public static final transient String SERVICE_REQUEST_LATITUDE = "service_request_latitude";
	public static final transient String SERVICE_REQUEST_LONGITUDE = "service_request_longitude";
	public static final transient String SERVICE_REQUEST_SERVICE_CODE = "service_request_service_code";
	
	@Property(SERVICE_REQUEST_ID)
	private String serviceRequestId;
	@Property(SERVICE_REQUEST_NOTICE)
	private String serviceRequestNotice;
	@Property(SERVICE_REQUEST_DESCRIPTION)
	private String serviceRequestDescription;
	@Property(SERVICE_REQUEST_DATETIME)
	private Date serviceRequestDatetime;
	@Property(SERVICE_REQUEST_LATITUDE)
	private String serviceRequestLatitude;
	@Property(SERVICE_REQUEST_LONGITUDE)
	private String serviceRequestLongitude;
	@Property(SERVICE_REQUEST_STATUS)
	private String serviceRequestStatus;
	@Reference(SERVICE_REQUEST_SERVICE_CODE)
	private ServiceEntry service;
	@Reference(SERVICE_REQUEST_USER)
	private transient UserEntry user;
	
	public RequestEntry() { 
		this.serviceRequestId = "";
		this.serviceRequestNotice = "";
		this.serviceRequestLatitude = "";
		this.serviceRequestLongitude = "";
		this.serviceRequestDescription = "";
		this.serviceRequestStatus = "";
	}

	public String getServiceRequestId() {
		return serviceRequestId;
	}

	public void setServiceRequestId(String serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}

	public String getServiceRequestNotice() {
		return serviceRequestNotice;
	}

	public void setServiceRequestNotice(String serviceRequestNotice) {
		this.serviceRequestNotice = serviceRequestNotice;
	}
	
	public String getServiceRequestDescription() {
		return serviceRequestDescription;
	}

	public void setServiceRequestDescription(String serviceRequestDescription) {
		this.serviceRequestDescription = serviceRequestDescription;
	}

	public Date getServiceRequestDatetime() {
		return serviceRequestDatetime;
	}

	public String getServiceRequestLatitude() {
		return serviceRequestLatitude;
	}

	public String getServiceRequestLongitude() {
		return serviceRequestLongitude;
	}

	public void setServiceRequestDatetime(Date serviceRequestDatetime) {
		this.serviceRequestDatetime = serviceRequestDatetime;
	}

	public void setServiceRequestLatitude(String serviceRequestLatitude) {
		this.serviceRequestLatitude = serviceRequestLatitude;
	}

	public void setServiceRequestLongitude(String serviceRequestLongitude) {
		this.serviceRequestLongitude = serviceRequestLongitude;
	}

	public String getServiceRequestStatus() {
		return serviceRequestStatus;
	}

	public void setServiceRequestStatus(String serviceRequestStatus) {
		this.serviceRequestStatus = serviceRequestStatus;
	}

	public UserEntry getUser() {
		return user;
	}

	public void setUser(UserEntry user) {
		this.user = user;
	}

	public ServiceEntry getService() {
		return service;
	}

	public void setService(ServiceEntry service) {
		this.service = service;
	}

	@Override
	public JsonObject getMinimalRepresentation() {
		JsonObject json = new JsonObject();
		json.addProperty(SERVICE_REQUEST_ID, serviceRequestId);
		json.addProperty(SERVICE_REQUEST_DESCRIPTION, serviceRequestDescription);
		json.addProperty(SERVICE_REQUEST_DATETIME, serviceRequestDatetime.toString());
		
		return json;
	}
}
