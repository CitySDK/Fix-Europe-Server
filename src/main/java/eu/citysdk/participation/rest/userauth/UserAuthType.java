package eu.citysdk.participation.rest.userauth;

public enum UserAuthType {
	TYPE_NONE("none"),
	TYPE_REG("reg"),
	TYPE_BASIC("basic"),
	TYPE_GOOGLE("google");
	
	String type;
	UserAuthType(String type) {
		this.type = type;
	}
	
	public boolean isEqual(String type) {
		return type.equals(this.type);
	}
}
