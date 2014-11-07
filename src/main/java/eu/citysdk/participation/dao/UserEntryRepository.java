package eu.citysdk.participation.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.google.code.morphia.query.UpdateResults;
import com.mongodb.Mongo;

import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.model.AccessTokenEntry;
import eu.citysdk.participation.model.NullUserEntry;
import eu.citysdk.participation.model.UserEntry;

/**
 * DAO for users
 * 
 * @author Pedro Cruz
 * 
 */
public class UserEntryRepository extends BasicDAO<UserEntry, ObjectId> {
	private Logger logger = LoggerFactory.getLogger(UserEntryRepository.class);
	
	public UserEntryRepository(Mongo mongo, Morphia morphia) {
		super(mongo, morphia, MongoHelper.DB_NAME);
	}

	/**
	 * Gets all the user entries
	 * 
	 * @return a list of {@link eu.citysdk.participation.model.UserEntry}
	 */
	public List<UserEntry> findAll() {
		return ds.find(UserEntry.class).asList();
	}

	/**
	 * Gets a user with a given id
	 * 
	 * @param userId
	 *            the id of the user
	 * @return a {@link eu.citysdk.participation.model.UserEntry} with the given
	 *         id
	 */
	public UserEntry findUserWithId(String userId) {
		return findUserWith(UserEntry.USER_ID, userId);
	}

	/**
	 * Gets a user with a given email address
	 * 
	 * @param email
	 *            the email address of the user
	 * @return a {@link eu.citysdk.participation.model.UserEntry} with the given
	 *         email
	 */
	public UserEntry findUserWithEmail(String email) {
		return findUserWith(UserEntry.USER_EMAIL, email);
	}

	private UserEntry findUserWith(String filter, String value) {
		List<UserEntry> entries = ds.find(UserEntry.class)
				.filter(filter, value).asList();

		if (!entries.isEmpty()) {
			return entries.get(0);			
		}		

		List<UserEntry> entriesMail = ds.find(UserEntry.class)
				.filter(UserEntry.USER_EMAIL, value).asList();

		if (!entriesMail.isEmpty()) {
			return entriesMail.get(0);			
		}				
		
		return new NullUserEntry();
	}

	/**
	 * Gets a user with a given set of credentials
	 * 
	 * @param user
	 *            user's username
	 * @param email
	 *            user's email
	 * @param password
	 *            user's password
	 * @return a {@link eu.citysdk.participation.model.UserEntry} if found or
	 *         its null object.
	 */
	public UserEntry findUserWithCredentials(String user, String password) {
		Query<UserEntry> query = ds.createQuery(UserEntry.class);
		Criteria userCriteria = query.criteria(UserEntry.USER_ID).equal(user);
		Criteria emailCriteria = query.criteria(UserEntry.USER_EMAIL).equal(
				user);
		Criteria passCriteria = query.criteria(UserEntry.USER_PASS).equal(
				password);

		query.and(passCriteria).or(userCriteria, emailCriteria);	
		List<UserEntry> list = query.asList();

		if (list.size() > 0)
			return list.get(0);

		return new NullUserEntry();
	}

	/**
	 * Gets a user with a given token
	 * 
	 * @param user
	 *            the username/user id
	 * @param token
	 *            the user's token
	 * @return a {@link eu.citysdk.participation.model.UserEntry} if found or
	 *         its null object.
	 */
	public UserEntry findUserWithToken(String user, AccessTokenEntry token) {
		Query<UserEntry> query = ds.createQuery(UserEntry.class);
		Criteria userCriteria = query.criteria(UserEntry.USER_ID).equal(user);
		Criteria emailCriteria = query.criteria(UserEntry.USER_EMAIL).equal(
				user);
		Criteria tokenCriteria = query.criteria(UserEntry.USER_TOKEN).equal(
				token);

		query.and(tokenCriteria).or(userCriteria, emailCriteria);

		List<UserEntry> list = query.asList();

		if (list.size() > 0)
			return list.get(0);

		return new NullUserEntry();
	}

	/**
	 * Associates a given token to a user
	 * 
	 * @param user
	 *            the user to update
	 * @param token
	 *            the user's token
	 * @return an updated {@link eu.citysdk.participation.UserEntry}
	 */
	public UserEntry addTokenToUser(UserEntry user, AccessTokenEntry token) {
		Query<UserEntry> query = ds.createQuery(UserEntry.class)
				.field(UserEntry.USER_ID).equal(user.getUserId());
		user.setUserToken(token);

		UpdateOperations<UserEntry> ops = getDatastore()
				.createUpdateOperations(UserEntry.class).set(
						UserEntry.USER_TOKEN, getDatastore().getKey(token));
		update(query, ops);

		UpdateResults<UserEntry> entry = ds.update(query, ops);
		if (entry.getUpdatedExisting())
			return findUserWithId(user.getUserId());

		return new NullUserEntry();
	}

	/**
	 * Deletes a token from a user
	 * 
	 * @param user
	 *            the user to remove the token from
	 */
	public void removeTokenFromUser(UserEntry user) {
		Query<UserEntry> query = ds.createQuery(UserEntry.class)
				.field(UserEntry.USER_ID).equal(user.getUserId());
		UpdateOperations<UserEntry> ops = getDatastore()
				.createUpdateOperations(UserEntry.class).unset(
						UserEntry.USER_TOKEN);
		ds.update(query, ops);
	}

	/**
	 * Deletes all entries
	 */
	public void deleteAll() {
		ds.delete(ds.createQuery(UserEntry.class));
	}

	/**
	 * Deletes a user with a given username
	 * 
	 * @param username
	 *            the username of the user
	 */
	public void deleteByUsername(String username) {
		UserEntry entry = findUserWithId(username);
		ds.delete(entry);
	}

	/**
	 * Checks if a username is registered
	 * 
	 * @param user
	 *            the username
	 * @return <code>true</code> if already registered, <code>false</code>
	 *         otherwise
	 */
	public boolean isUsernameRegistered(String user) {
		return findUserWithId(user).isAuthenticated(); // if NullUserEntry,
															// it will return
															// false
															// meaning no user
															// is registered
	}

	/**
	 * Checks if an email is registered
	 * 
	 * @param email
	 *            the user's email
	 * @return <code>true</code> if already registered, <code>false</code>
	 *         otherwise
	 */
	public boolean isEmailRegistered(String email) {
		return findUserWithEmail(email).isAuthenticated();
	}
}
