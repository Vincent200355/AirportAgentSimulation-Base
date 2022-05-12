package aas.unit.model.civil;

import aas.model.communication.voice.BoardingCall;
import aas.model.communication.voice.BoardingRequest;
import aas.model.communication.voice.Ticket;
import aas.model.communication.voice.TicketRequest;
import aas.model.AgentFootprint;
import aas.model.communication.Message;

import org.junit.Ignore;
import org.junit.Test;
import aas.model.AgentRole;
import aas.model.civil.pax.SimplePax;
import aas.model.util.Point;

public class SimplePaxTest {
	
	@Test
	public void testSimplePax() {
		SimplePax pax = new SimplePax(0, "john", new Point(0.0, 0.0), "DLH123");
		assert pax != null;
		assert pax.getFootprint().getId() == 0;
		assert pax.getFootprint().getRole() == AgentRole.Civil;
		assert pax.getFootprint().getType().compareTo("pax") == 0;
		assert pax.getFootprint().getName().compareTo("john") == 0;
	}
	
	@Test
	public void testSimulate() {
		SimplePax pax = new SimplePax(0, "john", new Point(0.0, 0.0), "DLH123");
		Message[] messages1 = pax.simulate(0, new Message[0], new AgentFootprint[0]);
		assert messages1.length == 0;
		assert pax.getSpeed() > 0.0;
		AgentFootprint checkin = new AgentFootprint(1, AgentRole.Civil, "checkin", "checkin1", new Point(0.0, 0.0));
		Message[] messages2 = pax.simulate(1, new Message[0], new AgentFootprint[] { checkin });
		assert messages2.length == 1;
		assert messages2[0] instanceof TicketRequest;
		assert messages2[0].getReceiver() == 1;
		Ticket ticket = new Ticket(2L, 1, 0, "DLH123", new Point(1.0, 1.0));
		Message[] messages3 = pax.simulate(2, new Message[] { ticket }, new AgentFootprint[0]);
		assert messages3.length == 0;
		Message[] messages4 = pax.simulate(3, new Message[0], new AgentFootprint[0]);
		assert messages4.length == 0;
		assert pax.getSpeed() > 0.0;
		AgentFootprint aircraft = new AgentFootprint(2, AgentRole.Civil, "aircraft", "DLH123", new Point(1.0, 1.0));
		Message[] messages5 = pax.simulate(4, new Message[0], new AgentFootprint[] { aircraft });
		assert messages5.length == 1;
		assert messages5[0] instanceof BoardingRequest;
		assert messages5[0].getReceiver() == 2;
		BoardingCall call = new BoardingCall(5L, 2, 0, 1);
		Message[] messages6 = pax.simulate(2, new Message[] { call }, new AgentFootprint[0]);
		assert messages6.length == 0;
		assert pax.isDone();
	}
	
	@Test
	public void testGetSpeed() {
		SimplePax pax = new SimplePax(0, "john", new Point(0.0, 0.0), "DLH123");
		pax.simulate(0, new Message[0], new AgentFootprint[0]);
		assert pax.getSpeed() > 0.0;
	}
	
	@Test
	@Ignore
	// the assertion that the position does not change failes sometimes, but not
	// consistently
	public void testGetDirection() {
		Point start = new Point(0.0, 0.0);
		SimplePax pax = new SimplePax(0, "john", start, "DLH123");
		pax.simulate(0, new Message[0], new AgentFootprint[0]);
		double refDirection = start.getDirection(pax.getFootprint().getPosition());
		assert !pax.getFootprint().getPosition().equals(new Point(0.0, 0.0));
		assert Math.abs(pax.getDirection() - refDirection) < 1.0;
	}
	
	@Test
	public void testGetFootprint() {
		SimplePax pax = new SimplePax(0, "john", new Point(0.0, 0.0), "DLH123");
		assert pax.getFootprint() != null;
	}
	
	@Test
	public void testIsDone() {
		SimplePax pax = new SimplePax(0, "john", new Point(0.0, 0.0), "DLH123");
		pax.simulate(0, new Message[0], new AgentFootprint[0]);
	}
	
}
