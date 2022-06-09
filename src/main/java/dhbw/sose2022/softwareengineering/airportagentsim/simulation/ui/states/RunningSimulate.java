package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.UIController;
import javafx.scene.layout.GridPane;

public class RunningSimulate extends State {
    public RunningSimulate(AirportAgentSim aas, UIController uiController) {
        super(aas, uiController);
    }

    @Override
    public void addEntity(Entity entity) {
        // TODO It is not possible to add entities while the simulation is running.
    }

    @Override
    public GridPane configureEntity(Entity entity) {
        // TODO It is not possible to adjust entities while the simulation is running.
        return null;
    }

    @Override
    public String getName() {
        return "Simulation is running.";
    }

    @Override
    public void deleteEntity(Entity entity) {

    }

    @Override
    public void setPosition(Entity entity, int x, int y) {

    }
}
