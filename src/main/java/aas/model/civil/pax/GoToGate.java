package aas.model.civil.pax;

import aas.model.communication.voice.BoardingRequest;
import aas.model.communication.Message;
import aas.model.AgentFootprint;
import aas.model.communication.voice.Ticket;
import aas.model.util.Point;

public class GoToGate implements State {
	
	private static double MAX_SPEED = 5.0;
	
	
	private Point position;
	private Ticket ticket;
	private AgentFootprint aircraft;
	
	public GoToGate(Point start, Ticket ticket) {
		this.position = new Point();
		this.ticket = ticket;
		this.position = start;
	}
	
	@Override
	public Point calculateNextStep(int time, Message[] requests, AgentFootprint[] neighbours) {
		this.aircraft = this.lookForAircraft(neighbours);
		return this.position = this.position.moveTo(this.position, this.ticket.getGate(), GoToGate.MAX_SPEED);
	}
	
	private AgentFootprint lookForAircraft(AgentFootprint[] neighbours) {
		for(AgentFootprint neighbour : neighbours)
			if(neighbour.getType().compareTo("aircraft") == 0 && neighbour.getName().compareTo(this.ticket.getFlight()) == 0)
				return neighbour;
		return null;
	}
	
	@Override
	public Message[] getRequests(int time, AgentFootprint me) {
		if(this.aircraft == null)
			return new Message[0];
		BoardingRequest request = new BoardingRequest(time, me.getId(), this.aircraft.getId());
		return new Message[] { request };
	}
	
	@Override
	public State getNextState() {
		if(this.aircraft == null)
			return null;
		return new WaitForBoardingCall(this.position);
	}
	
}
