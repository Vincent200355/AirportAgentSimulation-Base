package aas.model.communication.voice;

import aas.model.util.Point;

public class Ticket extends VoiceMessage {
	
	private String flight;
	private Point gate;
	
	public Ticket(long time, Integer sender, Integer receiver, String flight, Point gate) {
		super(time, sender, receiver);
		this.flight = flight;
		this.gate = gate;
	}
	
	public String getFlight() {
		return this.flight;
	}
	
	public Point getGate() {
		return this.gate;
	}
	
}
