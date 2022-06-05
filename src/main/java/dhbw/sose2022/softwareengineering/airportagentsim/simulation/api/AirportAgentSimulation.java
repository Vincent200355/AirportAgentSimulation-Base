package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api;

import java.util.Random;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurableAttribute;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurationFormatException;
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
	
	/**
	 * Returns a {@link Random} for the use by the plugin.<br><br>
	 * 
	 * If plugins desire to create random numbers for any simulation-releated
	 * purpose, they are required to use the random number generator provided by
	 * this method. This ensures that the simulation is repeatable.<br><br>
	 * 
	 * Note that invoking this method will allocate a new seeded random number
	 * generator. Therefore, plugins should only invoke this method once and
	 * reuse the returned object, because obtaining new objects may cause the
	 * random number generator to reproduce the same values.<br><br>
	 * 
	 * @param plugin the plugin to get the random number generator for
	 * @return a random number generator
	 */
	public static Random getRandom(Plugin plugin) {
		return API.getRandom(plugin);
	}
	
	/**
	 * Returns the entity type ID for the given {@code type} or {@code null} if
	 * the given type is not registered entity type.<br><br>
	 * 
	 * @param type the entity type to check
	 * @return the entity type ID
	 */
	public static String getEntityID(Class<?> type) {
		return API.getEntityID(type);
	}
	
	/**
	 * Returns the entity type ID for the given {@code entity} or {@code null}
	 * if the given entity is not of a registered entity type.<br><br>
	 * 
	 * @param type the entity to check
	 * @return the entity type ID
	 */
	public static String getEntityID(Entity entity) {
		return API.getEntityID(entity);
	}
	
	/**
	 * Returns the plugin which registered the given entity type. If there is no
	 * plugin which registered the given entity type, this method will return
	 * {@code null}.<br><br>
	 * 
	 * @param entityTypeID the entity type ID to check
	 * @return the plugin which registered the entity type
	 */
	public static Plugin getPluginForEntityType(String entityTypeID) {
		return API.getPluginForEntityType(entityTypeID);
	}
	
	/**
	 * Registers a new configuration type.<br><br>
	 * 
	 * Any configuration type must satisfy all of the following requirements:
	 * <ul>
	 *   <li>The {@code type} must be {@code public}.</li>
	 *   <li>The {@code type} must be instantiable, i.e. not an interface or an
	 *   abstract class.</li>
	 *   <li>The {@code type} must define a {@code public} zero-argument
	 *   constructor</li>
	 *   <li>The {@code type} must be accessible.</li>
	 *   <li>The same {@code type} cannot be registered multiple times, even if
	 *   the parameters differ. It is, however, permitted to register the exact
	 *   same type multiple times to reduce issues originating from different
	 *   plugins using the same types.</li>
	 *   <li>Registration of primitive types is forbidden.</li>
	 * </ul>
	 * 
	 * Registering a configuration type will implicitly register all array types
	 * derived from that type.<br><br>
	 * 
	 * @throws ConfigurationFormatException if the configuration type definition
	 * is illegal for any reason
	 * 
	 * @param type the type to register
	 */
	public static void registerConfigurationType(Class<?> type) throws ConfigurationFormatException {
		API.registerConfigurationType(type);
	}
	
	/**
	 * Registers a new configuration type.<br><br>
	 * 
	 * Any configuration type must satisfy all of the following requirements:
	 * <ul>
	 *   <li>The {@code type} must be {@code public}.</li>
	 *   <li>The {@code type} must be instantiable, i.e. not an interface or an
	 *   abstract class.</li>
	 *   <li>The {@code type} must define a {@code public} constructor which
	 *   takes exactly {@code parameters.length} arguments which are of the
	 *   {@link ConfigurableAttribute#getType() types} defined by
	 *   {@code parameters}. Note that is insufficient to declare a constructor
	 *   to whose parameters the types defined by {@code parameters} are
	 *   assignable; the runtime types must match. It is, however, permitted to
	 *   register the exact same type multiple times to reduce issues
	 *   originating from different plugins using the same types.</li>
	 *   <li>The {@code type} must be accessible.</li>
	 *   <li>The same {@code type} cannot be registered multiple times, even if
	 *   the {@code parameters} differ.</li>
	 *   <li>Registration of primitive types is forbidden. This implies that
	 *   parameters of primitive types cannot be used. If using primitive types
	 *   is desired, an additional constructor must be defined for the purpose
	 *   of registering a class as a configuration type.</li>
	 * </ul>
	 * 
	 * Registering a configuration type will implicitly register all array types
	 * derived from that type.<br><br>
	 * 
	 * If the configuration type uses complex parameters, those complex types
	 * must also be registered as configuration types. However, the order of
	 * registration is irrelevant, i.e. it is explicitly not required that the
	 * type of a complex parameter is already registered when registering a
	 * dependent type. Therefore, self-dependencies and circular dependencies
	 * between types are permitted.<br><br>
	 * 
	 * @throws ConfigurationFormatException if the configuration type definition
	 * is illegal for any reason
	 * 
	 * @param type the type to register
	 * @param parameters the parameters used by the type
	 */
	public static void registerConfigurationType(Class<?> type, ConfigurableAttribute[] parameters) throws ConfigurationFormatException {
		API.registerConfigurationType(type, parameters);
	}
	
	/**
	 * Registers a new plugin {@link Entity}. This will register the entity type
	 * as a configuration type.<br><br>
	 * 
	 * The type will be identified using the given {@code entityTypeID}. The
	 * {@code entityTypeID} must be unique between all plugins.<br><br>
	 * 
	 * The {@code type} will be registered as a configuration type as if by
	 * invoking {@link AirportAgentSimulation#registerConfigurationType(Class)}.
	 * The restrictions for registration of configuration types apply.<br><br>
	 * 
	 * @throws ConfigurationFormatException if the configuration type definition
	 * is illegal for any reason
	 * 
	 * @param plugin the plugin to register the entity for
	 * @param entityTypeID the unique entity type ID for the given type
	 * @param type the type to register
	 */
	public static void registerEntity(Plugin plugin, String entityTypeID, Class<? extends Entity> type) throws ConfigurationFormatException {
		API.registerEntity(plugin, entityTypeID, type);
	}
	
	/**
	 * Registers a new plugin {@link Entity}. This will register the entity type
	 * as a configuration type.<br><br>
	 * 
	 * The type will be identified using the given {@code entityTypeID}. The
	 * {@code entityTypeID} must be unique between all plugins.<br><br>
	 * 
	 * The {@code type} will be registered as a configuration type as if by
	 * invoking
	 * {@link AirportAgentSimulation#registerConfigurationType(Class, ConfigurableAttribute[])}.
	 * The restrictions for registration of configuration types apply.<br><br>
	 * 
	 * @throws ConfigurationFormatException if the configuration type definition
	 * is illegal for any reason
	 * 
	 * @param plugin the plugin to register the entity for
	 * @param entityTypeID the unique entity type ID for the given type
	 * @param type the type to register
	 * @param parameters the parameters used by the type
	 */
	public static void registerEntity(Plugin plugin, String entityTypeID, Class<? extends Entity> type, ConfigurableAttribute[] parameters) throws ConfigurationFormatException {
		API.registerEntity(plugin, entityTypeID, type, parameters);
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
