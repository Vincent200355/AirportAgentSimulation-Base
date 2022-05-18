package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;

public interface DirectedMessage {
	
	public Entity getTarget();
	
}
