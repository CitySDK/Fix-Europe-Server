package eu.citysdk.participation.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;

import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.model.NullRequestEntry;
import eu.citysdk.participation.model.RequestEntry;

/**
 * DAO for {@link eu.citysdk.participation.model.RequestEntry} entries
 * 
 * @author Pedro Cruz
 * 
 */
public class RequestEntryRepository extends BasicDAO<RequestEntry, ObjectId> {
	private static final int GET_ALL = -1;

	public RequestEntryRepository(Mongo mongo, Morphia morphia) {
		super(mongo, morphia, MongoHelper.DB_NAME);
	}

	/**
	 * Adds all requests
	 * 
	 * @param entries
	 *            a list of {@link eu.citysdk.participation.model.RequestEntry}
	 *            to be added
	 */
	public void saveAll(List<RequestEntry> entries) {
		for (RequestEntry entry : entries) {
			ds.save(entry);
		}
	}

	/**
	 * Gets all the stored requests
	 * 
	 * @return a list of {@link eu.citysdk.participation.model.RequestEntry}
	 */
	public List<RequestEntry> findAll() {
		return ds.find(RequestEntry.class).order(RequestEntry.SERVICE_REQUEST_DATETIME).asList();
	}

	/**
	 * Gets a request entry with a given id
	 * 
	 * @return a {@link eu.citysdk.participation.model.RequestEntry}
	 */
	public RequestEntry findRequestWithId(String id) {
		List<RequestEntry> entries = ds.find(RequestEntry.class)
				.filter(RequestEntry.SERVICE_REQUEST_ID, id).asList();
		if (entries.size() > 0)
			return entries.get(0);

		return new NullRequestEntry();
	}

	/**
	 * Gets all requests matching the given parameters
	 * 
	 * @param parameters
	 *            the filtering parameters
	 * @param offset
	 *            pagination system
	 * @param limit
	 *            pagination system
	 * @return a list of {@link eu.citysdk.participation.model.RequestEntry}
	 */
	public List<RequestEntry> findRequestWith(Map<String, Object> parameters,
			int limit, int offset) {
		Query<RequestEntry> query = ds.createQuery(RequestEntry.class);
		Set<String> keys = parameters.keySet();
		Criteria[] criterias = new Criteria[keys.size()];

		int i = 0;
		for (String key : keys) {
			criterias[i++] = query.criteria(key).equal(parameters.get(key));
		}

		query.and(criterias);

		if (limit == GET_ALL)
			return query.order(RequestEntry.SERVICE_REQUEST_DATETIME).asList();
		else
			return query.order(RequestEntry.SERVICE_REQUEST_DATETIME).offset(offset).limit(limit).asList();
	}

	/**
	 * Deletes all entries
	 */
	public void deleteAll() {
		ds.delete(ds.createQuery(RequestEntry.class));
	}

	/**
	 * Deletes a request with a given ID
	 * 
	 * @param requestId
	 *            the id of the request entry
	 */
	public void deleteRequestById(String requestId) {
		RequestEntry entry = findRequestWithId(requestId);
		ds.delete(entry);
	}

	/**
	 * Counts the number of reported requests between two dates
	 * 
	 * @param start
	 *            the start date
	 * @param end
	 *            the end date
	 * @return the number of reported requests between two dates
	 */
	public long countOpenRequestsBetween(Date start, Date end) {
		return countRequestsBetween(start, end, "open");
	}
	
	/**
	 * Counts the number of closed requests between two dates
	 * 
	 * @param start
	 *            the start date
	 * @param end
	 *            the end date
	 * @return the number of reported requests between two dates
	 */
	public long countClosedRequestsBetween(Date start, Date end) {
		return countRequestsBetween(start, end, "closed");
	}
	
	private long countRequestsBetween(Date start, Date end, String state) {
		Query<RequestEntry> query = ds.createQuery(RequestEntry.class);
		Criteria gt = query.criteria(RequestEntry.SERVICE_REQUEST_DATETIME).greaterThan(start);
		Criteria lte = query.criteria(RequestEntry.SERVICE_REQUEST_DATETIME).lessThanOrEq(end);
		Criteria status = query.criteria(RequestEntry.SERVICE_REQUEST_STATUS).equal(state);
		query.and(gt, lte, status);
		
		return query.countAll();
	}
}
