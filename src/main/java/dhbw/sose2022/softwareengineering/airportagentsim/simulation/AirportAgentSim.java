package dhbw.sose2022.softwareengineering.airportagentsim.simulation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.AirportAgentSimulation;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurationFormatException;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurationParseException;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.EntityConfiguration;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.SimulationConfiguration;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.registry.ConfigurationTypeRegistry;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.export.AirportSimExporter;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.AirportAgentSimulationAPI;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.LoadedPlugin;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.PluginActivateException;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.PluginLoadException;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.PluginManager;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation.SimulationWorld;

public final class AirportAgentSim {
	
	private final String log4jPrefix;
	private final Path pluginsDirectory;
	private final Path configurationFile;
	
	private final Logger logger;
	private final PluginManager pluginManager;
	private final ConfigurationTypeRegistry configurationTypeRegistry;
	private final AirportAgentSimulationAPI aasAPI;
	
	private SimulationConfiguration configuration;
	private SimulationWorld world;
	private AirportSimExporter exporter;
	
	private long simulationCycleDuration = 17;
	private boolean simulationPaused = false;
	
	public AirportAgentSim(String log4jPrefix, Path pluginsDirectory, Path configurationFile) {
		
		Validate.notBlank(log4jPrefix, "log4j prefix cannot be blank");
		Validate.notNull(pluginsDirectory, "plugin directory cannot be null");
		
		this.log4jPrefix = log4jPrefix;
		this.pluginsDirectory = pluginsDirectory;
		this.configurationFile = configurationFile;
		
		this.logger = LogManager.getLogger(this.log4jPrefix + "/default");
		this.pluginManager = new PluginManager(this.logger, LogManager.getLogger(this.log4jPrefix + "/plugin/default"), MarkerManager.getMarker(this.log4jPrefix + "/plugin"), (s) -> MarkerManager.getMarker(this.log4jPrefix + "/plugin/" + s));
		this.configurationTypeRegistry = new ConfigurationTypeRegistry();
		this.aasAPI = new AirportAgentSimulationAPI(this);
		
	}
	
	public void run() {
		
		this.logger.info("Starting Airport Agent Simulation...");
		
		AirportAgentSimulation.setAPI(this.aasAPI);
		
		try {
			loadPluginDirectory(this.pluginsDirectory);
		} catch(IOException e) {
			this.logger.error("Failed to load plugins", e);
		}
		
		this.logger.info("Plugin loading complete. {} plugin(s) active.", this.pluginManager.getActivePluginCount());
		
		this.logger.debug("Loading configuration from \"{}\"", this.configurationFile);
		try {
			this.configuration = new SimulationConfiguration(this.configurationFile);
		} catch(IOException e) {
			this.logger.error("Failed to load configuration from \"" + this.configurationFile.toString() + "\"", e);
			return;
		}
		this.logger.info("Configuration loading complete");
		
		this.logger.info("Creating world");
		int worldWidth = this.configuration.getWidth();
		int worldHeight = this.configuration.getHeight();
		if(worldWidth <= 0 || worldHeight <= 0) {
			this.logger.error("Illegal world dimensions: width={}, height={}", worldWidth, worldHeight);
			return;
		}
		this.world = new SimulationWorld(this, this.logger, worldWidth, worldHeight);
		this.exporter = new AirportSimExporter(Path.of("exports"), "csv", this);
		this.logger.info("Creating {} entity(s)", this.configuration.getPlacedEntities().length);
		for(EntityConfiguration entityConfig : this.configuration.getPlacedEntities()) {
			
			String entityTypeID = entityConfig.getEntityType();
			
			if(!this.configurationTypeRegistry.isEntityIDRegistered(entityTypeID)) {
				this.logger.warn("Unknown entity type \"{}\". This may be due to missing plugins. The entity will not be spawned", entityTypeID);
				continue;
			}
			
			this.logger.debug("Creating entity of type {} width width {} and height {}", entityTypeID, entityConfig.getWidth(), entityConfig.getHeight());
			Entity entity;
			try {
				entity = this.configurationTypeRegistry.parseEntity(entityTypeID, entityConfig.getPluginAttributes());
			} catch(ConfigurationFormatException e) {
				this.logger.warn("Failed to load entity. The entity will not be spawned", e);
				continue;
			} catch(ConfigurationParseException e) {
				this.logger.warn("Failed to load entity. The entity will not be spawned", e);
				continue;
			}
			
			try {
				entity.spawn(this.world, entityConfig.getPosition()[0], entityConfig.getPosition()[1], entityConfig.getWidth(), entityConfig.getHeight());
			} catch(Exception e) {
				this.logger.warn("Failed to spawn entity", e);
				continue;
			}
			
		}
		this.exporter.afterInit();
		
		long cur = System.currentTimeMillis();
		long nxt = cur;
		
		for(int cycle = 0; cycle < this.configuration.getDuration(); cycle++) {
			
			if(this.simulationPaused) {
				this.logger.info("Simulation paused");
				while(this.simulationPaused) {
					try {
						Thread.sleep(200);
					} catch(InterruptedException e) {}
				}
				cur = System.currentTimeMillis();
				nxt = cur;
				this.logger.info("Simulation continued");
			}
			
			nxt += this.simulationCycleDuration;
			
			this.logger.trace("Running simulation cycle {}", cycle);
			this.world.update();
			this.exporter.afterTick();
			
			if(cur < nxt) {
				while(true) {
					cur = System.currentTimeMillis();
					if(cur >= nxt)
						break;
					try {
						Thread.sleep(nxt - cur);
					} catch(InterruptedException e) {}
				}
			} else if(cur - 1000 >= nxt && this.simulationCycleDuration > 0) {
				this.logger.warn("Simulation lag detected. Simulation is {} second(s) behind.", ((cur - nxt) / 1000));
			}
			
		}
		
		this.logger.info("Simulation complete");
		
		this.logger.info("Exporting...");
		this.exporter.exportSimToCsv("airport-sim");
		this.exporter.exportConfigToJson("config");
		this.logger.info("Exporting complete");
		
		this.exporter.afterSimFinished();
		this.logger.info("Shutting down...");
		
	}
	
	public void loadPluginDirectory(Path dir) throws IOException {
		
		List<Path> pluginJARs = Files.list(dir)
				.filter(Files::isRegularFile)
				.filter((p) -> p.getFileName().toString().endsWith(".jar"))
				.collect(Collectors.toCollection(ArrayList::new));
		
		if(pluginJARs.isEmpty())
			return;
		
		this.logger.info("Loading plugins from \"{}\"...", dir);
		
		List<LoadedPlugin> preparedPlugins = new ArrayList<LoadedPlugin>();
		
		for(Path pluginJAR : pluginJARs) {
			try {
				LoadedPlugin p = this.pluginManager.loadPlugin(pluginJAR);
				this.logger.info("Loaded \"{}\" by {} from \"{}\"", p.getName(), p.getStringifiedAuthors(), pluginJAR);
				preparedPlugins.add(p);
			} catch(PluginLoadException e) {
				this.logger.warn("Failed to load \"" + pluginJAR + "\"", e);
			}
		}
		
		while(!preparedPlugins.isEmpty()) {
			
			boolean hasNew = false;
			
			Iterator<LoadedPlugin> iter = preparedPlugins.iterator();
			while(iter.hasNext()) {
				LoadedPlugin p = iter.next();
				if(this.pluginManager.canActivate(p)) {
					this.logger.info("Activating \"{}\"", p.getName());
					try {
						this.pluginManager.activatePlugin(p);
					} catch(PluginActivateException e) {
						this.logger.warn("Failed to activate \"" + p.getName() + "\"", e);
					}
					iter.remove();
					hasNew = true;
				}
			}
			
			if(!hasNew) {
				HashSet<String> activePlugins = this.pluginManager.getActivePluginIDs();
				iter = preparedPlugins.iterator();
				while(iter.hasNext()) {
					LoadedPlugin p = iter.next();
					Set<String> unsatisfiableDependencies = p.getDependencies();
					unsatisfiableDependencies.removeAll(activePlugins);
					this.logger.warn("Failed to activate \"{}\", because some dependencies cannot be satisfied: {}", p.getName(), unsatisfiableDependencies.stream().collect(Collectors.joining(", ")));
				}
				preparedPlugins.clear();
			}
			
		}
		
	}
	
	public PluginManager getPluginManager() {
		return this.pluginManager;
	}
	
	public ConfigurationTypeRegistry getConfigurationTypeRegistry() {
		return this.configurationTypeRegistry;
	}
	
	public SimulationConfiguration getConfiguration() {
		return this.configuration;
	}
	
	public SimulationWorld getWorld() {
		return this.world;
	}
	
	public void setSimulationCycleDuration(long durationMilliseconds) {
		this.simulationCycleDuration = durationMilliseconds;
	}
	
	public void setSimulationPaused(boolean paused) {
		this.simulationPaused = paused;
	}
	
}
