package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.UIController;
import javafx.scene.layout.GridPane;


public abstract class State {
    AirportAgentSim aas;
    UIController uiController;

    State(AirportAgentSim aas, UIController uiController) {
        this.aas = aas;
        this.uiController = uiController;
    }

    public abstract void addEntity(Entity entity);

    public abstract void deleteEntity(Entity entity);

    public abstract GridPane configureEntity(Entity entity);

    public abstract String getName();

    public abstract void setPosition(Entity entity, int x, int y);
}
