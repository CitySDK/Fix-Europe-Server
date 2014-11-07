package eu.citysdk.participation.dao;

import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;

import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.model.AccessTokenEntry;
import eu.citysdk.participation.model.NullAccessTokenEntry;

public class TokenEntryRepository extends BasicDAO<AccessTokenEntry, ObjectId> {
	public TokenEntryRepository(Mongo mongo, Morphia morphia) {
		super(mongo, morphia, MongoHelper.DB_NAME);
	}

	/**
	 * Gets a given token
	 * 
	 * @param token
	 *            the access token
	 * @return {@link eu.citysdk.participation.model.AccessTokenEntry}
	 */
	public AccessTokenEntry findToken(String token) {
		List<AccessTokenEntry> list = ds.find(AccessTokenEntry.class)
				.filter(AccessTokenEntry.ACCESS_TOKEN, token).asList();

		if (list.size() > 0)
			return list.get(0);

		return new NullAccessTokenEntry();
	}

	/**
	 * Deletes a given token
	 * 
	 * @param token
	 *            the token to be deleted
	 */
	public void deleteToken(String token) {
		Query<AccessTokenEntry> query = ds.createQuery(AccessTokenEntry.class)
				.field(AccessTokenEntry.ACCESS_TOKEN).equal(token);
		ds.delete(query);
	}
}
