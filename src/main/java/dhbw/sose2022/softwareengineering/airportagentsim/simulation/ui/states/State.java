package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;


public abstract class State {
    AirportAgentSim aas;

    public State(AirportAgentSim aas) {
        this.aas = aas;
    }

    public abstract void addEntity(Entity entity);

    public abstract void configureEntity(Entity entity);
}
