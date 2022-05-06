package aas.controller.export;

import aas.model.communication.Message;
import aas.model.Agent;

public interface Export {
	
	void notify(int p0, Agent p1, Message[] p2);
	
	void finish();
	
}
