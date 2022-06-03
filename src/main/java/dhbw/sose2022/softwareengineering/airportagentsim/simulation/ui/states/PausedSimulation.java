package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import javafx.scene.layout.VBox;

public class PausedSimulation extends State {
    public PausedSimulation(AirportAgentSim aas) {
        super(aas);
    }

    @Override
    public void addEntity(Entity entity) {
        // TODO Insert entities directly into the simulation.
    }

    @Override
    public VBox configureEntity(Entity entity) {
        // TODO Only the changes provided by the simulation are possible.
        return null;
    }
}
