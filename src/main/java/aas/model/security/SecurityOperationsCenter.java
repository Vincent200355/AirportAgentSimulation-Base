package aas.model.security;

import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Logger;

import aas.controller.logger.LoggerType;
import aas.model.Agent;
import aas.model.AgentFootprint;
import aas.model.AgentRole;
import aas.model.communication.Message;
import aas.model.communication.network.radio.PoliceCode;
import aas.model.communication.network.radio.PoliceRadio;
import aas.model.util.Point;

public class SecurityOperationsCenter implements Agent {
	
	private static final Logger EVENT_LOGGER = Logger.getLogger(LoggerType.EVENT + ".soc");
	
	
	private AgentFootprint footprint;
	private Vector<Integer> officers;
	
	public SecurityOperationsCenter(int id, String name, Point position) {
		this.officers = new Vector<Integer>();
		this.footprint = new AgentFootprint(id, AgentRole.Security, "SecurityOperationsCenter", name, position);
	}
	
	@Override
	public AgentFootprint getFootprint() {
		return this.footprint;
	}
	
	@Override
	public Message[] simulate(int time, Message[] messages, AgentFootprint[] neighbours) {
		Vector<Message> outMessages = new Vector<Message>();
		for(Message message : messages)
			if (message instanceof PoliceRadio)
				outMessages.addAll(Arrays.asList(listenToRadio(time, (PoliceRadio) message)));
		return outMessages.toArray(new Message[outMessages.size()]);
	}
	
	private Message[] listenToRadio(long time, PoliceRadio radio) {
		switch(radio.getCode()) {
			case OfficerOnDuty:
				this.officers.add(radio.getSender());
				break;
			case OfficerOffDuty:
				this.officers.remove(radio.getSender());
				break;
			case CriminalDetected:
				SecurityOperationsCenter.EVENT_LOGGER.info(String.valueOf(time) + " - " + this.getFootprint().toString() + ": All officers chase criminal " + radio.getCriminal().getId());
				return sendStartChase(time, radio.getCriminal());
			case CriminalArrested:
				SecurityOperationsCenter.EVENT_LOGGER.info(String.valueOf(time) + " - " + this.getFootprint().toString() + ": All officers stop chasing.");
				return sendStopChase(time, radio.getCriminal());
			default:
				return new Message[0];
		}
		return new Message[0];
	}
	
	private Message[] sendStartChase(long time, AgentFootprint criminal) {
		return new Message[] { new PoliceRadio(time, this.footprint.getId(), PoliceCode.ChaseCriminal, criminal) };
	}
	
	private Message[] sendStopChase(long time, AgentFootprint criminal) {
		return new Message[] { new PoliceRadio(time, this.footprint.getId(), PoliceCode.StopChase, criminal) };
	}
	
	public int getShiftSize() {
		return this.officers.size();
	}
	
	@Override
	public boolean isDone() {
		return false;
	}
	
}
