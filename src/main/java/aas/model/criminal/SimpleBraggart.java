package aas.model.criminal;

import java.util.Random;
import aas.model.communication.manual.ArrestCall;
import aas.model.communication.voice.TicketRequest;
import aas.model.communication.Message;
import aas.model.AgentRole;
import aas.model.communication.voice.Ticket;
import aas.model.util.Point;
import aas.model.AgentFootprint;
import aas.model.Moveable;
import aas.model.Agent;

public class SimpleBraggart implements Agent, Moveable, Braggart {
	
	private static final Double SPEED = 4.0;
	
	
	private AgentFootprint footprint;
	private Point currentPosition;
	private Point lastPosition;
	private AgentFootprint checkIn;
	private Ticket ticket;
	private String flight;
	private boolean arrested;
	
	public SimpleBraggart(int id, String name, Point start) {
		this.footprint = new AgentFootprint(id, AgentRole.Criminal, "braggart", name, start);
		this.currentPosition = start;
	}
	
	@Override
	public AgentFootprint getFootprint() {
		this.footprint.setPosition(this.currentPosition);
		return this.footprint;
	}
	
	@Override
	public Message[] simulate(int time, Message[] messages, AgentFootprint[] neighbours) {
		this.lastPosition = this.currentPosition;
		checkMessages(messages);
		checkNeighbours(neighbours);
		if(this.checkIn != null) {
			TicketRequest request = new TicketRequest(time, this.footprint.getId(), this.checkIn.getId(), getFlightToBoard());
			return new Message[] { request };
		}
		walkAround();
		return new Message[0];
	}
	
	private void checkMessages(Message[] messages) {
		for(Message message : messages) {
			if(message instanceof Ticket)
				this.ticket = (Ticket) message;
			if(message instanceof ArrestCall)
				this.arrested = true;
		}
	}
	
	private void walkAround() {
		this.lastPosition = this.currentPosition;
		Random random = new Random();
		double direction = random.nextInt(360);
		double dx = SimpleBraggart.SPEED * Math.cos(direction);
		double dy = SimpleBraggart.SPEED * Math.sin(direction);
		Point newPosition = new Point(this.currentPosition.getX(), this.currentPosition.getY());
		newPosition.translate((int) Math.round(dx), (int) Math.round(dy));
		this.currentPosition = newPosition;
	}
	
	private void checkNeighbours(AgentFootprint[] neighbours) {
		for(final AgentFootprint neighbour : neighbours)
			if(neighbour.getType().compareTo("checkin") == 0)
				this.checkIn = neighbour;
	}
	
	@Override
	public boolean isDone() {
		return this.ticket != null || this.arrested;
	}
	
	@Override
	public double getSpeed() {
		if(this.lastPosition == null)
			return 0.0;
		return this.currentPosition.getDistance(this.lastPosition);
	}
	
	@Override
	public double getDirection() {
		if(this.lastPosition == null)
			return 0.0;
		return this.currentPosition.getDirection(this.lastPosition);
	}
	
	@Override
	public String getFlightToBoard() {
		if(this.flight == null) {
			final Random random = new Random();
			return "DLH" + random.nextInt(999);
		}
		return this.flight;
	}
	
	@Override
	public void setFlightToBoard(String flight) {
		this.flight = flight;
	}
	
}
