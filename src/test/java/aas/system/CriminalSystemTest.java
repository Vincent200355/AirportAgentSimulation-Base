package aas.system;

import org.junit.Test;

import aas.controller.AgentController;
import aas.model.civil.Aircraft;
import aas.model.civil.CheckInCounter;
import aas.model.civil.pax.SimplePax;
import aas.model.criminal.SimpleBraggart;
import aas.model.security.SimpleOfficer;
import aas.model.util.Point;

public class CriminalSystemTest {
	
	@Test
	public void test() {
		CheckInCounter checkin = new CheckInCounter(0, "counter1", new Point(10.0, 10.0));
		Aircraft aircraft = new Aircraft(1, new Point(20.0, 20.0), "DLH123", 1);
		SimplePax agent1 = new SimplePax(2, "juergen", new Point(0.0, 0.0), "DLH123");
		SimpleOfficer agent2 = new SimpleOfficer(3, "john mclain", new Point(0.0, 0.0));
		SimpleBraggart crimnial = new SimpleBraggart(4, "joker", new Point(0.0, 0.0));
		AgentController controller = new AgentController();
		controller.add(checkin);
		controller.add(aircraft);
		controller.add(agent1);
		controller.add(agent2);
		controller.add(crimnial);
		controller.setSimulationCycles(100);
		controller.run();
	}
	
}
