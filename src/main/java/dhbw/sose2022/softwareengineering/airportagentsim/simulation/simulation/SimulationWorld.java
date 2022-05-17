package dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation;

import java.util.Collection;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.World;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;

public final class SimulationWorld implements World {
	
	public int width;
	public int height;
	public Collection<Entity> entities;
	
	public SimulationWorld(int width, int height) {
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
	
}
