package dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin;

import java.net.URL;
import java.net.URLClassLoader;

public final class PluginClassLoader extends URLClassLoader {
	
	private LoadedPlugin loadedPlugin;
	
	public PluginClassLoader(URL jarURL) {
		super(new URL[] {jarURL});
	}
	
	public LoadedPlugin getLoadedPlugin() {
		return this.loadedPlugin;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		
		if(name.startsWith("dhbw.sose2022.softwareengineering.airportagentsim.simulation."))
			throw new ClassNotFoundException();
		
		return super.findClass(name);
		
	}
	
	
	void setLoadedPlugin(LoadedPlugin loadedPlugin) {
		if(this.loadedPlugin != null)
			throw new UnsupportedOperationException();
		this.loadedPlugin = loadedPlugin;
	}
	
}
