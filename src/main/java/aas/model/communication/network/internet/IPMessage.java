package aas.model.communication.network.internet;

import java.util.HashMap;

import aas.model.communication.network.NetworkMessage;

public class IPMessage extends NetworkMessage {
	
	String receiverHostName;
	String caption;
	HashMap<String, String> data;
	
	public IPMessage(long time, Integer sender, Integer receiver, String host, String caption) {
		super(time, sender, receiver);
		this.data = new HashMap<String, String>();
		this.receiverHostName = host;
		this.caption = caption;
	}
	
	public String getCaption() {
		return this.caption;
	}
	
	public void addData(String key, String value) {
		this.data.put(key, value);
	}
	
	public String getData(String key) {
		return this.data.get(key);
	}
	
	public String getReceiverHostName() {
		return this.receiverHostName;
	}
	
	public IPMessage copy(int copier, int receiver) {
		IPMessage copy = new IPMessage(this.getTime(), this.getSender(), receiver, this.receiverHostName, this.caption);
		for(String key : this.data.keySet())
			copy.addData(key, this.data.get(key));
		copy.addData("copier", Integer.toString(copier));
		return copy;
	}
	
	public boolean isMessage(String expectedCaption, String[] parameters) {
		if(this.caption != null && this.caption.toLowerCase().compareTo(expectedCaption.toLowerCase()) != 0)
			return false;
		for(String parameter : parameters)
			if(!this.data.containsKey(parameter))
				return false;
		return true;
	}
	
}
