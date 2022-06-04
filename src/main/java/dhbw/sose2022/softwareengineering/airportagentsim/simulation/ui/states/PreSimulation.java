package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.EntityConfiguration;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


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
    public VBox configureEntity(Entity entity) {
        // TODO All configuration options from the configuration file are available.
        // Create a Javafx formula with 6 text fields and labels
        VBox vBox = new VBox();
        for (String key : EntityConfiguration.DEFAULT_KEY_SET) {
            HBox hBox = new HBox();
            javafx.scene.control.Label label = new Label(key);
            javafx.scene.control.TextField textField = new javafx.scene.control.TextField();
            textField.setOnInputMethodTextChanged(actionEvent -> entity.setWidth(100));
            hBox.getChildren().add(label);
            hBox.getChildren().add(textField);
            vBox.getChildren().add(hBox);
        }

        return vBox;
    }
}
