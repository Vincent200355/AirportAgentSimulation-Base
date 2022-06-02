package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;

public sealed interface Message permits LocalMessage, GlobalMessage {
	
	public Entity getOrigin();
	
	public Point getOriginPosition();
	
	/**
	 * Returns the content of this message as a String for exporting.
	 * 
	 * @return the content of this message as String
	 */
	public String toString();
	
	/**
	 * Builds a message as from a String that was imported.
	 * 
	 * @param s
	 */
	public void fromString(String s);
	
}
