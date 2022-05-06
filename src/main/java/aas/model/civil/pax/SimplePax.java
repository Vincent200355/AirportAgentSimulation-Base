package aas.model.civil.pax;

import aas.model.communication.Message;
import aas.model.AgentRole;
import aas.controller.logger.LoggerType;
import aas.model.util.Point;
import aas.model.AgentFootprint;
import java.util.logging.Logger;
import aas.model.Moveable;
import aas.model.Agent;

public class SimplePax implements Agent, Moveable {
	
	private static final Logger LOGGER = Logger.getLogger(LoggerType.EVENT + ".simplePax");
	private static final double MAX_TRAVEL_DISTANCE = 5000.0;
	
	
	private AgentFootprint footprint;
	private Point startPosition;
	private Point lastPosition;
	private Point currentPosition;
	private State state;
	
	public SimplePax(int id, String name, Point position, String flight) {
		this.startPosition = new Point();
		this.lastPosition = new Point();
		this.currentPosition = new Point();
		this.startPosition = position;
		this.currentPosition = position;
		this.footprint = new AgentFootprint(id, AgentRole.Civil, "pax", name, this.currentPosition);
		setState(0L, new SearchCheckIn(this.currentPosition, flight));
	}
	
	private void setPosition(Point newPosition) {
		this.lastPosition = this.currentPosition;
		this.currentPosition = newPosition;
		this.footprint.setPosition(this.currentPosition);
	}
	
	@Override
	public Message[] simulate(int time, Message[] messages, AgentFootprint[] neighbours) {
		Point newPosition = this.state.calculateNextStep(time, messages, neighbours);
		Message[] newRequests = this.state.getRequests(time, this.getFootprint());
		setPosition(newPosition);
		if(this.state.getNextState() != null)
			setState(time, this.state.getNextState());
		return newRequests;
	}
	
	private void setState(long time, State newState) {
		if(newState == null)
			throw new IllegalArgumentException("state for agent must not be null");
		if(this.state == null)
			SimplePax.LOGGER.info(String.valueOf(Long.toString(time)) + " - " + this.footprint.toString() + ": " + " is initially in state " + newState.getClass().getSimpleName());
		else
			SimplePax.LOGGER.info(String.valueOf(Long.toString(time)) + " - " + this.footprint.toString() + ": " + this.state.getClass().getSimpleName() + "->" + newState.getClass().getSimpleName());
		this.state = newState;
	}
	
	@Override
	public double getSpeed() {
		return this.lastPosition.getDistance(this.currentPosition);
	}
	
	@Override
	public double getDirection() {
		return this.lastPosition.getDirection(this.currentPosition);
	}
	
	@Override
	public AgentFootprint getFootprint() {
		this.footprint.setPosition(this.currentPosition);
		return this.footprint;
	}
	
	@Override
	public boolean isDone() {
		return this.state instanceof Boarded || this.currentPosition.getDistance(this.startPosition) > MAX_TRAVEL_DISTANCE;
	}
	
}
