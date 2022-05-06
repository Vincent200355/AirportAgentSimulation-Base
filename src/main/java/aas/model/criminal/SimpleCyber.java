package aas.model.criminal;

import java.util.Arrays;
import java.util.Vector;

import aas.model.Agent;
import aas.model.AgentFootprint;
import aas.model.Moveable;
import aas.model.communication.Message;
import aas.model.communication.network.internet.IPMessage;
import aas.model.util.Point;

public class SimpleCyber implements Agent, Moveable {
	
	Braggart braggert;
	
	public SimpleCyber(int id, String name, Point start) {
		this.braggert = new SimpleBraggart(id, name, start);
	}
	
	@Override
	public double getSpeed() {
		return this.braggert.getSpeed();
	}
	
	@Override
	public double getDirection() {
		return this.braggert.getDirection();
	}
	
	@Override
	public AgentFootprint getFootprint() {
		return this.braggert.getFootprint();
	}
	
	@Override
	public Message[] simulate(int time, Message[] messages, AgentFootprint[] neighbours) {
		Vector<Message> out = new Vector<Message>();
		for(AgentFootprint neighbour : neighbours) {
			if(neighbour.getType().compareTo("checkin") == 0) {
				String flight = this.braggert.getFlightToBoard();
				IPMessage message = new IPMessage(time, this.getFootprint().getId(), neighbour.getId(), "checkin", "inblock");
				message.addData("flight", flight);
				message.addData("gate", new Point(0.0, 0.0).toString());
				message.addData("seats", "1");
				this.braggert.setFlightToBoard(flight);
			}
		}
		out.addAll(Arrays.asList(this.braggert.simulate(time, messages, neighbours)));
		return out.toArray(new Message[out.size()]);
	}
	
	@Override
	public boolean isDone() {
		return this.braggert.isDone();
	}
	
}
