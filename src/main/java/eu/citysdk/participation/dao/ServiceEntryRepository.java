package eu.citysdk.participation.dao;

import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.Mongo;

import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.model.NullServiceEntry;
import eu.citysdk.participation.model.ServiceEntry;

/**
 * DAO for {@link eu.citysdk.participation.model.ServiceEntry} entries
 * 
 * @author Pedro Cruz
 * 
 */
public class ServiceEntryRepository extends BasicDAO<ServiceEntry, ObjectId> {
	public ServiceEntryRepository(Mongo mongo, Morphia morphia) {
		super(mongo, morphia, MongoHelper.DB_NAME);
	}

	/**
	 * Gets all the stored services
	 * 
	 * @return a list of {@link eu.citysdk.participation.model.ServiceEntry}
	 */
	public List<ServiceEntry> findAll() {
		return ds.find(ServiceEntry.class).asList();
	}

	/**
	 * Gets a service with a given service code
	 * 
	 * @param code
	 *            the code of the service
	 * @return a {@link eu.citysdk.participation.model.ServiceEntry}
	 */
	public ServiceEntry findServiceByCode(String code) {
		List<ServiceEntry> entries = ds.find(ServiceEntry.class)
				.filter(ServiceEntry.SERVICE_CODE, code).asList();
		if (entries.size() > 0)
			return entries.get(0);

		return new NullServiceEntry();
	}
}
