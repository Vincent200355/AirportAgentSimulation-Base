package dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.plugin.Plugin;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.function.Function;

public final class PluginManager {
	
	private static final String PLUGIN_DESCRIPTOR_NAME = "plugin.txt";
	
	
	private final Logger logger;
	
	private final Logger pluginLogger;
	private final Marker pluginMarker;
	private final Function<String, Marker> pluginChildMarkerSupplier;
	
	private final HashMap<String, LoadedPlugin> activePlugins = new HashMap<String, LoadedPlugin>();
	
	
	public PluginManager(Logger logger, Logger pluginLogger, Marker pluginMarker, Function<String, Marker> pluginChildMarkerSupplier) {
		this.logger = logger;
		this.pluginLogger = pluginLogger;
		this.pluginMarker = pluginMarker;
		this.pluginChildMarkerSupplier = pluginChildMarkerSupplier;
	}
	
	public LoadedPlugin loadPlugin(Path pluginPath) throws PluginLoadException {
		
		Validate.notNull(pluginPath, "Cannot load plugin from null path");
		Validate.isTrue(pluginPath.getNameCount() > 0 && pluginPath.getFileName().toString().toLowerCase().endsWith(".jar"), "Only .jar files may be loaded as plugin");
		
		this.logger.trace("Loading plugin from {}", pluginPath);
		
		URL jarURL;
		try {
			jarURL = pluginPath.toUri().toURL();
		} catch(MalformedURLException e) {
			throw new PluginLoadException("Failed to construct URL for plugin", e);
		}
		
		Properties pluginDescriptor = readPluginDescriptor(pluginPath);
		
		this.logger.trace("Plugin descriptor is {}", pluginDescriptor);
		
		String id = getDescriptorProperty(pluginDescriptor, "id", true);
		String name = getDescriptorProperty(pluginDescriptor, "name", true);
		String entrypoint = getDescriptorProperty(pluginDescriptor, "entrypoint", true);
		String[] authors = getDescriptorListProperty(pluginDescriptor, "authors", true);
		if(authors.length == 0)
			throw new PluginLoadException("Cannot have an empty author list");
		String[] dependencyIDs = getDescriptorListProperty(pluginDescriptor, "dependencies", false);
		if(dependencyIDs == null)
			dependencyIDs = new String[0];
		
		HashSet<String> dependencyIDSet = new HashSet<>();
		for(String s : dependencyIDs) {
			if(id.equals(s))
				throw new PluginLoadException("Cannot declare self-dependency");
			if(!dependencyIDSet.add(s))
				throw new PluginLoadException("Cannot have duplicate dependency for \"" + s + "\"");
		}
		
		if(!pluginDescriptor.isEmpty())
			throw new PluginLoadException("Unknown key \"" + pluginDescriptor.keys() + "\" in " + PLUGIN_DESCRIPTOR_NAME);
		
		PluginClassLoader pluginClassLoader = new PluginClassLoader(jarURL);
		Class<? extends Plugin> mainClass = readMainClass(pluginClassLoader, entrypoint);
		Constructor<? extends Plugin> mainClassConstructor = findMainClassConstructor(mainClass);
		
		this.logger.trace("Instantiating main class of \"{}\"", name);
		Plugin plugin = invokeMainClassConstructor(mainClass, mainClassConstructor);

		LoadedPlugin loadedPlugin = new LoadedPlugin(this, id, name, authors, pluginClassLoader, plugin, dependencyIDSet);
		pluginClassLoader.setLoadedPlugin(loadedPlugin);
		
		this.logger.trace("Loaded plugin \"{}\"", name);
		
		return loadedPlugin;
		
	}
	
	public void activatePlugin(LoadedPlugin pl) throws PluginActivateException {
		
		for(String dependencyID : pl.getDependencies())
			if(!this.activePlugins.containsKey(dependencyID))
				throw new PluginActivateException("Cannot activate \"" + pl.getName() + "\", because the dependency with ID \"" + dependencyID + "\" is not present");
		
		if(this.activePlugins.putIfAbsent(pl.getID(), pl) != null)
			throw new PluginActivateException("Cannot activate \"" + pl.getName() + "\" with id \"" + pl.getID() + "\" by " + pl.getStringifiedAuthors() + ", because a plugin with the same ID is already active");
		
		try {
			pl.getPlugin().activate();
		} catch(Throwable e) {
			this.activePlugins.remove(pl.getID());
			this.logger.warn("Failed to activate \"" + pl.getName() + "\"", e);
			return;
		}
		
		this.logger.trace("Activated plugin \"{}\"", pl.getName());
		
	}
	
	public boolean canActivate(LoadedPlugin pl) {
		for(String dependencyID : pl.getDependencies())
			if(!this.activePlugins.containsKey(dependencyID))
				return false;
		return true;
	}
	
	public Logger getPluginLogger() {
		return this.pluginLogger;
	}
	
	public Marker getPluginMarker(String pluginID, String pluginName) {
		pluginID = pluginID.replace("\\", "\\\\").replace("/", "\\/");
		Marker childMarker = this.pluginChildMarkerSupplier.apply(pluginID + "/" + pluginName);
		childMarker.setParents(this.pluginMarker);
		return childMarker;
	}
	
	public int getActivePluginCount() {
		return this.activePlugins.size();
	}
	
	public HashSet<String> getActivePluginIDs() {
		return new HashSet<String>(this.activePlugins.keySet());
	}
	
	public LoadedPlugin getActivePluginByID(String id) {
		return this.activePlugins.get(id);
	}
	
	
	private Properties readPluginDescriptor(Path pluginPath) throws PluginLoadException {
		
		Properties pluginDescriptor = new Properties();
		
		FileSystem pluginJARFS = null;
		try {
			
			pluginJARFS = FileSystems.newFileSystem(pluginPath, new HashMap<String, Object>(), null);
			Path pluginDescriptorPath = pluginJARFS.getPath(PLUGIN_DESCRIPTOR_NAME);
			
			pluginDescriptor.load(Files.newInputStream(pluginDescriptorPath, StandardOpenOption.READ));
			
			pluginJARFS.close();
			
		} catch(IOException e) {
			throw new PluginLoadException("Failed to open plugin JAR", e);
		} finally {
			if(pluginJARFS != null) {
				try {
					pluginJARFS.close();
				} catch(IOException e) {}
			}
		}
		
		return pluginDescriptor;
		
	}
	
	private Class<? extends Plugin> readMainClass(PluginClassLoader pluginClassLoader, String className) throws PluginLoadException {
		
		Class<? extends Plugin> mainClass;
		
		try {
			mainClass = pluginClassLoader.loadClass(className).asSubclass(Plugin.class);
		} catch(ClassNotFoundException e) {
			throw new PluginLoadException("Could not find class \"" + className + "\" in plugin JAR");
		} catch(ClassCastException e) {
			throw new PluginLoadException("The class " + className + " does not implement " + Plugin.class.getName());
		} catch(Exception | Error e) {
			throw new PluginLoadException("An unexpected error occurred while attempting to load class \"" + className + "\"", e);
		}
		
		if(!Modifier.isPublic(mainClass.getModifiers()))
			throw new PluginLoadException("The class " + mainClass.getName() + " is not declared public");
		
		if(Modifier.isAbstract(mainClass.getModifiers()))
			throw new PluginLoadException("The class " + mainClass.getName() + " is declared abstract");
		
		return mainClass;
		
	}
	
	private Constructor<? extends Plugin> findMainClassConstructor(Class<? extends Plugin> mainClass) throws PluginLoadException {
		
		Constructor<? extends Plugin> constructor;
		try {
			constructor = mainClass.getDeclaredConstructor();
		} catch(NoSuchMethodException e) {
			throw new PluginLoadException("Could not find zero argument constructor in " + mainClass.getName());
		}
		
		if(!Modifier.isPublic(constructor.getModifiers()))
			throw new PluginLoadException("The zero argument constructor of " + mainClass.getName() + " is not declared public");
		
		if(!constructor.canAccess(null))
			throw new PluginLoadException("The zero argument constructor of " + mainClass.getName() + " is inaccessible");
		
		return constructor;
		
	}
	
	private Plugin invokeMainClassConstructor(Class<? extends Plugin> mainClass, Constructor<? extends Plugin> constructor) throws PluginLoadException {
		try {
			return constructor.newInstance();
		} catch(InvocationTargetException e) {
			throw new PluginLoadException("The zero argument constructor of " + mainClass.getName() + " threw an exception", e.getCause());
		} catch(ExceptionInInitializerError e) {
			throw new PluginLoadException("An initializer for " + mainClass.getName() + " threw an exception", e.getCause());
		} catch(InstantiationException | IllegalAccessException e) {
			throw new PluginLoadException("An unexpected exception occurred whilest attempting to instantiate " + mainClass.getName(), e);
		}
	}
	
	private String getDescriptorProperty(Properties p, String key, boolean required) throws PluginLoadException {
		String s = p.getProperty(key);
		if(s == null) {
			if(required)
				throw new PluginLoadException("Missing required key \"" + key + "\" in " + PLUGIN_DESCRIPTOR_NAME);
			return null;
		}
		p.remove(key);
		return s;
	}
	
	private String[] getDescriptorListProperty(Properties p, String key, boolean required) throws PluginLoadException {
		String s = getDescriptorProperty(p, key, required);
		if(s == null)
			return null;
		if(s.isBlank())
			return new String[0];
		String[] arr = s.split(",", -1);
		for(int i = 0; i < arr.length; i++) {
			arr[i] = arr[i].trim();
			if(arr[i].isEmpty())
				throw new PluginLoadException("Cannot have blank list element in value for \"" + key + "\" in " + PLUGIN_DESCRIPTOR_NAME);
		}
		return arr;
	}
	
}
