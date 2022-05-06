package aas.model.civil.pax;

import aas.model.communication.voice.Ticket;
import aas.model.AgentFootprint;
import aas.model.communication.Message;
import aas.model.util.Point;

public class WaitForTicket implements State {
	
	private Point position;
	private State nextState;
	
	public WaitForTicket(final Point position) {
		this.position = new Point();
		this.nextState = null;
		this.position = position;
	}
	
	@Override
	public Point calculateNextStep(int time, Message[] requests, AgentFootprint[] neighbours) {
		for(final Message request : requests)
			checkMessage(request);
		return this.position;
	}
	
	private void checkMessage(Message request) {
		if(request instanceof Ticket)
			this.nextState = new GoToGate(this.position, (Ticket) request);
	}
	
	@Override
	public Message[] getRequests(int time, AgentFootprint me) {
		return new Message[0];
	}
	
	@Override
	public State getNextState() {
		return this.nextState;
	}
	
}
