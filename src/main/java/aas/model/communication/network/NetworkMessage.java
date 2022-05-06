package aas.model.communication.network;

import aas.model.communication.AbstractMessage;

public class NetworkMessage extends AbstractMessage {
	
    private static final double MAXIMUM_DISTANCE = Double.MAX_VALUE;
    
    public NetworkMessage(long time, Integer sender, Integer receiver) {
        super(time, sender, receiver);
    }
    
    @Override
    public double getMaximumDistance() {
        return MAXIMUM_DISTANCE;
    }
    
}
