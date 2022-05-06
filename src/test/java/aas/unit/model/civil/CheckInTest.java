package aas.unit.model.civil;

import aas.model.communication.network.internet.DNSMessage;
import aas.model.communication.voice.Ticket;
import aas.model.communication.voice.TicketRequest;
import aas.model.communication.network.internet.IPMessage;
import aas.model.AgentFootprint;
import aas.model.communication.Message;
import aas.model.AgentRole;
import org.junit.Test;
import aas.model.civil.CheckInCounter;
import aas.model.util.Point;

public class CheckInTest {
	
	@Test
	public void testCheckInCounter() {
		CheckInCounter checkin = new CheckInCounter(0, "checkin1", new Point(0.0, 0.0));
		assert checkin != null;
	}
	
	@Test
	public void testGetFootprint() {
		CheckInCounter checkin = new CheckInCounter(0, "checkin1", new Point(0.0, 0.0));
		assert checkin.getFootprint().getId() == 0;
		assert checkin.getFootprint().getRole() == AgentRole.Civil;
		assert checkin.getFootprint().getType().compareTo("checkin") == 0;
		assert checkin.getFootprint().getName().compareTo("checkin1") == 0;
	}
	
	@Test
	public void testSimulate() {
		CheckInCounter checkin = new CheckInCounter(0, "checkin1", new Point(0.0, 0.0));
		Message[] messages0 = checkin.simulate(0, new Message[0], new AgentFootprint[0]);
		assert messages0.length == 0;
		IPMessage inBlockMessage = new IPMessage(1L, 1, 0, "checkin", "inblock");
		inBlockMessage.addData("flight", "DLH123");
		inBlockMessage.addData("gate", new Point(0.0, 0.0).toString());
		inBlockMessage.addData("seats", "2");
		Message[] messages2 = checkin.simulate(1, new Message[] { inBlockMessage }, new AgentFootprint[0]);
		assert messages2.length == 0;
		assert checkin.getRegisteredFlights() == 1;
		TicketRequest request1 = new TicketRequest(2L, 2, 0, "DLH123");
		TicketRequest request2 = new TicketRequest(2L, 3, 0, "DLH123");
		Message[] messages3 = checkin.simulate(2, new Message[] { request1, request2 }, new AgentFootprint[0]);
		assert messages3.length == 1;
		assert messages3[0] instanceof Ticket;
		assert messages3[0].getReceiver() == 2;
		Message[] messages4 = checkin.simulate(3, new Message[0], new AgentFootprint[0]);
		assert messages4.length == 1;
		assert messages4[0] instanceof Ticket;
		assert messages4[0].getReceiver() == 3;
	}
	
	@Test
	public void testSimulateWithGateway() {
		CheckInCounter checkin = new CheckInCounter(0, "checkin1", new Point(0.0, 0.0));
		checkin.setGateway(1);
		Message[] messages0 = checkin.simulate(0, new Message[0], new AgentFootprint[0]);
		assert messages0.length == 1;
		assert messages0[0] instanceof DNSMessage;
		assert messages0[0].getReceiver() == 1;
	}
	
	@Test
	public void testIsDone() {
		CheckInCounter checkin = new CheckInCounter(0, "checkin1", new Point(0.0, 0.0));
		assert !checkin.isDone();
	}
	
	@Test
	public void testGetWidth() {
		CheckInCounter checkin = new CheckInCounter(0, "checkin1", new Point(0.0, 0.0));
		assert checkin.getWidth() > 0.0;
	}
	
	@Test
	public void testGetLength() {
		CheckInCounter checkin = new CheckInCounter(0, "checkin1", new Point(0.0, 0.0));
		assert checkin.getLength() > 0.0;
	}
	
}
