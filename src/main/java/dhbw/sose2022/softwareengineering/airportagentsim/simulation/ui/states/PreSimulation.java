package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.EntityConfiguration;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;


public class PreSimulation extends State {
    public PreSimulation(AirportAgentSim aas) {
        super(aas);
    }

    @Override
    public void addEntity(Entity entity) {
        // TODO At this stage, the added entities are saved to the configuration file.
        entity.spawn(aas.getWorld(), 20, 50, 7, 7);
    }

    @Override
    public GridPane configureEntity(Entity entity) {
        // TODO All configuration options from the configuration file are available.
        // Create a Javafx formula with 6 text fields and labels
        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        int row = 0;
        for (String key : EntityConfiguration.DEFAULT_KEY_SET) {
            javafx.scene.control.Label label = new Label(key);
            label.setPadding(new Insets(0, 10, 0, 0));
            javafx.scene.control.TextField textField = new javafx.scene.control.TextField();
            textField.setOnInputMethodTextChanged(actionEvent -> entity.setWidth(100));
            gridPane.add(label, 0, row);
            gridPane.add(textField, 1, row);
            row++;
        }

        return gridPane;
    }
}
