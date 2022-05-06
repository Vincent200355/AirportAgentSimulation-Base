package aas.model.civil;

import aas.model.communication.voice.Ticket;
import aas.model.communication.network.internet.DNSMessage;
import aas.model.communication.network.internet.IPMessage;
import aas.model.communication.Message;
import aas.model.AgentRole;
import aas.controller.AgentController;
import aas.controller.logger.LoggerType;
import aas.model.AgentFootprint;
import aas.model.communication.voice.TicketRequest;
import java.util.Vector;
import aas.model.util.Point;
import java.util.HashMap;
import java.util.logging.Logger;
import aas.model.communication.network.internet.InternetPort;
import aas.model.StaticObject;
import aas.model.Agent;

public class CheckInCounter implements Agent, StaticObject, InternetPort {
	
	private static final Logger EVENT_LOGGER = Logger.getLogger(LoggerType.EVENT + ".checkin");
	private static final Logger LOGGER = Logger.getLogger(AgentController.class.getName());
	
	
	private Integer gateway;
	private boolean dnsRegistered;
	private HashMap<String, Integer> ticketList;
	private HashMap<String, Point> gateList;
	private Vector<TicketRequest> queue;
	private AgentFootprint footprint;
	
	public CheckInCounter(int id, String name, Point position) {
		this.dnsRegistered = false;
		this.ticketList = new HashMap<String, Integer>();
		this.gateList = new HashMap<String, Point>();
		this.queue = new Vector<TicketRequest>();
		this.footprint = new AgentFootprint(id, AgentRole.Civil, "checkin", name, position);
	}
	
	@Override
	public AgentFootprint getFootprint() {
		return this.footprint;
	}
	
	@Override
	public Message[] simulate(int time, Message[] messages, AgentFootprint[] neighbours) {
		for(Message message : messages) {
			if(message instanceof IPMessage)
				checkInternet(time, (IPMessage) message);
			if(message instanceof TicketRequest) {
				TicketRequest request = (TicketRequest) message;
				if(isTicketRequestValid(request))
					this.queue.add(request);
			}
		}
		if(!this.dnsRegistered && this.gateway != null) {
			this.dnsRegistered = true;
			return new Message[] { new DNSMessage(time, this.getFootprint().getId(), this.gateway, new String[] { "airport" }) };
		}
		if(this.queue.size() > 0) {
			Ticket ticket = this.generateTicket(time);
			if(ticket != null)
				return new Message[] { ticket };
		}
		return new Message[0];
	}
	
	private void registerAtDns() {}
	
	private void checkInternet(final long time, final IPMessage message) {
		if(!message.isMessage("inblock", new String[] { "flight", "seats", "gate" }))
			CheckInCounter.LOGGER.warning("In block message from " + message.getSender() + " can not be processed due to missing data.");
		this.ticketList.put(message.getData("flight"), Integer.valueOf(message.getData("seats")));
		this.gateList.put(message.getData("flight"), Point.valueOf(message.getData("gate")));
		CheckInCounter.EVENT_LOGGER.info(String.valueOf(time) + " - flight " + message.getData("flight") + " registered at checkin " + this.getFootprint().getName());
	}
	
	private boolean isTicketRequestValid(final TicketRequest request) {
		if(!this.gateList.containsKey(request.getFlight()) || !this.ticketList.containsKey(request.getFlight())) {
			CheckInCounter.LOGGER.warning("flight " + request.getFlight() + " not listed in checkin");
			return false;
		}
		return true;
	}
	
	private Ticket generateTicket(int time) {
		if(this.queue.size() < 1)
			return null;
		TicketRequest request = this.queue.get(0);
		if(!this.isTicketRequestValid(request)) {
			CheckInCounter.LOGGER.warning("No ticket left for passenger " + request.getSender() + " waiting for flight " + request.getFlight());
			return null;
		}
		Point gate = this.gateList.get(request.getFlight());
		Integer ticketNumber = this.ticketList.get(request.getFlight()) - 1;
		if(ticketNumber < 1) {
			this.ticketList.remove(request.getFlight());
			this.gateList.remove(request.getFlight());
		} else {
			this.ticketList.put(request.getFlight(), ticketNumber);
		}
		Ticket ticket = new Ticket(time, this.footprint.getId(), request.getSender(), request.getFlight(), gate);
		CheckInCounter.EVENT_LOGGER.info(String.valueOf(time) + " - " + this.footprint.toString() + ": Ticket for passenger " + ticket.getReceiver() + " generated.");
		this.queue.remove(0);
		return ticket;
	}
	
	@Override
	public boolean isDone() {
		return false;
	}
	
	@Override
	public double getWidth() {
		return 5.0;
	}
	
	@Override
	public double getLength() {
		return 10.0;
	}
	
	public int getRegisteredFlights() {
		return this.gateList.keySet().size();
	}
	
	@Override
	public void setGateway(int dnsId) {
		this.gateway = dnsId;
	}
	
}
