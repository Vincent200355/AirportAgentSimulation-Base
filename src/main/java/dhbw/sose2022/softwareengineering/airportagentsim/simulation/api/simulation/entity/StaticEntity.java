package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.World;

public abstract non-sealed class StaticEntity extends Entity {
	
	public StaticEntity(World world, Point pos, int width, int height) {
		super(world, pos.getX(), pos.getY(), width, height);
	}
	
}
