package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import javafx.scene.layout.VBox;

public class PreSimulation extends State {
    public PreSimulation(AirportAgentSim aas) {
        super(aas);
    }

    @Override
    public void addEntity(Entity entity) {
        // TODO At this stage, the added entities are saved to the configuration file.
    }

    @Override
    public VBox configureEntity(Entity entity) {
        // TODO All configuration options from the configuration file are available.
        return null;
    }
}
