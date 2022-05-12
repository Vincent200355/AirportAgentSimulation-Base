package dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.plugin.Plugin;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.logging.Log4jPluginLogger;

public final class AirportAgentSimulationAPI {		
	
	private final AirportAgentSim aas;
	
	public AirportAgentSimulationAPI(AirportAgentSim aas) {
		this.aas = aas;
	}
	
	public Log4jPluginLogger getLogger(Plugin plugin) {
		return getLoadedPlugin0(plugin).getPluginLogger();
	}
	
	public Logger getLog4jLogger(Plugin plugin) {
		return getLogger(plugin).getLog4jLogger();
	}
	
	public Marker getLog4jMarker(Plugin plugin) {
		return getLogger(plugin).getLog4jMarker();
	}
	
	
	private LoadedPlugin getLoadedPlugin0(Plugin plugin) {
		ClassLoader loader = plugin.getClass().getClassLoader();
		if(!(loader instanceof PluginClassLoader))
			throw new IllegalArgumentException("Plugin not loaded by AAS plugin manager");
		LoadedPlugin pl = ((PluginClassLoader) loader).getLoadedPlugin();
		if(pl == null)
			throw new IllegalArgumentException("Plugin not loaded by AAS plugin manager");
		return pl;
	}
	
}
