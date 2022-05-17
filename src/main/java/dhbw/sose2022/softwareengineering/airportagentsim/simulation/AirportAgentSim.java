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
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.AirportAgentSimulationAPI;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.LoadedPlugin;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.PluginActivateException;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.PluginLoadException;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.PluginManager;

public final class AirportAgentSim {
	
	private final String log4jPrefix;
	private final Path pluginsDirectory;
	
	private final Logger logger;
	private final PluginManager pluginManager;
	private final AirportAgentSimulationAPI aasAPI;
	
	public AirportAgentSim(String log4jPrefix, Path pluginsDirectory) {
		
		Validate.notBlank(log4jPrefix, "log4j prefix cannot be blank");
		Validate.notNull(pluginsDirectory, "plugin directory cannot be null");
		
		this.log4jPrefix = log4jPrefix;
		this.pluginsDirectory = pluginsDirectory;
		
		this.logger = LogManager.getLogger(this.log4jPrefix + "/default");
		this.pluginManager = new PluginManager(this.logger, LogManager.getLogger(this.log4jPrefix + "/plugin/default"), MarkerManager.getMarker(this.log4jPrefix + "/plugin"), (s) -> MarkerManager.getMarker(this.log4jPrefix + "/plugin/" + s));
		this.aasAPI = new AirportAgentSimulationAPI(this);
		
	}
	
	public void run() {
		
		this.logger.info("Starting Airport Agent Simulation...");
		
		AirportAgentSimulation.setAPI(this.aasAPI);
		
		try {
			loadPluginDirectory(this.pluginsDirectory);
		} catch(IOException e) {
			logger.error("Failed to load plugins", e);
		}
		
		this.logger.info("Plugin loading complete. {} plugin(s) active.", this.pluginManager.getActivePluginCount());
		
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
	
}