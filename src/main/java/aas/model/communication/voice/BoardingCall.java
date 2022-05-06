package aas.model.communication.voice;

public class BoardingCall extends VoiceMessage {
	
	private Integer seat;
	
	public BoardingCall(long time, Integer sender, Integer receiver, int seatNumber) {
		super(time, sender, receiver);
		this.setSeat(seatNumber);
	}
	
	public Integer getSeat() {
		return this.seat;
	}
	
	private void setSeat(int seatNumber) {
		this.seat = seatNumber;
	}
	
}
