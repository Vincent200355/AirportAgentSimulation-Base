package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.logging.PluginLogger;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.plugin.Plugin;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.AirportAgentSimulationAPI;

public final class AirportAgentSimulation {
	
	private static AirportAgentSimulationAPI API;
	
	
	private AirportAgentSimulation() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Returns a {@link PluginLogger} for the given  {@code plugin}.<br><br>
	 * 
	 * The provided logger allows for simplified correct use of the Log4j
	 * logging mechanism.<br><br>
	 * 
	 * If full access to the Log4j API is desired, the methods
	 * {@link #getLog4jLogger(Plugin)} and {@link #getLog4jMarker(Plugin)}
	 * should be used instead.<br><br>
	 * 
	 * @param plugin the plugin to get the logger for
	 * 
	 * @return the logger
	 */
	public static PluginLogger getLogger(Plugin plugin) {
		return API.getLogger(plugin);
	}
	
	/**
	 * Returns the Log4j {@link Logger} for the given {@code plugin}.<br><br>
	 * 
	 * This method should only be used if {@link #getLogger(Plugin)} is
	 * insufficient for the desired purpose.<br><br>
	 * 
	 * If any plugin uses Log4j directly it must ensure that every logging
	 * operation uses the marker provided by {@link #getLog4jMarker(Plugin)}
	 * either directly or as parent of some other {@link Marker}.<br><br>
	 * 
	 * @param plugin the plugin to get the logger for
	 * 
	 * @return the log4j logger
	 */
	public static Logger getLog4jLogger(Plugin plugin) {
		return API.getLog4jLogger(plugin);
	}
	
	/**
	 * Returns the Log4j {@link Marker} for the given {@code plugin}<br><br>
	 * 
	 * This method should only be used if {@link #getLogger(Plugin)} is
	 * insufficient for the desired purpose.<br><br>
	 * 
	 * If any plugin uses Log4j directly it must ensure that every logging
	 * operation uses the marker provided by this method either directly or as
	 * parent of some other {@link Marker}.<br><br>
	 * 
	 * @param plugin the plugin to get the marker for
	 * 
	 * @return the log4j marker
	 */
	public static Marker getLog4jMarker(Plugin plugin) {
		return API.getLog4jMarker(plugin);
	}
	
	public static void registerEntity(Class<? extends Entity> e) {
		// TODO implement
	}
	
	/**
	 * Initializes the API.<br><br>
	 * 
	 * This method must not be called by any plugin.<br><br>
	 * 
	 * @param api the API
	 */
	@SuppressWarnings("exports")
	public static void setAPI(AirportAgentSimulationAPI api) {
		if(API != null)
			throw new UnsupportedOperationException("Cannot redefine plugin API");
		API = api;
	}
	
}
