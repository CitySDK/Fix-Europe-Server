package eu.citysdk.participation.rest.hal;

import com.google.gson.JsonObject;

public class NullLinks implements HalLinks {

	@Override
	public JsonObject getMinimalRepresentation() {
		return new JsonObject();
	}

	@Override
	public String getLinkResource() {
		return "_links";
	}

}
