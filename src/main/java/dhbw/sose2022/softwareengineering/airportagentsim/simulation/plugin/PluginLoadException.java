package dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin;

public class PluginLoadException extends Exception {
	
	private static final long serialVersionUID = 2337253989632108880L;
	
	
	public PluginLoadException() {
		super();
	}
	
	public PluginLoadException(String message) {
		super(message);
	}
	
	public PluginLoadException(Throwable cause) {
		super(cause);
	}
	
	public PluginLoadException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
