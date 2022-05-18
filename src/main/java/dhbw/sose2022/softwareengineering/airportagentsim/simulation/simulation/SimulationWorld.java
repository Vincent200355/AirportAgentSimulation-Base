package dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.Logger;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.World;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.AirportAgentSimulationAPI;

public final class SimulationWorld implements World {
	
	@SuppressWarnings("unused")
	private final AirportAgentSim aas;
	private final Logger logger;
	
	private final int width;
	private final int height;
	private final ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public SimulationWorld(AirportAgentSim aas, Logger logger, int width, int height) {
		this.aas = aas;
		this.logger = logger;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}
	
	@Override
	public int getHeight() {
		return this.height;
	}
	
	@Override
	public Collection<Entity> getEntities() {
		return this.entities;
	}
	
	@Override
	public void add(Entity e) {
		this.entities.add(e);
	}
	
	@Override
	public void addAll(Collection<? extends Entity> c) {
		this.entities.addAll(c);
	}
	
	@Override
	public void remove(Entity e) {
		this.entities.remove(e);
	}
	
	public void update() {
		
		for(Entity entity : this.entities) {
			try {
				entity.update();
			} catch(Exception e) {
				this.logger.warn("Failed to update entity of type " + entity.getClass().getSimpleName() + " from plugin \"" + AirportAgentSimulationAPI.getLoadedPlugin(entity.getPlugin()).getName() + "\"", e);
			}
		}
		
	}
	
}
