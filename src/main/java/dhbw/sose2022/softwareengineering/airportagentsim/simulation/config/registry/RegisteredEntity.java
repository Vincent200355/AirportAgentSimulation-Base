package dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.registry;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.LoadedPlugin;

record RegisteredEntity(LoadedPlugin plugin,
						Class<? extends Entity> entityType) {

	LoadedPlugin getPlugin() {
		return this.plugin;
	}

	Class<? extends Entity> getEntityType() {
		return this.entityType;
	}
}