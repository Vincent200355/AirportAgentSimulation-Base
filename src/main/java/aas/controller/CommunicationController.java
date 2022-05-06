package aas.controller;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import aas.AirportAgentSimulation;
import aas.model.AgentFootprint;
import aas.model.communication.Broadcast;
import aas.model.communication.Message;

public class CommunicationController {
	
	private static final Logger LOGGER =  Logger.getLogger(AirportAgentSimulation.class.getName());
	
	
	private Vector<Message> incoming;
	private Vector<Message> outgoing;
	private AgentController agentController;
	
	public CommunicationController(AgentController controller) {
		this.incoming = new Vector<Message>();
		this.outgoing = new Vector<Message>();
		this.agentController = controller;
	}
	
	public void addMessages(Message[] messages) {
		for(Message message : messages) {
			if(message != null) {
				if(message instanceof Broadcast)
					addBroadcast((Broadcast) message);
				else
					addSingleMessage(message);
			}
		}
	}
	
	private void addSingleMessage(Message message) {
		if(message == null)
			throw new NullPointerException();
		if(message.getReceiver() == null)
			throw new IllegalArgumentException("Receiver of a single message must not be null: " + message.toString());
		AgentFootprint sender = this.agentController.getAgent(message.getSender());
		AgentFootprint receiver = this.agentController.getAgent(message.getReceiver());
		if(sender == null || receiver == null) {
			CommunicationController.LOGGER.log(Level.WARNING, "unkown sender or receiver");
			return;
		}
		if(sender.getPosition().getDistance(receiver.getPosition()) > message.getMaximumDistance())
			return;
		this.incoming.add(message);
	}
	
	private void addBroadcast(Broadcast broadcast) {
		AgentFootprint sender = this.agentController.getAgent(broadcast.getSender());
		AgentFootprint[] receivers = this.agentController.getFootprints(sender.getPosition(), broadcast.getMaximumDistance());
		Message[] broadcast2;
		for(int length = (broadcast2 = broadcast.broadcast(receivers)).length, i = 0; i < length; ++i) {
			Message message = broadcast2[i];
			if(message != null)
				addSingleMessage(message);
		}
	}
	
	public void transfer(int time) {
		this.outgoing.clear();
		this.outgoing.addAll(this.incoming);
		this.incoming.clear();
	}
	
	public Message[] getOutgoing() {
		return this.outgoing.toArray(new Message[this.outgoing.size()]);
	}
	
	public Message[] getOutgoing(int agentId) {
		Vector<Message> messages = new Vector<Message>();
		for(Message message : this.outgoing)
			if(message.getReceiver() - agentId == 0)
				messages.add(message);
		return messages.toArray(new Message[messages.size()]);
	}
	
}
