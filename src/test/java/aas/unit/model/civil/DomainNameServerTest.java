package aas.unit.model.civil;

import aas.model.communication.network.internet.IPMessage;
import aas.model.communication.network.internet.DNSMessage;
import aas.model.AgentFootprint;
import aas.model.communication.Message;
import aas.model.AgentRole;
import org.junit.Test;
import aas.model.civil.DomainNameServer;
import aas.model.util.Point;

public class DomainNameServerTest {
	
	@Test
	public void testDomainNameServerIntStringPoint() {
		DomainNameServer server = new DomainNameServer(0, "server1", new Point(0.0, 0.0));
		assert server != null;
	}
	
	@Test
	public void testGetFootprint() {
		DomainNameServer server = new DomainNameServer(0, "server1", new Point(0.0, 0.0));
		assert server.getFootprint().getId() == 0;
		assert server.getFootprint().getName().compareTo("server1") == 0;
		assert server.getFootprint().getType().compareTo("dns") == 0;
		assert server.getFootprint().getRole() == AgentRole.Civil;
		assert server.getFootprint().getPosition().equals(new Point(0.0, 0.0));
	}
	
	@Test
	public void testSimulate() {
		DomainNameServer server = new DomainNameServer(0, "server1", new Point(0.0, 0.0));
		Message[] out1 = server.simulate(0, new Message[0], new AgentFootprint[0]);
		assert out1.length == 0;
		Message[] in2a = { new DNSMessage(1L, 1, 0, new String[] { "test1", "test2" }) };
		Message[] out2a = server.simulate(1, in2a, new AgentFootprint[0]);
		assert out2a.length == 0;
		IPMessage message2b = new IPMessage(2L, 2, 0, "test1", "testMessage");
		message2b.addData("test", "1");
		Message[] in2b = { message2b };
		Message[] out2b = server.simulate(2, in2b, new AgentFootprint[0]);
		assert out2b.length == 1;
		assert out2b[0].getReceiver() == 1;
		assert out2b[0].getSender() == 2;
		assert ((IPMessage) out2b[0]).getCaption().compareTo("testMessage") == 0;
		assert ((IPMessage) out2b[0]).getData("test").compareTo("1") == 0;
	}
	
	@Test
	public void testIsDone() {
		DomainNameServer server = new DomainNameServer(0, "server1", new Point(0.0, 0.0));
		assert !server.isDone();
	}
	
}
