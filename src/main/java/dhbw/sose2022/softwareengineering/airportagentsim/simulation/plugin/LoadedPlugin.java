package dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.plugin.Plugin;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.logging.Log4jPluginLogger;

public class LoadedPlugin {
	
	private final PluginManager pluginManager;
	
	private final String id;
	private final String name;
	private final String[] authors;
	
	private final PluginClassLoader pluginClassLoader;
	private final Plugin plugin;
	private final HashSet<String> dependencies;
	
	private final Log4jPluginLogger pluginLogger;
	
	public LoadedPlugin(PluginManager pluginManager, String id, String name, String[] authors, PluginClassLoader pluginClassLoader, Plugin plugin, HashSet<String> dependencies) {
		
		this.pluginManager = pluginManager;
		
		this.id = id;
		this.name = name;
		this.authors = authors;
		this.pluginClassLoader = pluginClassLoader;
		this.plugin = plugin;
		this.dependencies = dependencies;
		
		this.pluginLogger = initLogger0();
		
	}
	
	public PluginManager getPluginManager() {
		return this.pluginManager;
	}
	
	public String getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String[] getAuthors() {
		return this.authors.clone();
	}
	
	public String getStringifiedAuthors() {
		if(this.authors.length == 1)
			return this.authors[0];
		StringBuilder sb = new StringBuilder(this.authors[0]);
		for(int i = 1; i < this.authors.length - 1; i++) {
			sb.append(", ");
			sb.append(this.authors[i]);
		}
		sb.append(" and ");
		sb.append(this.authors[this.authors.length - 1]);
		return sb.toString();
	}
	
	public PluginClassLoader getPluginClassLoader() {
		return this.pluginClassLoader;
	}
	
	public Plugin getPlugin() {
		return this.plugin;
	}
	
	public Set<String> getDependencies() {
		return new HashSet<String>(this.dependencies);
	}
	
	public Log4jPluginLogger getPluginLogger() {
		return this.pluginLogger;
	}
	
	
	private Log4jPluginLogger initLogger0() {
		Logger log4jLogger = this.pluginManager.getPluginLogger();
		Marker log4jMarker = this.pluginManager.getPluginMarker(this.id, this.name);
		return new Log4jPluginLogger(log4jLogger, log4jMarker);
	}
	
}
