package aas.model.communication;

import aas.model.AgentFootprint;

public interface Broadcast extends Message {
	
	Message[] broadcast(AgentFootprint[] p0);
	
}
