package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity;

import org.apache.commons.lang3.Validate;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.plugin.Plugin;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.World;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation.SimulationWorld;

public abstract sealed class Entity permits MovingEntity, StaticEntity {
	
	SimulationWorld world;
	int posX;
	int posY;
	int width;
	int height;
	boolean dead;
	
	Plugin plugin;
	
	Entity() {}
	
	/**
	 * Returns whether this entity has already been spawned into a world.<br>
	 * <br>
	 * 
	 * @return whether this entity has been spawned
	 */
	public final boolean isSpawned() {
		return this.world != null;
	}
	
	/**
	 * Returns whether this entity has been killed, i.e. added to a world and
	 * then removed from it.<br><br>
	 * 
	 * @return whether this entity has been removed
	 */
	public final boolean isDead() {
		return this.dead;
	}
	
	/**
	 * Returns the world this entity belongs to. If the entity has either not
	 * been spawned yet or if the entity has been killed, this is {@code null}.
	 * <br><br>
	 * 
	 * @return
	 */
	public final World getWorld() {
		return this.world;
	}
	
	/**
	 * Returns the position of this entity.<br><br>
	 * 
	 * @return the position of this entity
	 */
	public final Point getPosition() {
		return new Point(this.posX, this.posY);
	}
	
	/**
	 * Returns the width of this entity, which is not negative, but may be 0.
	 * <br><br>
	 * 
	 * @return the width of this entity
	 */
	public final int getWidth() {
		return this.width;
	}
	
	/**
	 * Returns the height of this entity, which is not negative, but may be 0.
	 * <br><br>
	 * 
	 * @return the height of this entity
	 */
	public final int getHeight() {
		return this.height;
	}
	
	/**
	 * Returns the {@link Plugin} which is responsible for managing this entity.
	 * Implementors of {@link Entity} are encouraged to use this method to
	 * obtain a reference to the plugin's main class if needed. The returned
	 * value is never {@code null}.<br><br>
	 * 
	 * @return the plugin responsible for managing this entity
	 */
	public final Plugin getPlugin() {
		return this.plugin;
	}
	
	/**
	 * Updates the position of this entity to the given position.<br><br>
	 * 
	 * @param pos the new position for this entity
	 */
	public final void setPosition(Point pos) {
		
		Validate.notNull(pos, "Position cannot be null");
		Validate.isTrue(pos.getX() + this.width < this.world.getWidth(), "Entity out of bounds");
		Validate.isTrue(pos.getY() + this.height < this.world.getHeight(), "Entity out of bounds");
		if(!isSpawned())
			throw new IllegalStateException("The entity has not been spawned yet");
		
		this.posX = pos.getX();
		this.posY = pos.getY();
		
	}
	
	/**
	 * Updates the width of this entity to the given width.<br><br>
	 * 
	 * @param width the new width of the entity
	 */
	public final void setWidth(int width) {
		
		Validate.isTrue(width >= 0, "Cannot have negative width");
		Validate.isTrue(this.posX + width < this.world.getWidth(), "Entity out of bounds");
		if(!isSpawned())
			throw new IllegalStateException("The entity has not been spawned yet");
		
		this.width = width;
		
	}
	
	/**
	 * Updates the height of this entity to the given height.<br><br>
	 * @param height the new height of the entity
	 */
	public final void setHeight(int height) {
		
		Validate.isTrue(height >= 0, "Cannot have negative height");
		Validate.isTrue(this.posY + height < this.world.getHeight(), "Entity out of bounds");
		if(!isSpawned())
			throw new IllegalStateException("The entity has not been spawned yet");
		
		this.height = height;
		
	}
	
	/**
	 * Spawns the entity in the given world at the given position.<br><br>
	 * 
	 * @param world the world
	 * @param posX the x-position
	 * @param posY the y-position
	 * @param width the width of the entity
	 * @param height the height of the entity
	 */
	public final void spawn(World world, int posX, int posY, int width, int height) {
		
		Validate.notNull(world, "World must not be null");
		Validate.isTrue(world instanceof SimulationWorld, "Invalid world");
		Validate.isTrue(width >= 0, "Cannot have negative width");
		Validate.isTrue(height >= 0, "Cannot have negative height");
		Validate.isTrue(posX >= 0, "Entity out of bounds");
		Validate.isTrue(posY >= 0, "Entity out of bounds");
		Validate.isTrue(posX + width < world.getWidth(), "Entity out of bounds");
		Validate.isTrue(posY + height < world.getHeight(), "Entity out of bounds");
		
		if(isSpawned())
			throw new IllegalStateException("Cannot spawn an entity which has already been spawned");
		
		this.world = (SimulationWorld) world;
		this.world.addEntity(this);
		
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		
		onBirth();
		
	}
	
	/**
	 * Kills the entity and removes it from the world.<br><br>
	 */
	public final void kill() {
		
		if(isDead())
			throw new IllegalStateException("Cannot kill an entity which is already dead");
		
		if(!isSpawned())
			throw new IllegalStateException("Cannot kill an entity before it has been spawned");
		
		onDeath();
		
		this.world = null;
		this.world.removeEntity(this);
		
		this.posX = 0;
		this.posY = 0;
		this.width = 0;
		this.height = 0;
		this.dead = true;
		
	}
	
	/**
	 * Triggers an update cycle on this entity.<br><br>
	 * 
	 * This method is regularly invoked by the simulation to allow this entity
	 * to update its state.<br><br>
	 * 
	 * Plugins should not invoke this method as this would distort the
	 * perception of time for the entity.<br><br>
	 */
	public abstract void update();
	
	
	/**
	 * Allows the implementing plugin to update the internal state of this
	 * entity.<br><br>
	 * 
	 * There is no required result of invoking this method. Therefore, no-op
	 * would be a valid implementation.<br><br>
	 * 
	 * This method is regularly invoked by the simulation to allow this entity
	 * to update its state.<br><br>
	 * 
	 * Plugins should not invoke this method as this would distort the
	 * perception of time for the entity.<br><br>
	 */
	public abstract void pluginUpdate();
	
	/**
	 * Allows the implementing plugin to update the internal state of this
	 * entity.<br><br>
	 * 
	 * This method is invoked before the first update on this entity, but after
	 * the entity has been added to a world.<br><br>
	 * 
	 * Plugins should not invoke this method.<br><br>
	 */
	public void onBirth() {}
	
	/**
	 * Allows the implementing plugin to update the internal state of this
	 * entity.<br><br>
	 * 
	 * This method is invoked after the last update on this entity, but before
	 * the entity is removed from the world.<br><br>
	 * 
	 * Plugins should not invoke this method.<br><br>
	 */
	public void onDeath() {}
	
}
