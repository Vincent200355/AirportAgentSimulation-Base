package aas.model.security;

import aas.model.communication.manual.ArrestCall;
import aas.model.communication.network.radio.PoliceRadio;
import aas.model.communication.network.radio.PoliceCode;
import java.util.Vector;
import aas.model.communication.Message;
import aas.model.AgentRole;
import aas.controller.logger.LoggerType;
import aas.model.util.Point;
import aas.model.AgentFootprint;
import java.util.logging.Logger;
import aas.model.Moveable;
import aas.model.Agent;

public class SimpleOfficer implements Agent, Moveable {
	
	private static final Logger EVENT_LOGGER = Logger.getLogger(LoggerType.EVENT + ".officer");
	private static final Double MAX_CHASE_SPEED = 10.0;
	
	
	private AgentFootprint footprint;
	private Point currentPosition;
	private Point lastPosition;
	private Point target;
	
	public SimpleOfficer(int id, String name, Point start) {
		this.footprint = new AgentFootprint(id, AgentRole.Security, "simpleOfficer", name, start);
		this.currentPosition = start;
	}
	
	@Override
	public double getSpeed() {
		if(this.lastPosition == null)
			return 0.0;
		return this.lastPosition.getDistance(this.currentPosition);
	}
	
	@Override
	public double getDirection() {
		if(this.lastPosition == null)
			return 0.0;
		return this.lastPosition.getDirection(this.currentPosition);
	}
	
	@Override
	public AgentFootprint getFootprint() {
		this.footprint.setPosition(this.currentPosition);
		return this.footprint;
	}
	
	@Override
	public Message[] simulate(int time, Message[] messages, AgentFootprint[] neighbours) {
		this.lastPosition = this.currentPosition;
		Vector<Message> output = new Vector<Message>();
		checkMessages(time, messages);
		AgentFootprint criminal = this.checkNeighbours(neighbours);
		if(this.target == null && criminal != null) {
			this.target = criminal.getPosition();
			PoliceRadio radioCall = new PoliceRadio(time, this.getFootprint().getId(), PoliceCode.CriminalDetected, criminal);
			output.add(radioCall);
			SimpleOfficer.EVENT_LOGGER.info(String.valueOf(time) + " - " + this.getFootprint().toString() + ": Detected criminal with id " + criminal.getId());
		}
		if(this.target != null) {
			if(criminal != null)
				this.target = criminal.getPosition();
			this.currentPosition = chaseCriminal();
			Message arrest = arrestCriminal(time, criminal);
			if(arrest != null) {
				output.add(arrest);
				output.add(new PoliceRadio(time, this.getFootprint().getId(), PoliceCode.CriminalArrested, criminal));
				SimpleOfficer.EVENT_LOGGER.info(String.valueOf(time) + " - " + this.getFootprint().toString() + ": Arresting criminal with id " + criminal.getId());
			}
		}
		return output.toArray(new Message[output.size()]);
	}
	
	private void checkMessages(long time, Message[] messages) {
		for(Message message : messages) {
			if(message instanceof PoliceRadio) {
				PoliceRadio radio = (PoliceRadio) message;
				switch(radio.getCode()) {
					case ChaseCriminal:
						this.target = radio.getCriminal().getPosition();
						SimpleOfficer.EVENT_LOGGER.info(String.valueOf(time) + " - " + this.getFootprint().toString() + ": Chasing criminal with id " + radio.getCriminal().getId());
						break;
					case StopChase:
						this.target = null;
						SimpleOfficer.EVENT_LOGGER.info(String.valueOf(time) + " - " + this.getFootprint().toString() + ": Stop chasing. Holding position.");
						break;
				}
			}
		}
	}
	
	private Point chaseCriminal() {
		return this.currentPosition.moveTo(this.currentPosition, this.target, SimpleOfficer.MAX_CHASE_SPEED);
	}
	
	private AgentFootprint checkNeighbours(AgentFootprint[] neighbours) {
		for(final AgentFootprint neighbour : neighbours)
			if(neighbour.getRole() == AgentRole.Criminal)
				return neighbour;
		return null;
	}
	
	private Message arrestCriminal(final int time, final AgentFootprint criminal) {
		if(criminal == null)
			return null;
		ArrestCall call = new ArrestCall(time, this.getFootprint().getId(), criminal.getId());
		if(Math.abs(getFootprint().getPosition().getDistance(criminal.getPosition()) - call.getMaximumDistance()) > 1.0)
			return null;
		return call;
	}
	
	@Override
	public boolean isDone() {
		return false;
	}
	
}
