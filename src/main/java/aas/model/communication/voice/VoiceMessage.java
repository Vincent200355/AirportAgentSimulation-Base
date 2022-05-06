package aas.model.communication.voice;

import aas.model.communication.AbstractMessage;

public class VoiceMessage extends AbstractMessage {
	
	private static final double MAXIMUM_DISTANCE = 13.0;
	
	public VoiceMessage(long time, Integer sender, Integer receiver) {
		super(time, sender, receiver);
	}
	
	@Override
	public double getMaximumDistance() {
		return MAXIMUM_DISTANCE;
	}
	
}
