package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.MovingEntity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.EntityConfiguration;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.SimulationConfiguration;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.PluginManager;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation.SimulationWorld;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states.PreSimulation;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states.State;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.update.GUIUpdater;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UIController {
    @FXML
    private SplitPane mainSplitPlane;

    @FXML
    private TreeView<String> libraryTreeView;
    @FXML
    private TreeView<String> simulationTreeView;


    @FXML
    private AnchorPane viewPane;

    @FXML
    private VBox settingsVBox;

    @FXML
    private TextField feedbackLabel;

    private AirportAgentSim aas;
    private State currentState;
    private Set<Entity> entityLibrarySet = new HashSet<>();

    // x und y Werte unserer Main Scene
    double mainSceneX, mainSceneY;

    /**
     * This method links the UI to the underlying {@link AirportAgentSim simulation}.
     *
     * @param aas The underlying simulation
     */
    public void initializeAAS(AirportAgentSim aas) {
        if (this.aas != null)
            throw new IllegalStateException();
        if (aas == null)
            throw new IllegalArgumentException();
        this.aas = aas;
        this.aas.setGUIUpdater(new GUIUpdater(this::updateView));
    }

    /**
     * Initializes the GUI based on the {@link AirportAgentSim simulation} data.
     */
    public void initializeGUI() {
        setState(new PreSimulation(this.aas));
        initializeLibrary();
        initializeSimulationObjects();
        initializeView();
    }

    private VBox getSettings(Entity e) {
        VBox settingsEntityVBox = new VBox();
        for (String key : EntityConfiguration.DEFAULT_KEY_SET) {
            HBox attribute = new HBox();
            attribute.setPadding(new Insets(5, 0, 5, 0));

            Label label = new Label(key + ": ");
            label.setPadding(new Insets(0, 10, 0, 0));
            attribute.getChildren().add(label);

            TextField textField = new TextField();
            attribute.getChildren().add(textField);

            settingsEntityVBox.getChildren().add(attribute);
        }
        return settingsEntityVBox;
    }


    private VBox getSettingsWorldVBox() {
        VBox settingsWorldVBox = new VBox();
        for (String key : SimulationConfiguration.DEFAULT_KEY_MAP_WORLD.keySet()) {
            if (key.equals("placedEntities"))
                continue;

            HBox attribute = new HBox();
            attribute.setPadding(new Insets(5, 0, 5, 0));

            Label label = new Label(key + ": ");
            label.setPadding(new Insets(0, 10, 0, 0));
            attribute.getChildren().add(label);

            TextField textField = new TextField();
            attribute.getChildren().add(textField);

            settingsWorldVBox.getChildren().add(attribute);
        }
        return settingsWorldVBox;
    }

    /**
     * Initializes the TreeView of the entities placed in the {@link AirportAgentSim simulation}.
     */
    private void initializeSimulationObjects() {
        // the root is set invisible.
        TreeItem<String> root = new TreeItem<String>("invisibleRootElement");

        PluginManager pm = aas.getPluginManager();
        Iterator<String> activePluginID = pm.getActivePluginIDs().iterator();
        while (activePluginID.hasNext()) {
            TreeItem<String> node = new TreeItem<>(pm.getActivePluginByID(activePluginID.next()).getPlugin().getClass().getSimpleName());
            root.getChildren().add(node);
        }

        // contents
        simulationTreeView.setRoot(root);
        // selection mode
        simulationTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // event listener
        simulationTreeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    // TODO what happens if a entity is selected.
//                    settingsVBox.getChildren().setAll(getSettings(true));
                });
    }

    private void initializeView() {
        Rectangle world = new Rectangle();
        world.setFill(Color.rgb(255, 255, 255));

        SimulationWorld simulationWorld = aas.getWorld();
        world.setHeight(simulationWorld.getHeight());
        world.setWidth(simulationWorld.getWidth());

        Entity[] entities = simulationWorld.getEntities().toArray(new Entity[0]);

        viewPane.getChildren().add(world);

        for (TreeItem<String> node : simulationTreeView.getRoot().getChildren()) {
            node.getChildren().clear();
        }

        for (Entity e : entities) {
            if (e instanceof MovingEntity) {
                Circle entity = new Circle(
                        e.getPosition().getX(),
                        e.getPosition().getY(),
                        e.getWidth(),
                        Color.rgb(20, 255, 20)
                );
//                TODO implementation of customized entities
//                TODO validate Style string
//                entity.setStyle("" +
//                        "-fx-fill: #404a54;" +
//                        "-fx-stroke: black;" +
//                        "-fx-stroke-width: 5;");
                viewPane.getChildren().add(entity);
            } else {
                Rectangle entity = new Rectangle(
                        e.getPosition().getX(),
                        e.getPosition().getY(),
                        Color.rgb(255, 20, 20)
                );

                entity.setOnMousePressed(t -> {
                    mainSceneX = t.getSceneX();
                    mainSceneY = t.getSceneY();

                    Rectangle r = (Rectangle) (t.getSource());
                    r.toFront();
                });

                entity.setOnMouseDragged(t -> {
                    double offsetX = t.getSceneX() - mainSceneX;
                    double offsetY = t.getSceneY() - mainSceneY;

                    Rectangle r = (Rectangle) (t.getSource());

                    r.setX(r.getX() + offsetX);
                    r.setY(r.getY() + offsetY);

                    mainSceneX = t.getSceneX();
                    mainSceneY = t.getSceneY();
                });

                entity.setOnMouseReleased(t -> {
                    sortView();
                });

                viewPane.getChildren().add(entity);
            }
            sortView();

            TreeItem<String> node = new TreeItem<>(e.getPlugin().getClass().getSimpleName());

            for (TreeItem<String> n : simulationTreeView.getRoot().getChildren()) {
                if (n.getValue().equals(node.getValue()))
                    n.getChildren().add(node);
            }
        }
    }

    private void sortView() {
        ObservableList<Node> workingList = FXCollections.observableArrayList(
                viewPane.getChildren()
        );

        Collections.sort(workingList, (o1, o2) -> {
            String class1 = o1.getClass().toString();
            String class2 = o2.getClass().toString();
            return class2.compareTo(class1);
        });

        viewPane.getChildren().setAll(workingList);
    }

    private void initializeLibrary() {
        TreeItem<String> root = new TreeItem("invisibleRootElement");

        Iterator<String> itr = aas.getPluginManager().getActivePluginIDs().iterator();
        while (itr.hasNext()) {
            TreeItem<String> node = new TreeItem<>(aas.getPluginManager().getActivePluginByID("base").getName());
            System.out.println(aas.getPluginManager().getActivePluginByID(itr.next()).getDependencies());
//            node.addEventHandler(MouseEvent.MOUSE_CLICKED, m -> {
//                Circle entity = new Circle();
//                entity.setCenterX(7);
//                entity.setCenterY(7);
//                entity.setRadius(7);
//                entity.setFill(Color.rgb(20, 255, 20));
//                viewPane.getChildren().add(entity);
//            });
            root.getChildren().add(node);
        }
        libraryTreeView.setRoot(root);
        libraryTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        libraryTreeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    viewPane.getChildren().add(new TextField(this.aas.getPluginManager().
                            getActivePluginByID((newValue).getValue()).getName()));
                });
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

    /**
     * This method is called from the UI start button.<br>
     * TODO It starts a simulation.
     */
    public void startSimulation() {
        // UI feedback for users so that they are informed that no more input is possible.
        mainSplitPlane.getChildrenUnmodifiable().get(0).setDisable(true);
        mainSplitPlane.getChildrenUnmodifiable().get(2).setDisable(true);
        mainSplitPlane.setDividerPosition(0, 0);
        mainSplitPlane.setDividerPosition(1, 100);

        feedbackLabel.setText("running");
        // TODO Start simulation with UI input.
    }

    /**
     * This method opens a file chooser TODO that loads either a CSV export file or a JSON configuration file.
     */
    public void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
    }

    /**
     * Set the currentState of the UI.
     *
     * @param state The state it's set to.
     */
    private void setState(State state) {
        this.currentState = state;
    }
}