package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.EntityConfiguration;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.SimulationConfiguration;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.IllegalComponentStateException;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class UIController {
	
	private AirportAgentSim aas;
	
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

    // x und y Werte unserer Main Scene
    double mainSceneX, mainSceneY;

    @FXML
    public void initialize() throws IOException {
        initializeLibrary();
        initializeView();
        initializeSimulationObjects();
    }
    
    public void initializeAAS(AirportAgentSim aas) {
    	if(this.aas != null)
    		throw new IllegalStateException();
    	if(aas == null)
    		throw new IllegalArgumentException();
    	this.aas = aas;
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
            if (ec.getHeight() == 0) {
                Circle entity = new Circle();
                entity.setCenterX(ec.getPosition()[0]);
                entity.setCenterY(ec.getPosition()[1]);
                entity.setRadius(7);
                entity.setFill(Color.rgb(20, 255, 20));
                viewPane.getChildren().add(entity);
            } else {
                Rectangle entity = new Rectangle();
                entity.setX(ec.getPosition()[0]);
                entity.setY(ec.getPosition()[1]);
                if (entity.getY() + ec.getHeight() > world.getHeight())
                    entity.setHeight(world.getHeight() - entity.getY());
                else
                    entity.setHeight(ec.getHeight());
                if (entity.getX() + ec.getWidth() > world.getWidth())
                    entity.setWidth(world.getWidth() - entity.getX());
                else
                    entity.setWidth(ec.getWidth());
                entity.setFill(Color.rgb(255, 20, 20));
                entity.setOnMousePressed((t) -> {
                    mainSceneX = t.getSceneX();
                    mainSceneY = t.getSceneY();

                    Rectangle r = (Rectangle) (t.getSource());
                    r.toFront();
                });

                entity.setOnMouseDragged((t) -> {
                    double offsetX = t.getSceneX() - mainSceneX;
                    double offsetY = t.getSceneY() - mainSceneY;

                    Rectangle r = (Rectangle) (t.getSource());

                    r.setX(r.getX() + offsetX);
                    r.setY(r.getY() + offsetY);

                    mainSceneX = t.getSceneX();
                    mainSceneY = t.getSceneY();
                });

                entity.setOnMouseReleased((t) -> {
                    sortView();
                });
                viewPane.getChildren().add(entity);
            }
            sortView();
        }
    }

    private void sortView() {
        ObservableList<Node> workingList = FXCollections.observableArrayList(
                viewPane.getChildren()
        );
        Collections.sort(workingList, (o1, o2) -> {
            String class1 = o1.getClass().toString();
            String class2 = o2.getClass().toString();
            if (o1 instanceof Rectangle && o1.getClass().equals(o2.getClass())) {
                if (((Rectangle) o1).getWidth() < ((Rectangle) o2).getWidth())
                    return 1;
                if (((Rectangle) o1).getWidth() > ((Rectangle) o2).getWidth())
                    return -1;
                else {
                    if (((Rectangle) o1).getHeight() < ((Rectangle) o2).getHeight())
                        return 1;
                    if (((Rectangle) o1).getHeight() > ((Rectangle) o2).getHeight())
                        return -1;
                    else
                        return 0;
                }

            }
            return class2.compareTo(class1);
        });

        viewPane.getChildren().setAll(workingList);
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

            securityNode.getChildren().addAll(securityNode1, securityNode2, securityNode3);
        }
        TreeItem<String> objectNode = new TreeItem<String>("Objects");

        root.getChildren().addAll(passengerNode, invaderNode, securityNode, objectNode);
        libraryTreeView.setRoot(root);

        libraryTreeView.getSelectionModel().selectedItemProperty().addListener(this::addEntityToView);
    }

    private void updateView() throws IOException {
        viewPane.getChildren().clear();
        initializeView();
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

    public void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
    }

    public void addEntityToView(ObservableValue observable, Object oldValue, Object newValue) {
        Circle object = new Circle();
        object.setRadius(7);
        object.setCenterY(200);

        switch ((String) ((TreeItem) newValue).getValue()) {
            case "Security3":
                object.setCenterX(200);
                object.setFill(Color.rgb(20, 100, 20));
                viewPane.getChildren().add(object);
                break;
            case "Security2":
                object.setCenterX(250);
                object.setFill(Color.rgb(100, 200, 20));
                viewPane.getChildren().add(object);
                break;
            case "Security1":
                object.setCenterX(300);
                object.setFill(Color.rgb(150, 200, 20));
                viewPane.getChildren().add(object);
                break;
            case "Invaders1":
                object.setCenterX(350);
                object.setFill(Color.rgb(255, 20, 20));
                viewPane.getChildren().add(object);
                break;
        }
    }
}
