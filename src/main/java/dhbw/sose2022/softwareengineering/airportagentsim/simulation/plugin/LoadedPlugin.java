package dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin;

import java.util.HashSet;
import java.util.Set;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.plugin.Plugin;

public class LoadedPlugin {
	
	private final String id;
	private final String name;
	private final String[] authors;
	
	private final PluginClassLoader pluginClassLoader;
	private final Plugin plugin;
	private final HashSet<String> dependencies;
	
	public LoadedPlugin(String id, String name, String[] authors, PluginClassLoader pluginClassLoader, Plugin plugin, HashSet<String> dependencies) {
		this.id = id;
		this.name = name;
		this.authors = authors;
		this.pluginClassLoader = pluginClassLoader;
		this.plugin = plugin;
		this.dependencies = dependencies;
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
	
}
