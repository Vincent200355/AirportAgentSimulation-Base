package dhbw.sose2022.softwareengineering.airportagentsim.simulation.config;

abstract class RegistryEntry {
	
	final Class<?> target;
	
	RegistryEntry(Class<?> target) {
		this.target = target;
	}
	
}
