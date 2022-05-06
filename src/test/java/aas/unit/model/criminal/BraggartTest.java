package aas.unit.model.criminal;

import aas.model.communication.manual.ArrestCall;
import aas.model.communication.voice.Ticket;
import aas.model.communication.voice.TicketRequest;
import aas.model.AgentRole;
import aas.model.communication.Message;
import aas.model.AgentFootprint;
import org.junit.Test;
import aas.model.criminal.SimpleBraggart;
import aas.model.util.Point;

public class BraggartTest {
	
	@Test
	public void testBraggart() {
		SimpleBraggart braggart = new SimpleBraggart(0, "averal", new Point(0.0, 0.0));
		assert braggart != null;
	}
	
	@Test
	public void testGetFootprint() {
		SimpleBraggart braggart = new SimpleBraggart(0, "averal", new Point(0.0, 0.0));
		AgentFootprint footprint = braggart.getFootprint();
		assert footprint != null;
		assert footprint.getId() == 0;
		assert footprint.getType().compareTo("braggart") == 0;
		assert footprint.getName().compareTo("averal") == 0;
		assert footprint.getPosition().equals(new Point(0.0, 0.0));
	}
	
	@Test
	public void testSimulateSuccess() {
		SimpleBraggart braggart = new SimpleBraggart(0, "averal", new Point(0.0, 0.0));
		Message[] out1 = braggart.simulate(0, new Message[0], new AgentFootprint[0]);
		assert out1.length == 0;
		assert braggart.getSpeed() > 1.0;
		AgentFootprint checkin = new AgentFootprint(1, AgentRole.Civil, "checkin", "checkin1", new Point(0.0, 0.0));
		Message[] out2 = braggart.simulate(1, new Message[0], new AgentFootprint[] { checkin });
		assert out2.length == 1;
		assert out2[0] instanceof TicketRequest;
		assert out2[0].getReceiver() == 1;
		assert braggart.getSpeed() < 1.0;
		Ticket ticket = new Ticket(2L, 1, 0, "DLH123", new Point(0.0, 0.0));
		Message[] out3 = braggart.simulate(3, new Message[] { ticket }, new AgentFootprint[0]);
		assert braggart.isDone();
	}
	
	@Test
	public void testSimulateFail() {
		SimpleBraggart braggart = new SimpleBraggart(0, "averal", new Point(0.0, 0.0));
		Message[] out1 = braggart.simulate(0, new Message[0], new AgentFootprint[0]);
		assert out1.length == 0;
		assert braggart.getSpeed() > 1.0;
		AgentFootprint checkin = new AgentFootprint(1, AgentRole.Civil, "checkin", "checkin1", new Point(0.0, 0.0));
		Message[] out2 = braggart.simulate(1, new Message[0], new AgentFootprint[] { checkin });
		assert out2.length == 1;
		assert out2[0] instanceof TicketRequest;
		assert out2[0].getReceiver() == 1;
		assert braggart.getSpeed() < 1.0;
		final ArrestCall arrest = new ArrestCall(2L, 3, 0);
		final Message[] out3 = braggart.simulate(2, new Message[] { arrest }, new AgentFootprint[0]);
		assert braggart.isDone();
	}
	
	@Test
	public void testIsDoneArrest() {
		SimpleBraggart braggart = new SimpleBraggart(0, "averal", new Point(0.0, 0.0));
		braggart.simulate(0, new Message[0], new AgentFootprint[0]);
		assert !braggart.isDone();
		ArrestCall arrest = new ArrestCall(2L, 3, 0);
		braggart.simulate(1, new Message[] { arrest }, new AgentFootprint[0]);
		assert braggart.isDone();
	}
	
	@Test
	public void testIsDoneTicket() {
		SimpleBraggart braggart = new SimpleBraggart(0, "averal", new Point(0.0, 0.0));
		braggart.simulate(0, new Message[0], new AgentFootprint[0]);
		assert !braggart.isDone();
		Ticket ticket = new Ticket(1L, 1, 0, "DLH123", new Point(0.0, 0.0));
		braggart.simulate(1, new Message[] { ticket }, new AgentFootprint[0]);
		assert braggart.isDone();
	}
	
	@Test
	public void testGetSpeed() {
		SimpleBraggart braggart = new SimpleBraggart(0, "averal", new Point(0.0, 0.0));
		braggart.simulate(0, new Message[0], new AgentFootprint[0]);
		assert braggart.getSpeed() > 1.0;
	}
	
}
