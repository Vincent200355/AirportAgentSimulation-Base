package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.EntityConfiguration;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.SimulationConfiguration;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class UIController {
    @FXML
    private TreeView libraryTreeView;

    @FXML
    private AnchorPane viewPane;

    @FXML
    private TextField feedbackLabel;
    @FXML
    private Button stopButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button startButton;

    @FXML
    public void initialize() throws IOException {
        initializeLibrary();
        initializeView();
        initializeSimulationObjects();
    }

    private void initializeSimulationObjects() {
    }

    private void initializeView() throws IOException {
        Rectangle world = new Rectangle();
        world.setFill(Color.rgb(255, 255, 255));

        SimulationConfiguration config = new SimulationConfiguration();
        world.setHeight(config.getHeight());
        world.setWidth(config.getWidth());

        EntityConfiguration[] entities = config.getPlacedEntities();

        viewPane.getChildren().add(world);

        for (EntityConfiguration ec : entities) {
            Circle entity = new Circle();
            entity.setCenterX(ec.getPosition()[0]);
            entity.setCenterY(ec.getPosition()[1]);
            entity.setRadius(ec.getWidth());
            entity.setFill(Color.rgb(255, 20, 20));
            viewPane.getChildren().add(entity);
        }
    }

    private void initializeLibrary() {
        TreeItem root = new TreeItem("invisibleRootElement");

        TreeItem<String> invaderNode = new TreeItem<String>("Invaders");
        {
            TreeItem<String> invaderNode1 = new TreeItem<String>("Invaders1");
            TreeItem<String> invaderNode2 = new TreeItem<String>("Invaders2");
            TreeItem<String> invaderNode3 = new TreeItem<String>("Invaders3");

            invaderNode.getChildren().addAll(invaderNode1, invaderNode2, invaderNode3);
        }
        TreeItem<String> passengerNode = new TreeItem<>("Passengers");
        TreeItem<String> securityNode = new TreeItem<>("Security");
        {
            TreeItem<String> securityNode1 = new TreeItem<>("Security1");
            TreeItem<String> securityNode2 = new TreeItem<>("Security2");
            TreeItem<String> securityNode3 = new TreeItem<>("Security3");
            securityNode3.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                Circle object = new Circle();
                object.setRadius(500);
                object.setCenterX(200);
                object.setCenterY(200);
                object.setFill(Color.rgb(255, 20, 20));
                viewPane.getChildren().add(object);
                mouseEvent.consume();
            });

            securityNode.getChildren().addAll(securityNode1, securityNode2, securityNode3);
        }
        TreeItem<String> objectNode = new TreeItem<String>("Objects");

        root.getChildren().addAll(passengerNode, invaderNode, securityNode, objectNode);
        libraryTreeView.setRoot(root);
    }

    public void stopSimulation() {
        feedbackLabel.setText("stopped");
    }

    public void pauseSimulation() {
        feedbackLabel.setText("paused");
    }

    public void startSimulation() {
        feedbackLabel.setText("running");
    }

    public void loadPlugins() {

    }
}
