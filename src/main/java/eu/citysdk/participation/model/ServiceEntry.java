package eu.citysdk.participation.model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;

/**
 * Basic service of a request
 * 
 * @author Pedro Cruz
 *
 */
@Entity(noClassnameStored=true)
public class ServiceEntry extends BaseEntry {
	public static final transient String SERVICE_CODE = "service_code";
	public static final transient String SERVICE_NAME = "service_name";
	public static final transient String SERVICE_DESCRIPTION = "description";
	public static final transient String SERVICE_METADATA = "metadata";
	public static final transient String SERVICE_TYPE = "type";
	public static final transient String SERVICE_KEYWORDS = "keywords";
	public static final transient String SERVICE_GROUP = "group";
	
	@Property(SERVICE_CODE)
	private transient String serviceCode;
	@Property(SERVICE_NAME)
	private String serviceName;
	@Property(SERVICE_DESCRIPTION)
	private String description;
	@Property(SERVICE_METADATA)
	private transient boolean metadata;
	@Property(SERVICE_TYPE)
	private transient String type;
	@Property(SERVICE_KEYWORDS)
	private transient String keywords;
	@Property(SERVICE_GROUP)
	private transient String group;
	
	public ServiceEntry() {
		this.serviceCode = "";
		this.serviceName = "";
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String serviceDescription) {
		this.description = serviceDescription;
	}

	public boolean isMetadata() {
		return metadata;
	}

	public String getType() {
		return type;
	}

	public String getKeywords() {
		return keywords;
	}

	public String getGroup() {
		return group;
	}

	public void setMetadata(boolean metadata) {
		this.metadata = metadata;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public boolean isRegistered() {
		return true;
	}
}
