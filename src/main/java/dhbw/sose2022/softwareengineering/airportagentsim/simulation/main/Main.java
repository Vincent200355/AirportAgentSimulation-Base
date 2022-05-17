package dhbw.sose2022.softwareengineering.airportagentsim.simulation.main;

import java.nio.file.Path;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;

public class Main {
	
	public static void main(String[] args) {
		
		AirportAgentSim aas = new AirportAgentSim("aas", Path.of("plugin"));
		aas.run();
		
	}
	
}
