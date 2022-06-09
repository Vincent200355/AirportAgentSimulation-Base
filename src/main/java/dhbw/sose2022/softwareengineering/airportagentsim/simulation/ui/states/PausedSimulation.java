package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.UIController;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Collection;

public class PausedSimulation extends State {
    public PausedSimulation(AirportAgentSim aas, UIController uiController) {
        super(aas, uiController);
    }

    @Override
    public void addEntity(Entity entity) {
        // TODO At this stage, the added entities are saved to the configuration file.
        entity.spawn(aas.getWorld(), 20, 50, 7, 7);
    }

    public void deleteEntity(Entity entity) {
        aas.getWorld().removeEntity(entity);
        uiController.updateUI();
    }

    @Override
    public GridPane configureEntity(Entity entity) {
        int textFieldWidth = 100;
        // TODO All configuration options from the configuration file are available.
        // Create a Javafx formula with 6 text fields and labels
        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10, 0, 10, 0));

        {
            // Position
            javafx.scene.control.Label positionLabel = new Label("Position");
            positionLabel.setPadding(new Insets(0, 10, 0, 10));

            TextField positionXTextField = new TextField();
            positionXTextField.setText(Integer.toString(entity.getPosition().getX()));
            positionXTextField.setPrefWidth(textFieldWidth / 2);
            positionXTextField.setAlignment(Pos.CENTER_LEFT);
            positionXTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.matches("\\d*")) {
                            setPosition(entity, Integer.valueOf(newValue), entity.getPosition().getY());
                        } else {
                            positionXTextField.setText(oldValue);
                        }
                    }
            );

            positionXTextField.setOnKeyPressed(ke -> {
                int actualValue = Integer.valueOf(positionXTextField.getText());

                switch (ke.getCode()) {
                    case ADD:
                        positionXTextField.setText(String.valueOf(actualValue + 1));
                        break;
                    case SUBTRACT:
                        positionXTextField.setText(String.valueOf(actualValue - 1));
                        break;
                    default:
                        break;
                }
            });

            TextField positionYTextField = new TextField();
            positionYTextField.setText(Integer.toString(entity.getPosition().getY()));
            positionYTextField.setPrefWidth(textFieldWidth / 2);
            positionYTextField.setAlignment(Pos.CENTER_RIGHT);
            positionYTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.matches("\\d*")) {
                            setPosition(entity, entity.getPosition().getX(), Integer.valueOf(newValue));
                        } else {
                            positionYTextField.setText(oldValue);
                        }
                    }
            );

            positionYTextField.setOnKeyPressed(ke -> {
                int actualValue = Integer.valueOf(positionYTextField.getText());

                switch (ke.getCode()) {
                    case ADD:
                        positionYTextField.setText(String.valueOf(actualValue + 1));
                        break;
                    case SUBTRACT:
                        positionYTextField.setText(String.valueOf(actualValue - 1));
                        break;
                    default:
                        break;
                }
            });

            HBox positionPane = new HBox();
            positionPane.setPrefWidth(textFieldWidth);
            positionPane.getChildren().add(positionXTextField);
            positionPane.getChildren().add(positionYTextField);

            gridPane.add(positionLabel, 0, 1);
            gridPane.add(positionPane, 1, 1);

            // Width
            javafx.scene.control.Label widthLabel = new Label("Width");
            widthLabel.setPadding(new Insets(0, 10, 0, 10));

            javafx.scene.control.TextField widthTextField = new javafx.scene.control.TextField();
            widthTextField.setText(Integer.toString(entity.getWidth()));
            widthTextField.setPrefWidth(textFieldWidth);
            widthTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.matches("\\d*")) {
                            setWidth(entity, Integer.valueOf(newValue));
                            widthTextField.setText(Integer.toString(entity.getWidth()));
                        } else {
                            widthTextField.setText(oldValue);
                        }
                    }
            );

            widthTextField.setOnKeyPressed(ke -> {
                int actualValue = Integer.valueOf(widthTextField.getText());

                switch (ke.getCode()) {
                    case ADD:
                        widthTextField.setText(String.valueOf(actualValue + 1));
                        break;
                    case SUBTRACT:
                        widthTextField.setText(String.valueOf(actualValue - 1));
                        break;
                    default:
                        break;
                }
            });

            gridPane.add(widthLabel, 0, 2);
            gridPane.add(widthTextField, 1, 2);

            // Height
            javafx.scene.control.Label heightLabel = new Label("Height");
            heightLabel.setPadding(new Insets(0, 10, 0, 10));

            javafx.scene.control.TextField heightTextField = new javafx.scene.control.TextField();
            heightTextField.setText(Integer.toString(entity.getHeight()));
            heightTextField.setPrefWidth(textFieldWidth);
            heightTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.matches("\\d*")) {
                            setHeight(entity, Integer.valueOf(newValue));
                            heightTextField.setText(Integer.toString(entity.getHeight()));
                        } else {
                            heightTextField.setText(oldValue);
                        }
                    }
            );
            heightTextField.setOnKeyPressed(ke -> {
                int actualValue = Integer.valueOf(heightTextField.getText());

                switch (ke.getCode()) {
                    case ADD:
                        heightTextField.setText(String.valueOf(actualValue + 1));
                        break;
                    case SUBTRACT:
                        heightTextField.setText(String.valueOf(actualValue - 1));
                        break;
                    default:
                        break;
                }
            });

            gridPane.add(heightLabel, 0, 3);
            gridPane.add(heightTextField, 1, 3);

            // delete
            Button deleteButton = new Button("delete");
            deleteButton.setAlignment(Pos.CENTER);
            // call deleteEntity() if clicked
            deleteButton.setOnMouseClicked(m -> deleteEntity(entity));

            gridPane.add(deleteButton, 0, 6, 2, 1);
            gridPane.setHalignment(deleteButton, HPos.CENTER);
        }
        return gridPane;
    }

    private void setWidth(Entity entity, int width) {
        Collection<? extends Entity> colliding = aas.getWorld().getEntities(
                entity.getPosition().getX(),
                entity.getPosition().getY(),
                width,
                entity.getHeight());
        colliding.remove(entity);

        if (colliding.isEmpty())
            entity.setWidth(width);
        uiController.updateUI();
    }

    private void setHeight(Entity entity, int height) {
        Collection<? extends Entity> colliding = aas.getWorld().getEntities(
                entity.getPosition().getX(),
                entity.getPosition().getY(),
                entity.getWidth(),
                height);
        colliding.remove(entity);

        if (colliding.isEmpty())
            entity.setHeight(height);
        uiController.updateUI();
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
        return "Simulation is paused.";
    }

    public void setPosition(Entity entity, int x, int y) {
        //TODO improve
        Collection<? extends Entity> colliding = aas.getWorld().getEntities(
                x,
                y,
                entity.getWidth(),
                entity.getHeight()
        );
        colliding.remove(entity);
        if (colliding.isEmpty()) {
            Point pos = new Point(x, y);
            entity.setPosition(pos);
        }
        uiController.updateUI();
    }
}
