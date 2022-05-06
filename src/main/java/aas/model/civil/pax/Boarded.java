package aas.model.civil.pax;

import aas.model.AgentFootprint;
import aas.model.communication.Message;
import aas.model.util.Point;

public class Boarded implements State {
	
	private Point position;
	
	public Boarded(final Point position) {
		this.position = new Point();
		this.position = position;
	}
	
	@Override
	public Point calculateNextStep(int time, Message[] requests, AgentFootprint[] neighbours) {
		return this.position;
	}
	
	@Override
	public Message[] getRequests(int time, AgentFootprint me) {
		return new Message[0];
	}
	
	@Override
	public State getNextState() {
		return null;
	}
	
}
