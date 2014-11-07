package eu.citysdk.participation.rest.hal;

import com.google.gson.JsonObject;

public class NullEmbeddedResource extends HalEmbedded {

	public NullEmbeddedResource() {
		super("");
	}

	@Override
	public JsonObject getMinimalRepresentation() {
		return new JsonObject();
	}
}
