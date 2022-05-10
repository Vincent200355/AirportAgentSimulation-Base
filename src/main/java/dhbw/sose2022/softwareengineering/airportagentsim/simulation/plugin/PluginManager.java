package dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import org.apache.commons.lang3.Validate;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.plugin.Plugin;

public final class PluginManager {
	
	private static final String PLUGIN_DESCRIPTOR_NAME = "plugin.txt";
	
	private final HashMap<String, LoadedPlugin> activePlugins = new HashMap<String, LoadedPlugin>();
	
	
	public LoadedPlugin loadPlugin(Path pluginPath) throws PluginLoadException {
		
		Validate.notNull(pluginPath, "Cannot load plugin from null path");
		Validate.isTrue(pluginPath.getNameCount() > 0 && pluginPath.getFileName().toString().toLowerCase().endsWith(".jar"), "Only .jar files may be loaded as plugin");
		
		URL jarURL;
		try {
			jarURL = pluginPath.toUri().toURL();
		} catch(MalformedURLException e) {
			throw new PluginLoadException("Failed to construct URL for plugin", e);
		}
		
		Properties pluginDescriptor = readPluginDescriptor(pluginPath);
		
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
		Plugin plugin = invokeMainClassConstructor(mainClass, mainClassConstructor);
		
		return new LoadedPlugin(id, name, authors, pluginClassLoader, plugin, dependencyIDSet);
		
	}
	
	public void activatePlugin(LoadedPlugin pl) throws PluginActivateException {
		
		for(String dependencyID : pl.getDependencies())
			if(!this.activePlugins.containsKey(dependencyID))
				throw new PluginActivateException("Cannot activate \"" + pl.getName() + "\", because the dependency with ID \"" + dependencyID + "\" is not present");
		
		if(this.activePlugins.putIfAbsent(pl.getID(), pl) != null)
			throw new PluginActivateException("Cannot activate \"" + pl.getName() + "\" with id \"" + pl.getID() + "\" by " + pl.getStringifiedAuthors() + ", because a plugin with the same ID is already active");
		
	}
	
	public boolean canActivate(LoadedPlugin pl) {
		for(String dependencyID : pl.getDependencies())
			if(!this.activePlugins.containsKey(dependencyID))
				return false;
		return true;
	}
	
	public int getActivePluginCount() {
		return this.activePlugins.size();
	}
	
	public HashSet<String> getActivePluginIDs() {
		return new HashSet<String>(this.activePlugins.keySet());
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
