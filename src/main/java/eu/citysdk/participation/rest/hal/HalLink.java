package eu.citysdk.participation.rest.hal;

import com.google.gson.JsonObject;

public class HalLink implements HalResource {
	private String name;
	private String href;
	private boolean templated;

	public HalLink(String name) {
		this.name = name;
		this.templated = false;
	}
	
	public void setHref(String href) {
		this.href = href;
	}
	
	public void isTemplated() {
		this.templated = true;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public JsonObject getMinimalRepresentation() {
		JsonObject href = new JsonObject();
		href.addProperty("href", this.href);
		if (templated)
			href.addProperty("templated", templated);
		
		return href;
	}
}
