package eu.citysdk.participation.helper;

public class HttpParamsHelper {
	public static final String HTTP_PARAM_ID = "id";
	public static final String HTTP_PARAM_USER = "user";
	public static final String HTTP_PARAM_LIMIT = "limit";
	public static final String HTTP_PARAM_OFFSET = "offset";
	public static final String HTTP_PARAM_REGISTER = "reg";
	
	public static final String HTTP_TEMPLATED_REQUEST = "{?user,limit,offset}";
	public static final String HTTP_TEMPLATED_STATS = "{?user}";
	public static final String HTTP_TEMPLATED_REQUEST_ID = "{id}";
}
