package aas.model.civil.pax;

import aas.model.communication.voice.TicketRequest;
import java.util.Random;
import aas.model.communication.Message;
import aas.model.AgentFootprint;
import aas.model.util.Point;

public class SearchCheckIn implements State {
	
	private static double SPEED = 3.0;
	
	
	private Point position;
	private AgentFootprint checkIn;
	private String flight;
	
	public SearchCheckIn(Point position, String flight) {
		this.position = position;
		this.flight = flight;
	}
	
	@Override
	public Point calculateNextStep(int time, Message[] requests, AgentFootprint[] neighbours) {
		this.checkIn = this.lookForCheckin(neighbours);
		if(this.checkIn == null)
			this.position = this.move(this.position);
		return this.position;
	}
	
	private Point move(final Point oldPosition) {
		Random random = new Random();
		double direction = random.nextInt(360);
		double dx = SearchCheckIn.SPEED * Math.cos(direction);
		double dy = SearchCheckIn.SPEED * Math.sin(direction);
		Point newPosition = new Point(oldPosition.getX(), oldPosition.getY());
		newPosition.translate((int) Math.round(dx), (int) Math.round(dy));
		return newPosition;
	}
	
	private AgentFootprint lookForCheckin(AgentFootprint[] neighbours) {
		for(AgentFootprint neighbour : neighbours)
			if(neighbour.getType().compareTo("checkin") == 0)
				return neighbour;
		return null;
	}
	
	@Override
	public Message[] getRequests(int time, AgentFootprint me) {
		if(this.checkIn == null)
			return new Message[0];
		TicketRequest request = new TicketRequest(time, me.getId(), this.checkIn.getId(), this.flight);
		return new Message[] { request };
	}
	
	@Override
	public State getNextState() {
		if(this.checkIn == null)
			return null;
		return new WaitForTicket(this.position);
	}
	
}
