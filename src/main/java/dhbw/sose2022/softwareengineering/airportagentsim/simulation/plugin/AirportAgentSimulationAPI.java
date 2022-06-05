package dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin;

import java.util.Random;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurableAttribute;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurationFormatException;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.plugin.Plugin;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.logging.Log4jPluginLogger;

public final class AirportAgentSimulationAPI {		
	
	private final AirportAgentSim aas;
	
	public AirportAgentSimulationAPI(AirportAgentSim aas) {
		this.aas = aas;
	}
	
	public Log4jPluginLogger getLogger(Plugin plugin) {
		Validate.notNull(plugin);
		return getLoadedPlugin(plugin).getPluginLogger();
	}
	
	public Logger getLog4jLogger(Plugin plugin) {
		Validate.notNull(plugin);
		return getLogger(plugin).getLog4jLogger();
	}
	
	public Marker getLog4jMarker(Plugin plugin) {
		Validate.notNull(plugin);
		return getLogger(plugin).getLog4jMarker();
	}
	
	public Random getRandom(Plugin plugin) {
		long seed = Double.doubleToLongBits(Math.E * Math.PI); // TODO use the seed from the configuration
		return new Random(seed ^ getLoadedPlugin(plugin).hashCode());
	}
	
	public String getEntityID(Class<?> type) {
		Validate.notNull(type);
		return this.aas.getConfigurationTypeRegistry().getEntityID(type);
	}
	
	public String getEntityID(Entity entity) {
		Validate.notNull(entity);
		return getEntityID(entity.getClass());
	}
	
	public Plugin getPluginForEntityType(String entityTypeID) {
		Validate.notNull(entityTypeID);
		LoadedPlugin loadedPlugin = this.aas.getConfigurationTypeRegistry().getPluginByEntityID(entityTypeID);
		return loadedPlugin == null ? null : loadedPlugin.getPlugin();
	}
	
	public void registerConfigurationType(Class<?> type) throws ConfigurationFormatException {
		registerConfigurationType(type, new ConfigurableAttribute[0]);
	}
	
	public void registerConfigurationType(Class<?> type, ConfigurableAttribute[] parameters) throws ConfigurationFormatException {
		Validate.notNull(type);
		Validate.noNullElements(parameters);
		this.aas.getConfigurationTypeRegistry().registerConfigurationType(type, parameters);
	}
	
	public void registerEntity(Plugin plugin, String entityTypeID, Class<? extends Entity> type) throws ConfigurationFormatException {
		registerEntity(plugin, entityTypeID, type, new ConfigurableAttribute[0]);
	}
	
	public void registerEntity(Plugin plugin, String entityTypeID, Class<? extends Entity> type, ConfigurableAttribute[] parameters) throws ConfigurationFormatException {
		Validate.notNull(plugin);
		Validate.notNull(entityTypeID);
		Validate.notNull(type);
		Validate.noNullElements(parameters);
		if(this.aas.getConfigurationTypeRegistry().isEntityIDRegistered(entityTypeID))
			throw new IllegalArgumentException("Entity ID \"" + entityTypeID + "\" is already in use");
		this.aas.getConfigurationTypeRegistry().registerConfigurationType(type, parameters);
		this.aas.getConfigurationTypeRegistry().registerEntityID(getLoadedPlugin(plugin), entityTypeID, type);
	}
	
	
	public static LoadedPlugin getLoadedPlugin(Plugin plugin) {
		ClassLoader loader = plugin.getClass().getClassLoader();
		if(!(loader instanceof PluginClassLoader))
			throw new IllegalArgumentException("Plugin not loaded by AAS plugin manager");
		LoadedPlugin pl = ((PluginClassLoader) loader).getLoadedPlugin();
		if(pl == null)
			throw new IllegalArgumentException("Plugin not loaded by AAS plugin manager");
		return pl;
	}
	
}
