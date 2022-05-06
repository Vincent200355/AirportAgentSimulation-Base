package aas.model.civil.pax;

import aas.model.util.Point;
import aas.model.AgentFootprint;
import aas.model.communication.Message;

public interface State {
	
	Point calculateNextStep(int p0, Message[] p1, AgentFootprint[] p2);
	
	Message[] getRequests(int p0, AgentFootprint p1);
	
	State getNextState();
	
}
