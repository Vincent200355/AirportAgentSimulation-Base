package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;

public sealed interface Message permits LocalMessage, GlobalMessage {
	
	public Entity getOrigin();
	
	public Point getOriginPosition();
	
}
