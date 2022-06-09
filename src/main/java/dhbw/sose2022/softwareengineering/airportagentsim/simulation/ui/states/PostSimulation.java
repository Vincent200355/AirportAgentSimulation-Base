package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.UIController;
import javafx.scene.layout.GridPane;

public class PostSimulation extends State {
    public PostSimulation(AirportAgentSim aas, UIController uiController) {
        super(aas, uiController);
    }

    @Override
    public void addEntity(Entity entity) {
        // TODO It is not possible to add entities after completion of the simulation.
    }

    @Override
    public void deleteEntity(Entity entity) {
        // TODO document why this method is empty
    }

    @Override
    public GridPane configureEntity(Entity entity) {
        // TODO It is not possible to adjust entities after completion of the simulation.
        return null;
    }

    @Override
    public String getName() {
        return "Simulation is finished.";
    }

    @Override
    public void setPosition(Entity entity, int x, int y) {

    }
}
