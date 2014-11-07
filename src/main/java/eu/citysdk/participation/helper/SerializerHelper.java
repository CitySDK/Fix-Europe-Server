package eu.citysdk.participation.helper;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import eu.citysdk.participation.rest.hal.HalMessage;

public class SerializerHelper {
	static {
		SerializerHelper helper = new SerializerHelper();
		builder = new GsonBuilder()
				.excludeFieldsWithModifiers(Modifier.TRANSIENT)
				.setFieldNamingPolicy(
						FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.registerTypeAdapter(HalMessage.class, helper.new HalResourceSerializer());
	}
	
	public static final GsonBuilder builder;

	class HalResourceSerializer implements JsonSerializer<HalMessage> {

		public HalResourceSerializer() { }

		@Override
		public JsonElement serialize(HalMessage model, Type typeOfSrc,
				JsonSerializationContext context) {
			JsonObject resource = (JsonObject) model.getBaseResource();
			if (model.hasEmbeddedResource()) {
				JsonObject embedded = model.getEmbeddedResource();
				resource.add(HalMessage.EMBEDDED, embedded);
			}
			
			if (model.hasLinks()) {
				JsonElement links = model.getLinkResource();
				resource.add(HalMessage.LINKS, links);
			}
			
			return resource;
		}
	}
}
