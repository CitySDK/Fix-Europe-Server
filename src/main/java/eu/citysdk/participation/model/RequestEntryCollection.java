package eu.citysdk.participation.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import eu.citysdk.participation.helper.ResourceHelper;
import eu.citysdk.participation.rest.hal.HalResource;

public class RequestEntryCollection implements HalResource {
	private List<RequestEntry> list;
	
	public RequestEntryCollection() {
		this.list = new ArrayList<RequestEntry>();
	}
	
	public void addAll(List<RequestEntry> entries) {
		this.list.addAll(entries);
	}
	
	@Override
	public JsonObject getMinimalRepresentation() {
		JsonArray array = new JsonArray();
		for (RequestEntry entry : list) {
			array.add(entry.getMinimalRepresentation());
		}
	
		JsonObject requests = new JsonObject();
		requests.add(ResourceHelper.REQUESTS, array);
		
		return requests;
	}

	public int size() {
		return list.size();
	}
}
