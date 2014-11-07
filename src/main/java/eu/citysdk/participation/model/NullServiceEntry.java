package eu.citysdk.participation.model;

/**
 * Null service entry
 * 
 * @author Pedro Cruz
 *
 */
public class NullServiceEntry extends ServiceEntry {
	public NullServiceEntry() {
		super();
	}
	
	@Override
	public boolean isRegistered() {
		return false;
	}
}
