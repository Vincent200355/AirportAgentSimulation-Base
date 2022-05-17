package dhbw.sose2022.softwareengineering.airportagentsim.simulation.config;

final class SimpleRegistryEntry extends RegistryEntry {
	
	SimpleRegistryEntry(Class<?> target) {
		super(target);
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof SimpleRegistryEntry
				&& this.target.equals(((SimpleRegistryEntry) other).target);
	}
	
	@Override
	public int hashCode() {
		return this.target.hashCode();
	}
	
}
