package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;

public class RunningSimulate extends State {
    public RunningSimulate(AirportAgentSim aas) {
        super(aas);
    }

    @Override
    public void addEntity(Entity entity) {
        // TODO It is not possible to add entities while the simulation is running.
    }

    @Override
    public void configureEntity(Entity entity) {
        // TODO It is not possible to adjust entities while the simulation is running.
    }
}
