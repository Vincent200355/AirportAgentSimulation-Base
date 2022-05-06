package aas.model.civil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import aas.controller.logger.LoggerType;
import aas.model.Agent;
import aas.model.AgentFootprint;
import aas.model.AgentRole;
import aas.model.StaticObject;
import aas.model.communication.Message;
import aas.model.communication.network.internet.DNSMessage;
import aas.model.communication.network.internet.IPMessage;
import aas.model.communication.network.internet.InternetGateway;
import aas.model.util.Point;

public class DomainNameServer implements InternetGateway, Agent, StaticObject {
	
	private static final Logger EVENT_LOGGER = Logger.getLogger(LoggerType.EVENT + ".dns");
	
	
	private AgentFootprint footprint;
	private HashMap<String, Vector<Integer>> resourceRecord;
	
	public DomainNameServer(int id, String name, Point position) {
		this.resourceRecord = new HashMap<String, Vector<Integer>>();
		this.footprint = new AgentFootprint(id, AgentRole.Civil, "dns", name, position);
	}
	
	@Override
	public AgentFootprint getFootprint() {
		return this.footprint;
	}
	
	@Override
	public Message[] simulate(int time, Message[] messages, AgentFootprint[] neighbours) {
		Vector<Message> outgoingMessages = new Vector<Message>();
		for(Message message : messages) {
			if(message instanceof DNSMessage) {
				DNSMessage registerMessage = (DNSMessage) message;
				registerHost(message.getSender(), registerMessage.getHostNames());
				DomainNameServer.EVENT_LOGGER.info(String.valueOf(time) + " - " + this.getFootprint().toString() + " : Agent " + registerMessage.getSender() + " registered at DNS.");
			}
			if(message instanceof IPMessage) {
				outgoingMessages.addAll(Arrays.asList(forwardIPMessage((IPMessage) message)));
				DomainNameServer.EVENT_LOGGER.info(String.valueOf(time) + " - " + this.getFootprint().toString() + " : Message of agent " + message.getSender() + " forwarded.");
			}
		}
		return outgoingMessages.toArray(new Message[outgoingMessages.size()]);
	}
	
	private void registerHost(int id, String[] names) {
		for(String name : names) {
			if(!this.resourceRecord.containsKey(name))
				this.resourceRecord.put(name, new Vector<Integer>());
			this.resourceRecord.get(name).add(id);
		}
	}
	
	private Message[] forwardIPMessage(IPMessage message) {
		String hostName = message.getReceiverHostName();
		if(!this.resourceRecord.containsKey(hostName))
			return new Message[0];
		Vector<IPMessage> messages = new Vector<IPMessage>();
		for(Integer address : this.resourceRecord.get(hostName))
			messages.add(message.copy(this.getFootprint().getId(), address));
		return messages.toArray(new Message[messages.size()]);
	}
	
	@Override
	public boolean isDone() {
		return false;
	}
	
	@Override
	public double getWidth() {
		return 1.5;
	}
	
	@Override
	public double getLength() {
		return 1.5;
	}
	
	@Override
	public String getName() {
		return this.footprint.getName();
	}
	
	@Override
	public int getId() {
		return this.footprint.getId();
	}
	
}
