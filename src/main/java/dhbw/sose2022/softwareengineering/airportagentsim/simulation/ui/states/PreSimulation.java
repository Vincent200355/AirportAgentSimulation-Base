package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
        gridPane.setPadding(new Insets(10, 0, 10, 0));

        {
            // Position
            javafx.scene.control.Label positionLabel = new Label("Position");
            positionLabel.setPadding(new Insets(0, 10, 0, 10));

            javafx.scene.control.TextField positionTextField = new javafx.scene.control.TextField();
            positionTextField.setText(
                    entity.getPosition().getX()
                            + " | "
                            + entity.getPosition().getY());
            positionTextField.setPrefWidth(60);
            positionTextField.setOnInputMethodTextChanged(actionEvent -> entity.setWidth(100));

            gridPane.add(positionLabel, 0, 1);
            gridPane.add(positionTextField, 1, 1);

            // Width
            javafx.scene.control.Label widthLabel = new Label("Width");
            widthLabel.setPadding(new Insets(0, 10, 0, 10));

            javafx.scene.control.TextField widthTextField = new javafx.scene.control.TextField();
            widthTextField.setText(Integer.toString(entity.getWidth()));
            widthTextField.setPrefWidth(60);
            widthTextField.setOnInputMethodTextChanged(actionEvent -> entity.setWidth(100));

            gridPane.add(widthLabel, 0, 2);
            gridPane.add(widthTextField, 1, 2);

            // Height
            javafx.scene.control.Label heightLabel = new Label("Height");
            heightLabel.setPadding(new Insets(0, 10, 0, 10));

            javafx.scene.control.TextField heightTextField = new javafx.scene.control.TextField();
            heightTextField.setText(Integer.toString(entity.getHeight()));
            heightTextField.setPrefWidth(60);
            heightTextField.setOnInputMethodTextChanged(actionEvent -> entity.setWidth(100));

            gridPane.add(heightLabel, 0, 3);
            gridPane.add(heightTextField, 1, 3);

            // Generates
            javafx.scene.control.Label generatesLabel = new Label("Generates");
            generatesLabel.setPadding(new Insets(0, 10, 0, 10));

            javafx.scene.control.TextArea generatesTextArea = new javafx.scene.control.TextArea();
            generatesTextArea.setPrefWidth(150);

            gridPane.add(generatesLabel, 0, 4);
            gridPane.add(generatesTextArea, 1, 4);

            // Plugin Attributes
            javafx.scene.control.Label pluginAttributesLabel = new Label("Plugin Attributes");
            pluginAttributesLabel.setPadding(new Insets(0, 10, 0, 10));

            javafx.scene.control.TextArea pluginAttributesTextArea = new javafx.scene.control.TextArea();
            pluginAttributesTextArea.setPrefWidth(150);

            gridPane.add(pluginAttributesLabel, 0, 5);
            gridPane.add(pluginAttributesTextArea, 1, 5);

            // delete
            Button deleteButton = new Button("delete");
            deleteButton.setAlignment(Pos.CENTER);

            gridPane.add(deleteButton, 0, 6, 2, 1);
            gridPane.setHalignment(deleteButton, HPos.CENTER);
        }
        return gridPane;
    }

//    @Override
//    public GridPane configureEntity(Entity entity) {
//        // TODO All configuration options from the configuration file are available.
//        // Create a Javafx formula with 6 text fields and labels
//        GridPane gridPane = new GridPane();
//        gridPane.setVgap(5);
//        gridPane.setPadding(new Insets(10, 0, 10, 0));
//        int row = 0;
//        for (String key : EntityConfiguration.DEFAULT_KEY_SET) {
//            javafx.scene.control.Label label = new Label(key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase());
//            label.setPadding(new Insets(, 10, 0, 0));10//            javafx.scene.control.TextField textField = new javafx.scene.control.TextField();
//            textField.setOnInputMethodTextChanged(actionEvent -> entity.setWidth(100));
//            gridPane.add(label, 0, row);
//            gridPane.add(textField, 1, row);
//            row++;
//        }
//
//        return gridPane;
//    }

    @Override
    public String getName() {
        return "Configuration.";
    }
}
