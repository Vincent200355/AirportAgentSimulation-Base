package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity;

import org.apache.commons.lang3.Validate;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;

public abstract non-sealed class MovingEntity extends Entity {
	
	private double direction;
	private double speed;
	
	private double xFraction = 0.0D;
	private double yFraction = 0.0D;
	
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
		Validate.isTrue(!p.equals(getPosition()), "Cannot turn towards the own position");
		
		double dx = p.getX() - this.posX;
		double dy = p.getY() - this.posY;
		
		this.direction = (dx < 0 && dy >= 0 ? 2.5 : 0.5) * Math.PI - Math.atan2(dy, dx);
		
	}
	
	@Override
	public final void update() {
		
		pluginUpdate();
		
		this.xFraction += Math.sin(this.direction) * this.speed;
		this.yFraction += Math.cos(this.direction) * this.speed;
		
		int dx = (int) Math.round(this.xFraction);
		int dy = (int) Math.round(this.yFraction);
		
		dx = Math.min(Math.max(dx, -this.posX), this.world.getWidth() - this.width - this.posX);
		dy = Math.min(Math.max(dy, -this.posY), this.world.getHeight() - this.height - this.posY);
		
		// TODO check collision with other entities
		
		this.xFraction -= dx;
		this.yFraction -= dy;
		this.posX += dx;
		this.posY += dy;
		
	}
	
}
