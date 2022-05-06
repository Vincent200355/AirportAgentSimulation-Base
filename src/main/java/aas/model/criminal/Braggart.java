package aas.model.criminal;

import aas.model.Moveable;
import aas.model.Agent;

public interface Braggart extends Agent, Moveable {
	
	String getFlightToBoard();
	
	void setFlightToBoard(String p0);
	
}
