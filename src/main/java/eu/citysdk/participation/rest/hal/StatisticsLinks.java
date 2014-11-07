package eu.citysdk.participation.rest.hal;

import com.google.gson.JsonObject;

import eu.citysdk.participation.helper.HttpParamsHelper;
import eu.citysdk.participation.helper.HttpPathHelper;
import eu.citysdk.participation.helper.ResourceHelper;

public class StatisticsLinks implements HalLinks {
	private static final String FIND = "find";
	private static final String HREF = "href";
	
	public StatisticsLinks() { }
	
	@Override
	public JsonObject getMinimalRepresentation() {
		JsonObject links = new JsonObject();
		
		JsonObject find = new JsonObject();
		find.addProperty(HREF, HttpPathHelper.HTTP_PATH_BASE
				+ HttpPathHelper.HTTP_PATH_STATS
				+ HttpParamsHelper.HTTP_TEMPLATED_STATS);
		find.addProperty("templated", true);
		links.add(FIND, find);
		
		return links;
	}

	@Override
	public String getLinkResource() {
		return ResourceHelper.STATS;
	}
}
