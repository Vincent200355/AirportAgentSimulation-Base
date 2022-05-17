package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config;

public class ConfigurationParseException extends Exception {
	
	private static final long serialVersionUID = -3834249880720664429L;
	
	
	public ConfigurationParseException() {
		super();
	}
	
	public ConfigurationParseException(String message) {
		super(message);
	}
	
	public ConfigurationParseException(Throwable cause) {
		super(cause);
	}
	
	public ConfigurationParseException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
