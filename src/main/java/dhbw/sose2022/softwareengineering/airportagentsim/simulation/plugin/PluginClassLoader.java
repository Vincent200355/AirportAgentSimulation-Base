package dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin;

import java.net.URL;
import java.net.URLClassLoader;

public final class PluginClassLoader extends URLClassLoader {
	
	public PluginClassLoader(URL jarURL) {
		super(new URL[] {jarURL});
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		
		if(name.startsWith("dhbw.sose2022.softwareengineering.airportagentsim.simulation."))
			throw new ClassNotFoundException();
		
		return super.findClass(name);
		
	}
	
}
