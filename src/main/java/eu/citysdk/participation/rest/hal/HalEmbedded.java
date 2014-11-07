package eu.citysdk.participation.rest.hal;

import com.google.gson.JsonObject;

public class HalEmbedded implements HalResource {
	private HalResource resource;
	private HalLinks links;
	
	private String embeddedResourceName;
	
	public HalEmbedded(String embeddedResourceName) {
		this.embeddedResourceName = embeddedResourceName;
		this.resource = new NullHalResource();
		this.links = new NullLinks();
	}
	
	@Override
	public JsonObject getMinimalRepresentation() {
		JsonObject embedded = (JsonObject) resource.getMinimalRepresentation();
		JsonObject link = (JsonObject) links.getMinimalRepresentation();
		embedded.add(HalMessage.LINKS, link);
		
		return embedded;
	}

	public String getEmbeddedResourceName() {
		return embeddedResourceName;
	}

	public void setEmbeddedResourceName(String embeddedResourceName) {
		this.embeddedResourceName = embeddedResourceName;
	}

	public HalResource getResource() {
		return resource;
	}

	public HalLinks getLinks() {
		return links;
	}

	public void setResource(HalResource resource) {
		this.resource = resource;
	}

	public void setLinks(HalLinks links) {
		this.links = links;
	}
}
