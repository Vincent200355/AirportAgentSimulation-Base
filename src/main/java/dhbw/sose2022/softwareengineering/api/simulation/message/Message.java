package dhbw.sose2022.softwareengineering.api.simulation.message;

import dhbw.sose2022.softwareengineering.api.geometry.Point;
import dhbw.sose2022.softwareengineering.api.simulation.entity.Entity;

public sealed interface Message permits LocalMessage, GlobalMessage {
	
	public Entity getOrigin();
	
	public Point getOriginPosition();
	
}
