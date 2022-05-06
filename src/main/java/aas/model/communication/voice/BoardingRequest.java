package aas.model.communication.voice;

public class BoardingRequest extends VoiceMessage {
	
	public BoardingRequest(long time, Integer sender, Integer receiver) {
		super(time, sender, receiver);
	}
	
}
