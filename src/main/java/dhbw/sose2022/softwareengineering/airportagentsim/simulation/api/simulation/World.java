package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation;

import java.util.Collection;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation.SimulationWorld;

public sealed interface World permits SimulationWorld {
	
	/**
	 * The width of the world, which is always positive.<br><br>
	 * 
	 * @return the width of the world
	 */
	public int getWidth();
	
	/**
	 * The height of the world, which is always positive.<br><br>
	 * 
	 * @return the height of the world
	 */
	public int getHeight();
	
	/**
	 * Returns a newly allocated list of all entities of this world.<br><br>
	 * 
	 * @return a list of all entities
	 */
	public Collection<Entity> getEntities();
	
	/**
	 * Adds an entity to this world. The entity will be spawned at x=0, y=0 and
	 * will have a width and height of 1.
	 * <b>{@link Entity#spawn(World, int, int, int, int)} should be used
	 * instead.</b><br><br>
	 * 
	 * @param e the entity
	 */
	@Deprecated
	public void add(Entity e);
	
	/**
	 * Adds the entities to this world. The entities are assigned a number i
	 * according to their position in the iteration order of the given
	 * {@link Collection}. The i-th entity will be spawned at x=i%width,
	 * y=i/width, where width is the width of this world. All entities will have
	 * a width and height of 1. If this method fails, it may do so after adding
	 * some, but not all, of the entities.
	 * <b>{@link Entity#spawn(World, int, int, int, int)} should be used
	 * instead.</b><br><br>
	 * 
	 * @param c a collection of entities
	 */
	@Deprecated
	public void addAll(Collection<? extends Entity> c);
	
	/**
	 * Removes an entity from this world. <b>{@link Entity#kill()} should be
	 * used instead.</b><br><br>
	 * 
	 * @param e the entity
	 */
	@Deprecated
	public void remove(Entity e);
	
}
