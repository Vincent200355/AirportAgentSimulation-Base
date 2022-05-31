package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation;

import java.util.Collection;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.DirectedMessage;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.Message;
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
	 * Returns a newly allocated collection of all entities of this world, which
	 * are located within the rectangle at the given position with the given
	 * width. Entities which touch this rectangle, but do not overlap the
	 * rectangle are included in the returned collection.<br><br>
	 * 
	 * @param x the x-position of the search rectangle
	 * @param y the y-position of the search rectangle
	 * @param width the width of the search rectangle
	 * @param height the height of the search rectangle
	 * @return a collection of entities in the given region
	 */
	public Collection<Entity> getEntities(int x, int y, int width, int height);
	
	/**
	 * Returns a newly allocated collection of all entities of this world, which
	 * are located within the rectangle at the given position with the given
	 * width. Entities which touch this rectangle, but do not overlap the
	 * rectangle are included in the returned collection only, if
	 * {@code excludeTouching} is {@code true}. Note that entities of area zero
	 * are considered to intersect the rectangle if they lie within (excluding
	 * the edge) and are considered to touch the rectangle if they lie on the
	 * edge.<br><br>
	 * 
	 * @param x the x-position of the search rectangle
	 * @param y the y-position of the search rectangle
	 * @param width the width of the search rectangle
	 * @param height the height of the search rectangle
	 * @param excludeTouching whether to include entities only touching the
	 * rectangle.
	 * @return a collection of entities in the given region
	 */
	public Collection<Entity> getEntities(int x, int y, int width, int height, boolean excludeTouching);
	
	/**
	 * Returns a newly allocated collection of all entities of this world, which
	 * are located in a circle of radius {@code maxDistance} around the given
	 * center. Entities touching this circle are included.<br><br>
	 * 
	 * @param centerX the x-position of the center of the search circle
	 * @param centerY the y-position of the center of the search circle
	 * @param maxDistance the radius of the center of the search circle
	 * @return a collection of entities in the given region
	 */
	public Collection<Entity> getEntities(int centerX, int centerY, double maxDistance);
	
	/**
	 * Sends the given message to the entities in this world. If the message is
	 * localized, it will only be received by close enough entities. Note that
	 * {@link DirectedMessage} does not restrict the receivers of the message,
	 * i.e. entities which are close enough to receive the message will receive
	 * it no matter if they are the intended recipient. Entities will be
	 * notified through the {@link Entity#receiveMessage(Message)} method.<br>
	 * <br>
	 * 
	 * @param m the message to send
	 */
	public void sendMessage(Message m);
	
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
