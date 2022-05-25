package dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.Validate;
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
	
	/**
	 * Creates a new simulation world. Both the width and the height must be
	 * positive.<br><br>
	 * 
	 * @param aas the simulation
	 * @param logger the logger to use
	 * @param width the width of the world
	 * @param height the height of the world
	 */
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
		ArrayList<Entity> list = new ArrayList<Entity>();
		list.addAll(this.entities);
		return list;
	}
	
	@Override
	@Deprecated
	public void add(Entity e) {
		Validate.notNull(e);
		e.spawn(this, 0, 0, 1, 1);
	}
	
	@Override
	@Deprecated
	public void addAll(Collection<? extends Entity> c) {
		Validate.notNull(c);
		Validate.isTrue(c.size() <= this.width * this.height, "Cannot fit entities in world");
		Iterator<? extends Entity> iterator = c.iterator();
		for(int y = 0; y < this.height; y++) {
			for(int x = 0; x < this.width; x++) {
				if(!iterator.hasNext())
					return;
				Entity e = iterator.next();
				Validate.notNull(e);
				e.spawn(this, x, y, 1, 1);
			}
		}
	}
	
	@Override
	@Deprecated
	public void remove(Entity e) {
		Validate.notNull(e);
		e.kill();
	}
	
	public void addEntity(Entity e) {
		this.entities.add(e);
	}
	
	public void removeEntity(Entity e) {
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
