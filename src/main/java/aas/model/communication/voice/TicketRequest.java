package aas.model.communication.voice;

public class TicketRequest extends VoiceMessage {
	
	private String flight;
	
	public TicketRequest(long time, Integer sender, Integer receiver, String flight) {
		super(time, sender, receiver);
		this.flight = flight;
	}
	
	public String getFlight() {
		return this.flight;
	}
	
}
