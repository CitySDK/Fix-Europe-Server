package eu.citysdk.participation.model;

/**
 * Null user entry
 * 
 * @author Pedro Cruz
 *
 */
public class NullUserEntry extends UserEntry {
	public NullUserEntry() {
		super();
	}
	
	@Override
	public boolean isAuthenticated() {
		return false;
	}
	
	@Override
	public boolean isRegistered() {
		return false;
	}
}
