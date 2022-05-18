package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config;

public class ConfigurationFormatException extends Exception {
	
	private static final long serialVersionUID = -3834249880720664429L;
	
	
	public ConfigurationFormatException() {
		super();
	}
	
	public ConfigurationFormatException(String message) {
		super(message);
	}
	
	public ConfigurationFormatException(Throwable cause) {
		super(cause);
	}
	
	public ConfigurationFormatException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
