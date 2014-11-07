package eu.citysdk.participation.rest.hal;

import com.google.gson.JsonObject;

public class NullHalResource implements HalResource {

	@Override
	public JsonObject getMinimalRepresentation() {
		return new JsonObject();
	}

}
