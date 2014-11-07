package eu.citysdk.participation.helper;

import com.google.code.morphia.logging.MorphiaLoggerFactory;
import com.google.code.morphia.logging.slf4j.SLF4JLogrImplFactory;

public class MongoHelper {
	static {
		MorphiaLoggerFactory.registerLogger(SLF4JLogrImplFactory.class);
	}
	
	public static final int PORT = 27017;
	public static final String HOST = "127.0.0.1";
	public static final String DB_NAME = "requests";
	public static final String MODEL_PACKAGE = "eu.citysdk.participation.model";
}