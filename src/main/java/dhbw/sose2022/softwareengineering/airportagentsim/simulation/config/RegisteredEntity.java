package dhbw.sose2022.softwareengineering.airportagentsim.simulation.config;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.LoadedPlugin;

final class RegisteredEntity {
	
	private final LoadedPlugin plugin;
	private final Class<? extends Entity> entityType;
	
	RegisteredEntity(LoadedPlugin plugin, Class<? extends Entity> entityType) {
		this.plugin = plugin;
		this.entityType = entityType;
	}
	
	LoadedPlugin getPlugin() {
		return this.plugin;
	}
	
	Class<? extends Entity> getEntityType() {
		return this.entityType;
	}
	
}
