package aas.model;

import aas.model.communication.Message;

public interface Agent {
	
	AgentFootprint getFootprint();
	
	Message[] simulate(final int p0, final Message[] p1, final AgentFootprint[] p2);
	
	boolean isDone();
	
}
