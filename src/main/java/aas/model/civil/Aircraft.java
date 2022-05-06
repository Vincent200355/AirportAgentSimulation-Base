package aas.model.civil;

import aas.model.communication.voice.BoardingCall;
import aas.model.communication.network.internet.IPMessage;
import aas.model.communication.Message;
import aas.model.AgentRole;
import aas.model.util.Point;
import aas.controller.logger.LoggerType;
import aas.model.communication.voice.BoardingRequest;
import java.util.Vector;
import aas.model.AgentFootprint;
import java.util.logging.Logger;
import aas.model.communication.network.internet.InternetPort;
import aas.model.Agent;

public class Aircraft implements Agent, InternetPort {
	
	private static final Logger LOGGER_DEBUG = Logger.getLogger(LoggerType.DEBUG + ".aircraft");
	private static final Logger LOGGER_EVENT = Logger.getLogger(LoggerType.EVENT + ".aircraft");
	
	
	private AgentFootprint footprint;
	private Integer gateway;
	private Vector<BoardingRequest> boardingQueue;
	private Integer seats;
	private int paxCounter;
	private boolean inBlock;
	
	public Aircraft(int id, Point gate, String flightName, int seats) {
		this.boardingQueue = new Vector<BoardingRequest>();
		this.paxCounter = 0;
		this.inBlock = false;
		this.footprint = new AgentFootprint(id, AgentRole.Civil, "aircraft", flightName, gate);
		this.seats = seats;
	}
	
	@Override
	public AgentFootprint getFootprint() {
		return this.footprint;
	}
	
	@Override
	public Message[] simulate(int time, Message[] messages, AgentFootprint[] neighbours) {
		for(final Message message : messages)
			if(message instanceof BoardingRequest)
				this.boardingQueue.add((BoardingRequest) message);
		if(!this.inBlock) {
			if(this.gateway != null) {
				IPMessage message2 = new IPMessage(time, this.getFootprint().getId(), this.gateway, "airport", "InBlock");
				message2.addData("flight", this.getFootprint().getName());
				message2.addData("gate", this.footprint.getPosition().toString());
				message2.addData("seats", this.seats.toString());
				this.inBlock = true;
				return new Message[] { message2 };
			}
			Aircraft.LOGGER_DEBUG.warning("No gateway defined for aircraft " + getFootprint().getName() + ". Unable to send inblock message to airport.");
		}
		if(this.boardingQueue.size() > 0) {
			Aircraft.LOGGER_EVENT.info(String.valueOf(time) + " - " + this.footprint + ": " + "Boarding call for agent " + this.boardingQueue.get(0).getSender() + " with seat " + this.paxCounter);
			final BoardingCall call = new BoardingCall(time, this.footprint.getId(), this.boardingQueue.get(0).getSender(), this.paxCounter);
			this.boardingQueue.remove(0);
			this.paxCounter++;
			return new Message[] { call };
		}
		return new Message[0];
	}
	
	@Override
	public boolean isDone() {
		return this.seats <= this.paxCounter;
	}
	
	@Override
	public void setGateway(int dnsId) {
		this.gateway = dnsId;
	}
	
}
