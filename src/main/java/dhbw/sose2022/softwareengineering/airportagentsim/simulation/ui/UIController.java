package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurationFormatException;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurationParseException;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.MovingEntity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.StaticEntity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.EntityConfiguration;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.SimulationConfiguration;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.registry.ConfigurationTypeRegistry;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation.SimulationWorld;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states.PreSimulation;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states.RunningSimulate;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states.State;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.update.GUIUpdater;
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
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
    private AnchorPane settingsAnchorPane;

    @FXML
    private TextField feedbackLabel;
    @FXML
    private Label iterationsLabel;

    private AirportAgentSim aas;
    private State currentState;
    private ArrayList<Entity> entityLibraryList = new ArrayList<>();
    private ArrayList<String> simulationEntityPlugins = new ArrayList<>();
    private HashMap<String, Entity> placedEntities = new HashMap<>();

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
        this.aas.setGUIUpdater(new GUIUpdater(this::updateUI));
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

    private void initializeLibrary() {
        TreeItem<String> root = new TreeItem("invisibleRootElement");

        ConfigurationTypeRegistry ctr = aas.getConfigurationTypeRegistry();
        Iterator<String> itr = ctr.getEntitiesByID().keySet().iterator();
        while (itr.hasNext()) {
            String entityID = itr.next();
            TreeItem<String> node = new TreeItem<String>(entityID);
            // TODO get style from plugin
            node.setGraphic(new Circle(6, Color.rgb(20, 255, 20)));

            try {
                Gson gson = new Gson();
                JsonObject object = new JsonObject();
                // TODO get default values from Plugin
                object = gson.fromJson("{\"initial-facing\":{\"x\":80,\"y\":80},\"initial-speed\":1.5,\"silent\":true,\"message\":\"Agent 2: (Position %p)\"}"
                        , JsonObject.class);
                this.entityLibraryList.add(ctr.parseEntity(entityID, object));
            } catch (ConfigurationFormatException e) {
                e.printStackTrace();
            } catch (ConfigurationParseException e) {
                e.printStackTrace();
            }
            root.getChildren().add(node);
        }
        libraryTreeView.setRoot(root);
        libraryTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        libraryTreeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    int index = libraryTreeView.getRoot().getChildren().indexOf(newValue);
                    currentState.addEntity((Entity) entityLibraryList.toArray()[index]);
                    updateUI();
                });
    }

    /**
     * Initializes the TreeView of the entities placed in the {@link AirportAgentSim simulation}.
     */
    private void initializeSimulationObjects() {
        // the root is set invisible.
        TreeItem<String> root = new TreeItem<String>("invisibleRootElement");

        // contents
        simulationTreeView.setRoot(root);
        // selection mode
        simulationTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // event listener
        simulationTreeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (oldValue != null && oldValue.isLeaf()) {
                        Shape shape = (Shape) viewPane.lookup("#" + oldValue.getValue());
                        shape.setStyle("");
                        settingsAnchorPane.getChildren().clear();
                    }

                    if (newValue != null && newValue.isLeaf()) {
                        Shape shape = (Shape) viewPane.lookup("#" + newValue.getValue());
                        shape.setStyle("-fx-stroke: black; -fx-stroke-width: 5;");
                        settingsAnchorPane.getChildren().add(currentState.configureEntity(placedEntities.get(newValue.getValue())));
                    }
                });
    }

    private void initializeView() {
        Rectangle world = new Rectangle();
        world.setFill(Color.rgb(255, 255, 255));

        SimulationWorld simulationWorld = aas.getWorld();
        world.setHeight(simulationWorld.getHeight());
        world.setWidth(simulationWorld.getWidth());

        viewPane.getChildren().add(world);

        for (Entity entity : aas.getWorld().getEntities()) {
            String id = Integer.toHexString(System.identityHashCode(entity));
            Node node = viewPane.lookup("#" + id);

            if (node == null && !(entity instanceof MovingEntity)) {
                node = new Rectangle(
                        entity.getPosition().getX(),
                        entity.getPosition().getY(),
                        entity.getWidth(),
                        entity.getHeight()
                );
                ((Rectangle) node).setFill(Color.rgb(255, 20, 20));
/*                    n.setOnMousePressed(t -> {
                        mainSceneX = t.getSceneX();
                        mainSceneY = t.getSceneY();

                        Rectangle r = (Rectangle) (t.getSource());
                        r.toFront();
                    });

                    n.setOnMouseDragged(t -> {
                        double offsetX = t.getSceneX() - mainSceneX;
                        double offsetY = t.getSceneY() - mainSceneY;

                        Rectangle r = (Rectangle) (t.getSource());

                        r.setX(r.getX() + offsetX);
                        r.setY(r.getY() + offsetY);

                        mainSceneX = t.getSceneX();
                        mainSceneY = t.getSceneY();
                    });

                    n.setOnMouseReleased(t -> {
                        sortView();
                    });*/

                node.setId(id);
                viewPane.getChildren().add(node);
            }
        }
    }

    private void updateUI() {
        updateView();
        updateSimulationObjects();
        updatePlacedEntities();
        updateIterations();
    }

    private void updateIterations() {
        iterationsLabel.setText("Iterations: " + aas.getWorld().getIteration());
    }

    private void updateView() {
        for (Entity entity : aas.getWorld().getEntities()) {
            String id = Integer.toHexString(System.identityHashCode(entity));
            Node node = viewPane.lookup("#" + id);

            if (node == null && entity instanceof MovingEntity) {
                node = new Circle(
                        entity.getPosition().getX(),
                        entity.getPosition().getY(),
                        entity.getWidth(),
                        Color.rgb(20, 255, 20)
                );
//                TODO implementation of customized entities
//                TODO validate Style string
//                entity.setStyle("" +
//                        "-fx-fill: #404a54;" +
//                        "-fx-stroke: black;" +
//                        "-fx-stroke-width: 5;");
                node.setId(id);
                viewPane.getChildren().add(node);
            }

            if (node == null && entity instanceof StaticEntity) {
                node = new Rectangle(
                        entity.getPosition().getX(),
                        entity.getPosition().getY(),
                        entity.getWidth(),
                        entity.getHeight()
                );
                node.setStyle("-fx-fill: #404a54;");
//                TODO implementation of customized entities
//                TODO validate Style string
//                entity.setStyle("" +
//                        "-fx-fill: #404a54;" +
//                        "-fx-stroke: black;" +
//                        "-fx-stroke-width: 5;");
                node.setId(id);
                viewPane.getChildren().add(node);
            }

            if (node != null && entity instanceof MovingEntity) {
                ((Circle) node).setCenterX(entity.getPosition().getX());
                ((Circle) node).setCenterY(entity.getPosition().getY());
            }
        }
        // Unmovable entities don't have to be updated
        for (String id : getDeletedEntitiesID()) {
            viewPane.getChildren().remove(viewPane.lookup("#" + id));
        }
    }

    private void updateSimulationObjects() {
        for (Entity entity : getAddedEntities()) {
            String className = entity.getClass().getSimpleName();
            String id = Integer.toHexString(System.identityHashCode(entity));

            TreeItem ti = getTreeItemByValue(className, simulationTreeView.getRoot());
            if (ti == null) {
                ti = new TreeItem(className);
                simulationTreeView.getRoot().getChildren().add(ti);
            }
            TreeItem<String> entityTreeItem = new TreeItem<String>(id);
            ti.getChildren().add(entityTreeItem);
            // Todo select event
        }
        for (String entityID : getDeletedEntitiesID())
            removeEntryFromTreeItem(simulationTreeView.getRoot(), entityID);
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

    public void stopSimulation() {
        return;
    }

    public void pauseSimulation() {
        return;
    }

    /**
     * This method is called from the UI start button.<br>
     * TODO It starts a simulation.
     */
    public void startSimulation() {
        // UI feedback for users so that they are informed that no more input is possible.
        mainSplitPlane.getChildrenUnmodifiable().get(0).setDisable(true);
        mainSplitPlane.getChildrenUnmodifiable().get(2).setDisable(true);
        settingsAnchorPane.getChildren().clear();
        mainSplitPlane.setDividerPosition(0, 0);
        mainSplitPlane.setDividerPosition(1, 100);

        setState(new RunningSimulate(aas));
        simulationTreeView.getSelectionModel().clearSelection();

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
        feedbackLabel.setText(currentState.getName());
    }

    private ArrayList<String> getDeletedEntitiesID() {
        ArrayList<String> deletedEntities = new ArrayList<>();
        deletedEntities.addAll(placedEntities.keySet());

        for (Entity e : aas.getWorld().getEntities()) {
            String id = Integer.toHexString(System.identityHashCode(e));
            if (placedEntities.keySet().contains(id))
                deletedEntities.remove(id);
        }
        return deletedEntities;
    }

    private ArrayList<String> getAddedEntitiesID() {
        ArrayList<String> addedEntities = new ArrayList<>();

        for (Entity e : aas.getWorld().getEntities()) {
            String id = Integer.toHexString(System.identityHashCode(e));
            if (!placedEntities.keySet().contains(id))
                addedEntities.add(id);
        }
        return addedEntities;
    }

    private ArrayList<Entity> getAddedEntities() {
        ArrayList<Entity> addedEntities = new ArrayList<>();

        for (Entity e : aas.getWorld().getEntities()) {
            String id = Integer.toHexString(System.identityHashCode(e));
            if (!placedEntities.keySet().contains(id))
                addedEntities.add(e);
        }
        return addedEntities;
    }

    private void updatePlacedEntities() {
        for (String string : getDeletedEntitiesID())
            this.placedEntities.remove(string);
        for (Entity entity : getAddedEntities())
            this.placedEntities.put(Integer.toHexString(System.identityHashCode(entity)), entity);
    }

    private TreeItem<?> getTreeItemByValue(Object value, TreeItem<?> root) {
        for (TreeItem<?> ti : root.getChildren())
            if (ti.getValue() == value)
                return ti;

        return null;
    }

    /**
     * Deletes the first leaf with the entry you are looking for and all directories
     * that become empty as a result of the deletion.
     *
     * @param treeItem TreeItem from which the entry is to be deleted.
     * @param entry    The entry to be deleted.
     */
    private void removeEntryFromTreeItem(TreeItem<?> treeItem, String entry) {
        for (TreeItem<?> ti : treeItem.getChildren()) {
            // As long as you can go even deeper into the tree structure, this
            // method calls recursively.
            if (!ti.isLeaf())
                removeEntryFromTreeItem(ti, entry);

            // If the entry is the one you are looking for, it will be deleted.
            if (ti.getValue() == entry) {
                treeItem.getChildren().remove(ti);
                // If the directory is now empty, this will also be deleted.
                if (treeItem.getChildren().isEmpty())
                    treeItem.getParent().getChildren().remove(treeItem);
            }
        }
    }
}