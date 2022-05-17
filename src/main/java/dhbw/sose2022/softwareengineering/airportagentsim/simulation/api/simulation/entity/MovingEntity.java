package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity;

import org.apache.commons.lang3.Validate;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;

public abstract non-sealed class MovingEntity extends Entity {
	
	private double direction;
	private double speed;
	
	public MovingEntity() {}
	
	public final double getDirection() {
		return this.direction;
	}
	
	public final double getSpeed() {
		return this.speed;
	}
	
	public final void setSpeed(double speed) {
		Validate.isTrue(speed >= 0.0D, "Speed cannot be negative");
		this.speed = speed;
	}
	
	public final void turn(Point p) {
		Validate.notNull(p);
		// TODO implement
	}
	
}
