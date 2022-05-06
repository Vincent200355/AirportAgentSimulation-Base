package aas.system;

import org.junit.Test;

import aas.controller.AgentController;
import aas.model.civil.Aircraft;
import aas.model.civil.CheckInCounter;
import aas.model.civil.pax.SimplePax;
import aas.model.criminal.SimpleBraggart;
import aas.model.security.SimpleOfficer;
import aas.model.util.Point;

public class SecuritySystemTest {
	
	@Test
	public void test1() {
		CheckInCounter checkin = new CheckInCounter(0, "counter1", new Point(10.0, 10.0));
		Aircraft aircraft = new Aircraft(1, new Point(20.0, 20.0), "DLH123", 1);
		aircraft.setGateway(checkin.getFootprint().getId());
		SimplePax agent1 = new SimplePax(2, "juergen", new Point(0.0, 0.0), "DLH123");
		SimpleOfficer agent2 = new SimpleOfficer(3, "john mclain", new Point(0.0, 0.0));
		AgentController controller = new AgentController();
		controller.add(checkin);
		controller.add(aircraft);
		controller.add(agent1);
		controller.add(agent2);
		controller.setSimulationCycles(100);
		controller.run();
	}
	
	@Test
	public void test2() {
		CheckInCounter checkin = new CheckInCounter(0, "counter1", new Point(10.0, 10.0));
		Aircraft aircraft = new Aircraft(1, new Point(20.0, 20.0), "DLH123", 1);
		aircraft.setGateway(checkin.getFootprint().getId());
		SimplePax agent1 = new SimplePax(2, "juergen", new Point(0.0, 0.0), "DLH123");
		SimpleOfficer officer1 = new SimpleOfficer(3, "john mclain", new Point(0.0, 0.0));
		SimpleBraggart criminal1 = new SimpleBraggart(4, "averal", new Point(5.0, 5.0));
		AgentController controller = new AgentController();
		controller.add(checkin);
		controller.add(aircraft);
		controller.add(agent1);
		controller.add(officer1);
		controller.add(criminal1);
		controller.setSimulationCycles(100);
		controller.run();
		assert !officer1.getFootprint().equals(new Point(0.0, 0.0));
	}
	
	@Test
	public void test3() {
		CheckInCounter checkin = new CheckInCounter(0, "counter1", new Point(10.0, 10.0));
		Aircraft aircraft = new Aircraft(1, new Point(20.0, 20.0), "DLH123", 1);
		aircraft.setGateway(checkin.getFootprint().getId());
		SimplePax agent1 = new SimplePax(2, "juergen", new Point(0.0, 0.0), "DLH123");
		SimpleOfficer officer1 = new SimpleOfficer(3, "john mclain", new Point(0.0, 0.0));
		SimpleBraggart criminal1 = new SimpleBraggart(4, "averal", new Point(1.0, 1.0));
		AgentController controller = new AgentController();
		controller.add(checkin);
		controller.add(aircraft);
		controller.add(agent1);
		controller.add(officer1);
		controller.add(criminal1);
		controller.setSimulationCycles(100);
		controller.run();
		assert criminal1.isDone();
	}
	
}
