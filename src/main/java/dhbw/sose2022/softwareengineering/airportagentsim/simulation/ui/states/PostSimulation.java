package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import javafx.scene.layout.GridPane;

public class PostSimulation extends State {
    public PostSimulation(AirportAgentSim aas) {
        super(aas);
    }

    @Override
    public void addEntity(Entity entity) {
        // TODO It is not possible to add entities after completion of the simulation.
    }

    @Override
    public GridPane configureEntity(Entity entity) {
        // TODO It is not possible to adjust entities after completion of the simulation.
        return null;
    }
}
