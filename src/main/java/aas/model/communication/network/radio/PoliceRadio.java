package aas.model.communication.network.radio;

import aas.model.AgentRole;
import java.util.Vector;
import aas.model.communication.Message;
import aas.model.AgentFootprint;
import aas.model.communication.Broadcast;
import aas.model.communication.network.NetworkMessage;

public class PoliceRadio extends NetworkMessage implements Broadcast {
	
	private static final int DEFAULT_RECEIVER = -1;
	
	
	private PoliceCode code;
	private AgentFootprint criminal;
	
	public PoliceRadio(long time, int sender, PoliceCode code) {
		super(time, sender, DEFAULT_RECEIVER);
		this.code = code;
	}
	
	public PoliceRadio(long time, int sender, PoliceCode code, AgentFootprint criminal) {
		this(time, sender, code);
		this.criminal = criminal;
	}
	
	private PoliceRadio(long time, int sender, int receiver, PoliceCode code, AgentFootprint criminal) {
		super(time, sender, receiver);
		this.code = code;
		this.criminal = criminal;
	}
	
	public AgentFootprint getCriminal() {
		return this.criminal;
	}
	
	public PoliceCode getCode() {
		return this.code;
	}
	
	@Override
	public Message[] broadcast(AgentFootprint[] receivers) {
		Vector<Message> messages = new Vector<Message>();
		for(AgentFootprint receiver : receivers)
			if(receiver.getRole() == AgentRole.Security)
				messages.add(new PoliceRadio(getTime(), getSender(), receiver.getId(), this.code, this.criminal));
		return messages.toArray(new Message[messages.size()]);
	}
	
}
