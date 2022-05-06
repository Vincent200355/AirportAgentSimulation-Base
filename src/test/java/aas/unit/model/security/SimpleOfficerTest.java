package aas.unit.model.security;

import aas.model.communication.manual.ArrestCall;
import aas.model.communication.network.radio.PoliceRadio;
import aas.model.communication.network.radio.PoliceCode;
import aas.model.AgentRole;
import aas.model.AgentFootprint;
import aas.model.communication.Message;
import org.junit.Test;
import aas.model.security.SimpleOfficer;
import aas.model.util.Point;

public class SimpleOfficerTest {
	
	@Test
	public void testSimpleOfficer() {
		SimpleOfficer officer = new SimpleOfficer(0, "john mclain", new Point(0.0, 0.0));
		assert officer != null;
	}
	
	@Test
	public void testGetSpeed() {
		SimpleOfficer officer = new SimpleOfficer(0, "john mclain", new Point(0.0, 0.0));
		officer.simulate(0, new Message[0], new AgentFootprint[0]);
		assert officer.getSpeed() < 1.0;
		AgentFootprint criminal = new AgentFootprint(2, AgentRole.Criminal, "dummy", "joe", new Point(1.0, 1.0));
		PoliceRadio chaseMessage = new PoliceRadio(0L, -1, PoliceCode.ChaseCriminal, criminal);
		officer.simulate(1, new Message[] { chaseMessage }, new AgentFootprint[0]);
		assert officer.getSpeed() > 1.0;
	}
	
	@Test
	public void testGetDirection() {
		SimpleOfficer officer = new SimpleOfficer(0, "john mclain", new Point(0.0, 0.0));
		officer.simulate(0, new Message[0], new AgentFootprint[0]);
		assert officer.getDirection() < 1.0;
		AgentFootprint criminal = new AgentFootprint(2, AgentRole.Criminal, "dummy", "joe", new Point(1.0, 1.0));
		PoliceRadio chaseMessage = new PoliceRadio(0L, -1, PoliceCode.ChaseCriminal, criminal);
		officer.simulate(1, new Message[] { chaseMessage }, new AgentFootprint[0]);
		assert Math.abs(officer.getDirection()) - 45.0 < 1.0;
	}
	
	@Test
	public void testGetFootprint() {
		SimpleOfficer officer = new SimpleOfficer(0, "john mclain", new Point(0.0, 0.0));
		AgentFootprint footprint = officer.getFootprint();
		assert footprint != null;
		assert footprint.getId() == 0;
		assert footprint.getType().compareTo("simpleOfficer") == 0;
		assert footprint.getName().compareTo("john mclain") == 0;
		assert footprint.getPosition().equals(new Point(0.0, 0.0));
	}
	
	@Test
	public void testSimulate() {
		SimpleOfficer officer = new SimpleOfficer(0, "john mclain", new Point(0.0, 0.0));
		Message[] out0 = officer.simulate(0, new Message[0], new AgentFootprint[0]);
		assert out0.length == 0;
		assert officer.getSpeed() < 1.0;
		AgentFootprint criminal = new AgentFootprint(1, AgentRole.Criminal, "criminal", "Averal", new Point(10.0, 10.0));
		Message[] out2 = officer.simulate(1, new Message[0], new AgentFootprint[] { criminal });
		assert out2.length == 1;
		assert out2[0] instanceof PoliceRadio;
		assert officer.getSpeed() > 1.0;
		final Message[] out3 = officer.simulate(2, new Message[0], new AgentFootprint[] { criminal });
		assert out3.length == 2;
		assert out3[0] instanceof ArrestCall;
		assert out3[1] instanceof PoliceRadio;
		final Message[] out4 = officer.simulate(3, new Message[0], new AgentFootprint[0]);
		assert out4.length == 0;
		assert officer.getSpeed() < 1.0;
		AgentFootprint criminal2 = new AgentFootprint(2, AgentRole.Criminal, "criminal2", "Jack", new Point(50.0, 50.0));
		PoliceRadio socCall1 = new PoliceRadio(4L, 3, PoliceCode.ChaseCriminal, criminal2);
		Message[] out5 = officer.simulate(4, new Message[] { socCall1 }, new AgentFootprint[0]);
		assert out5.length == 0;
		assert officer.getSpeed() > 1.0;
		PoliceRadio socCall2 = new PoliceRadio(4L, 3, PoliceCode.StopChase);
		Message[] out6 = officer.simulate(5, new Message[] { socCall2 }, new AgentFootprint[0]);
		assert out6.length == 0;
		assert officer.getSpeed() < 1.0;
	}
	
	@Test
	public void testIsDone() {
		SimpleOfficer officer = new SimpleOfficer(0, "john mclain", new Point(0.0, 0.0));
		assert !officer.isDone();
	}
	
}
