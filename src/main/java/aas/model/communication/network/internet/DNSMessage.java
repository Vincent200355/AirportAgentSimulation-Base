package aas.model.communication.network.internet;

import aas.model.communication.network.NetworkMessage;

public class DNSMessage extends NetworkMessage {
	
    private String[] hostNames;
    
    public DNSMessage(long time, Integer sender, int receiver, String[] hostNames) {
        super(time, sender, receiver);
        this.hostNames = hostNames;
    }
    
    public String[] getHostNames() {
        return this.hostNames;
    }
    
}
