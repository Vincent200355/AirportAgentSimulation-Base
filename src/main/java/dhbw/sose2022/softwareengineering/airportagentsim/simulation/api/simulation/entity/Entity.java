package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity;

import org.apache.commons.lang3.Validate;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.World;

public abstract sealed class Entity permits MovingEntity, StaticEntity {
	
	private World world;
	private int posX;
	private int posY;
	private int width;
	private int height;
	
	Entity() {}
	
	public final boolean isSpawned() {
		return this.world != null;
	}
	
	public final World getWorld() {
		return this.world;
	}
	
	public final Point getPosition() {
		return new Point(this.posX, this.posY);
	}
	
	public final int getWidth() {
		return this.width;
	}
	
	public final int getHeight() {
		return this.height;
	}
	
	public final void setPosition(Point pos) {
		Validate.notNull(pos, "Position cannot be null");
		Validate.isTrue(pos.getX() + this.width < this.world.getWidth(), "Entity out of bounds");
		Validate.isTrue(pos.getY() + this.height < this.world.getHeight(), "Entity out of bounds");
		this.posX = pos.getX();
		this.posY = pos.getY();
	}
	
	public final void setWidth(int width) {
		Validate.isTrue(this.posX + width < this.world.getWidth(), "Entity out of bounds");
		this.width = width;
	}
	
	public final void setHeight(int height) {
		Validate.isTrue(this.posY + height < this.world.getHeight(), "Entity out of bounds");
		this.height = height;
	}
	
	public final void spawn(World world, int posX, int posY, int width, int height) {
		
		Validate.notNull(world, "World must not be null");
		Validate.isTrue(posX + width < world.getWidth(), "Entity out of bounds");
		Validate.isTrue(posY + height < world.getHeight(), "Entity out of bounds");
		
		if(isSpawned())
			throw new IllegalStateException("Cannot spawn an entity which has already been spawned");
		
		this.world = world;
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		
	}
	
	public final void kill() {
		// TODO implement
	}
	
}
