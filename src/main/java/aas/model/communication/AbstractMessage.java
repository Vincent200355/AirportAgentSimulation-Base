package aas.model.communication;

public abstract class AbstractMessage implements Message {
	
	private long time;
	private Integer sender;
	private Integer receiver;
	
	public AbstractMessage(long time, Integer sender, Integer receiver) {
		this.time = 0L;
		this.time = time;
		this.sender = sender;
		this.setReceiver(receiver);
	}
	
	@Override
	public long getTime() {
		return this.time;
	}
	
	@Override
	public Integer getSender() {
		return this.sender;
	}
	
	@Override
	public Integer getReceiver() {
		return this.receiver;
	}
	
	protected void setReceiver(Integer receiver) {
		this.receiver = receiver;
	}
	
}
