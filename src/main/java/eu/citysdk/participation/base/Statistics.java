package eu.citysdk.participation.base;

import com.google.gson.JsonObject;

import eu.citysdk.participation.rest.hal.HalResource;

/**
 * Statistics mapping object
 * 
 * @author Pedro Cruz
 *
 */
public class Statistics implements HalResource {
	public transient static final String REPORTED_TODAY = "reported_today";
	public transient static final String REPORTED_WEEK = "reported_week";
	public transient static final String CLOSED_WEEK = "closed_week";
	public transient static final String CLOSED_MONTH = "closed_month";
	
	private long reportedToday;
	private long reportedWeek;
	private long closedWeek;
	private long closedMonth;

	public Statistics() {
		this.reportedToday = -1;
		this.reportedWeek = -1;
		this.closedMonth = -1;
		this.closedWeek = -1;
	}
	
	public void setReportedToday(long reportedToday) {
		this.reportedToday = reportedToday;
	}

	public void setReportedWeek(long reportedWeek) {
		this.reportedWeek = reportedWeek;
	}

	public void setClosedWeek(long closedWeek) {
		this.closedWeek = closedWeek;
	}

	public void setClosedMonth(long closedMonth) {
		this.closedMonth = closedMonth;
	}

	public long getReportedToday() {
		return reportedToday;
	}

	public long getReportedWeek() {
		return reportedWeek;
	}

	public long getClosedWeek() {
		return closedWeek;
	}

	public long getClosedMonth() {
		return closedMonth;
	}

	@Override
	public JsonObject getMinimalRepresentation() {
		JsonObject json = new JsonObject();
		json.addProperty(REPORTED_TODAY, reportedToday);
		json.addProperty(REPORTED_WEEK, reportedWeek);
		json.addProperty(CLOSED_WEEK, closedWeek);
		json.addProperty(CLOSED_MONTH, closedMonth);
		
		return json;
	}
}
