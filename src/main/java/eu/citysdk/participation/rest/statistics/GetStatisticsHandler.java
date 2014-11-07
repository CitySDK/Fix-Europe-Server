package eu.citysdk.participation.rest.statistics;

import java.net.UnknownHostException;

import org.joda.time.LocalDate;

import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.citysdk.participation.base.Statistics;
import eu.citysdk.participation.dao.RequestEntryRepository;
import eu.citysdk.participation.dao.UserEntryRepository;
import eu.citysdk.participation.helper.MongoHelper;
import eu.citysdk.participation.model.AccessTokenEntry;
import eu.citysdk.participation.model.UserEntry;

public class GetStatisticsHandler {
	private Mongo mongo;
	private Morphia morphia;
	private RequestEntryRepository reqRepo;
	private UserEntryRepository userRepo;

	public GetStatisticsHandler() throws UnknownHostException, MongoException {
		mongo = new Mongo(MongoHelper.HOST, MongoHelper.PORT);
		morphia = new Morphia();
		morphia.map(UserEntry.class).map(AccessTokenEntry.class);

		reqRepo = new RequestEntryRepository(mongo, morphia);
		userRepo = new UserEntryRepository(mongo, morphia);
	}

	/**
	 * Gets the general request statistics
	 * 
	 * @param pathParameters
	 *            get statistics following a given set of parameters
	 * @return a {@link eu.citysdk.participation.base.Statistics}
	 */
	public Statistics getStats() {
		LocalDate today = new LocalDate();
		LocalDate week = today.dayOfWeek().withMinimumValue();
		LocalDate month = today.minusMonths(1);
		
		long reportedToday = reqRepo.countOpenRequestsBetween(today.toDate(),
				today.toDate());
		long reportedWeek = reqRepo.countOpenRequestsBetween(week.toDate(),
				today.toDate());
		long closedWeek = reqRepo.countClosedRequestsBetween(week.toDate(),
				today.toDate());
		long closedMonth = reqRepo.countClosedRequestsBetween(month
				.monthOfYear().withMinimumValue().toDate(), month.monthOfYear()
				.withMaximumValue().toDate());

		Statistics stats = new Statistics();
		stats.setReportedToday(reportedToday);
		stats.setReportedWeek(reportedWeek);
		stats.setClosedWeek(closedWeek);
		stats.setClosedMonth(closedMonth);
		
		return stats;
	}
}
