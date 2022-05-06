package aas.model.communication.manual;

import aas.model.communication.AbstractMessage;

public class ManualInteraction extends AbstractMessage {
	
	private static final double MAXIMUM_DISTANCE = 1.0;
	
	public ManualInteraction(long time, Integer sender, Integer receiver) {
		super(time, sender, receiver);
	}
	
	@Override
	public double getMaximumDistance() {
		return MAXIMUM_DISTANCE;
	}
	
}
