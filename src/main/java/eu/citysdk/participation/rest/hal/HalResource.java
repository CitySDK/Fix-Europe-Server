package eu.citysdk.participation.rest.hal;

import com.google.gson.JsonObject;

public interface HalResource {
	JsonObject getMinimalRepresentation();
	// TODO: getCompleteRepresentation
}
