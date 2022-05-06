package aas.model.communication;

public interface Message {
	
	long getTime();
	
	Integer getSender();
	
	Integer getReceiver();
	
	double getMaximumDistance();
	
}
