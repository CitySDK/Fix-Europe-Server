package eu.citysdk.participation.rest.hal;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import eu.citysdk.participation.helper.HttpParamsHelper;
import eu.citysdk.participation.helper.HttpPathHelper;
import eu.citysdk.participation.helper.ResourceHelper;

public class RequestLinks implements HalLinks {
	private static final String FIND = "find";
	private static final String HREF = "href";
	private static final String SELF = "self";

	private String self;
	private List<HalLink> links;

	public RequestLinks() {
		self = "";
		links = new ArrayList<HalLink>();
	}

	public void setSelf(String self) {
		this.self = self;
	}

	@Override
	public JsonObject getMinimalRepresentation() {
		JsonObject links = new JsonObject();

		if (this.self != "") {
			JsonObject self = new JsonObject();
			self.addProperty(HREF, this.self);
			links.add(SELF, self);
		}

		for (HalLink link : this.links) {
			links.add(link.getName(), link.getMinimalRepresentation());
		}
		
		JsonObject find = new JsonObject();
		find.addProperty(HREF, HttpPathHelper.HTTP_PATH_BASE
				+ HttpPathHelper.HTTP_PATH_REQUEST
				+ HttpParamsHelper.HTTP_TEMPLATED_REQUEST);
		find.addProperty("templated", true);
		links.add(FIND, find);

		return links;
	}

	@Override
	public String getLinkResource() {
		return ResourceHelper.REQUESTS;
	}

	public void add(HalLink halLink) {
		links.add(halLink);
	}
}
