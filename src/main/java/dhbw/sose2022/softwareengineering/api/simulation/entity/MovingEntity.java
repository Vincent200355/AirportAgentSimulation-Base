package dhbw.sose2022.softwareengineering.api.simulation.entity;

import org.apache.commons.lang3.Validate;

import dhbw.sose2022.softwareengineering.api.geometry.Point;
import dhbw.sose2022.softwareengineering.api.simulation.World;

public abstract non-sealed class MovingEntity extends Entity {

	private double direction;
	private double speed;
	
	public MovingEntity(World world, Point pos, int width, int height) {
		super(world, pos.getX(), pos.getY(), width, height);
	}
	
	public double getDirection() {
		return this.direction;
	}
	
	public double getSpeed() {
		return this.speed;
	}
	
	public void setSpeed(double speed) {
		Validate.isTrue(speed >= 0.0D, "Speed cannot be negative");
		this.speed = speed;
	}
	
	public void turn(Point p) {
		Validate.notNull(p);
		// TODO implement
	}
	
}
