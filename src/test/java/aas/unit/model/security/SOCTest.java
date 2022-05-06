package aas.unit.model.security;

import aas.model.communication.Message;
import aas.model.communication.network.radio.PoliceRadio;
import aas.model.communication.network.radio.PoliceCode;
import aas.model.AgentFootprint;
import aas.model.AgentRole;
import org.junit.Test;
import aas.model.security.SecurityOperationsCenter;
import aas.model.util.Point;

public class SOCTest {
	
	@Test
	public void testSecurityOperationsCenter() {
		SecurityOperationsCenter soc = new SecurityOperationsCenter(0, "soc", new Point(0.0, 0.0));
		assert soc != null;
	}
	
	@Test
	public void testGetFootprint() {
		SecurityOperationsCenter soc = new SecurityOperationsCenter(0, "soc", new Point(0.0, 0.0));
		assert soc.getFootprint() != null;
		assert soc.getFootprint().getId() == 0;
		assert soc.getFootprint().getRole() == AgentRole.Security;
		assert soc.getFootprint().getName().compareTo("soc") == 0;
		assert soc.getFootprint().getPosition().equals(new Point(0.0, 0.0));
	}
	
	@Test
	public void testSimulate() {
		SecurityOperationsCenter soc = new SecurityOperationsCenter(0, "soc", new Point(0.0, 0.0));
		AgentFootprint officer1 = new AgentFootprint(1, AgentRole.Security, "simpleofficer", "johnMclain", new Point(0.0, 0.0));
		AgentFootprint officer2 = new AgentFootprint(2, AgentRole.Security, "simpleofficer", "LuckyLuke", new Point(0.0, 0.0));
		PoliceRadio register1 = new PoliceRadio(0L, 1, PoliceCode.OfficerOnDuty);
		PoliceRadio register2 = new PoliceRadio(0L, 2, PoliceCode.OfficerOnDuty);
		Message[] com = soc.simulate(0, new Message[] { register1, register2 }, new AgentFootprint[0]);
		assert soc.getShiftSize() == 2;
		assert com.length == 0;
		AgentFootprint criminal = new AgentFootprint(2, AgentRole.Criminal, "dummy", "joe", new Point(1.0, 1.0));
		PoliceRadio detected = new PoliceRadio(0L, 1, PoliceCode.CriminalDetected, criminal);
		com = soc.simulate(0, new Message[] { detected }, new AgentFootprint[0]);
		assert com.length == 1;
		assert ((PoliceRadio) com[0]).getCode() == PoliceCode.ChaseCriminal;
		PoliceRadio arrested = new PoliceRadio(0L, 2, PoliceCode.CriminalArrested, criminal);
		com = soc.simulate(0, new Message[] { arrested }, new AgentFootprint[0]);
		assert com.length == 1;
		assert ((PoliceRadio) com[0]).getCode() == PoliceCode.StopChase;
	}
	
	@Test
	public void testIsDone() {
		SecurityOperationsCenter soc = new SecurityOperationsCenter(0, "soc", new Point(0.0, 0.0));
		assert !soc.isDone();
	}
	
}
