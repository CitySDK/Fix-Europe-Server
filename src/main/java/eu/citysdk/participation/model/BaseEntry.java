package eu.citysdk.participation.model;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Version;

/**
 * General MongoDB entry
 * 
 * @author Pedro Cruz
 *
 */
public abstract class BaseEntry {
	public transient static final String FIELD_ID = "id";
	public transient static final String FIELD_VERSION ="version";
	
	@Id
	@Property(FIELD_ID)
	protected transient ObjectId id;

	@Version
	@Property(FIELD_VERSION)
	private transient Long version;

	public BaseEntry() {
		super();
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
