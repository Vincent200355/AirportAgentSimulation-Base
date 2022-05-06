package dhbw.sose2022.softwareengineering.api.simulation.entity;

import dhbw.sose2022.softwareengineering.api.geometry.Point;
import dhbw.sose2022.softwareengineering.api.simulation.World;

public abstract class Agent extends MovingEntity {
	
	public Agent(World world, Point pos, int width, int height) {
		super(world, pos, width, height);
	}
	
}
