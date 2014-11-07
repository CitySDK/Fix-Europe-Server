package eu.citysdk.participation.rest.hal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * HAL Resource model
 * 
 * @author Pedro Cruz
 *
 */
public class HalMessage {
	public static final String LINKS = "_links";
	public static final String EMBEDDED = "_embedded";
	
	private HalResource baseResource;
	private HalEmbedded embeddedResource;
	private HalLinks links;
	
	public HalMessage() {
		baseResource = new NullHalResource();
		embeddedResource = new NullEmbeddedResource();
		links = new NullLinks();
	}
	
	public boolean hasEmbeddedResource() {
		return !(embeddedResource instanceof NullEmbeddedResource);
	}
	
	public boolean hasLinks() {
		return !(links instanceof NullLinks);
	}

	public JsonElement getBaseResource() {
		return baseResource.getMinimalRepresentation();
	}

	public void setBaseResource(HalResource baseResource) {
		this.baseResource = baseResource;
	}

	public JsonObject getEmbeddedResource() {
		JsonObject resource = (JsonObject) embeddedResource.getMinimalRepresentation();
		JsonObject embedded = new JsonObject();
		embedded.add(embeddedResource.getEmbeddedResourceName(), resource);
		
		return embedded;
	}

	public void setEmbeddedResource(HalEmbedded embeddedResource) {
		this.embeddedResource = embeddedResource;
	}

	public void setLinks(HalLinks links) {
		this.links = links;
	}

	public JsonElement getLinkResource() {
		return links.getMinimalRepresentation();
	}
}
