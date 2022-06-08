package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.MovingEntity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.StaticEntity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.registry.ConfigurationTypeRegistry;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation.SimulationWorld;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states.PreSimulation;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states.RunningSimulate;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states.State;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.update.GUIUpdater;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class UIController {
    @FXML
    private SplitPane mainSplitPlane;

    @FXML
    private TreeView<String> libraryTreeView;
    @FXML
    private TreeView<String> simulationTreeView;

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
        mainSplitPlane.setDividerPosition(1, 0.9);
        setState(new PreSimulation(this.aas));
        initializeLibrary();
        initializeSimulationObjects();
        initializeView();
    }

    private void initializeLibrary() {
        TreeItem<String> root = new TreeItem<>("invisibleRootElement");

        ConfigurationTypeRegistry ctr = aas.getConfigurationTypeRegistry();
        for (String entityID : ctr.getEntitiesByID().keySet()) {
            TreeItem<String> node = new TreeItem<>(entityID);
            // TODO get style from plugin
            node.setGraphic(new Circle(6, generateColor(aas.getConfigurationTypeRegistry()
                    .getEntitiesByID().get(entityID).hashCode())));

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
        TreeItem<String> root = new TreeItem<>("invisibleRootElement");

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
                        shape.setStyle("-fx-stroke: black; -fx-stroke-width:" + (placedEntities.get(newValue.getValue()).getWidth() + placedEntities.get(newValue.getValue()).getWidth()) / 4 + ";");
                        settingsAnchorPane.getChildren().add(currentState.configureEntity(placedEntities.get(newValue.getValue())));
                    }
                });
    }

    private void initializeView() {
        SimulationWorld simulationWorld = aas.getWorld();

        this.viewPane = new AnchorPane();

        double scale = ((double) 1000 / simulationWorld.getWidth()) * 0.6;
        ZoomableScrollPane viewScrollPane = new ZoomableScrollPane(viewPane, scale);
        mainSplitPlane.getItems().add(1, viewScrollPane);

        Rectangle world = new Rectangle();
        world.setFill(Color.rgb(255, 255, 255));
        world.setStroke(Color.BLACK);
        world.setStrokeWidth(5);

        world.setHeight(simulationWorld.getHeight());
        world.setWidth(simulationWorld.getWidth());
        viewPane.getChildren().add(world);
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
            String id = getEntityID(entity);
            Node node = viewPane.lookup("#" + id);

            if (node == null && entity instanceof MovingEntity) {
                node = new Ellipse();
                ((Ellipse) node).setCenterX(entity.getPosition().getX());
                ((Ellipse) node).setCenterY(entity.getPosition().getY());
                ((Ellipse) node).setRadiusX(entity.getWidth() * 0.5);
                ((Ellipse) node).setRadiusY(entity.getHeight() * 0.5);
                ((Ellipse) node).setFill(generateColor(entity.getClass().hashCode()));
                // TODO mark selected item in the simulationTreeView
                node.setOnMouseClicked(mouseEvent -> {
                    findNode(getEntityID(entity), simulationTreeView.getRoot());
                    simulationTreeView.getSelectionModel().select((TreeItem<String>) findNode(getEntityID(entity), simulationTreeView.getRoot()));
                });
//                TODO implementation of customized entities
//                TODO validate Style string
                node.setCursor(Cursor.HAND);
                node.setId(id);
                Tooltip t = new Tooltip(entity.getClass().getSimpleName() + " #" + entity.getUID());
                Tooltip.install(node, t);
                t.setShowDelay(Duration.millis(10));
                viewPane.getChildren().add(node);
            }

            if (node == null && entity instanceof StaticEntity) {
                node = new Rectangle(
                        entity.getPosition().getX(),
                        entity.getPosition().getY(),
                        entity.getWidth(),
                        entity.getHeight()
                );
                ((Rectangle) node).setFill(generateColor(entity.getClass().hashCode()));
                // TODO mark selected item in the simulationTreeView
                node.setOnMouseClicked(mouseEvent ->
                        simulationTreeView.getSelectionModel().select(new TreeItem<>(getEntityID(entity))));
//                TODO implementation of customized entities
//                TODO validate Style string
                node.setCursor(Cursor.HAND);
                node.setId(id);
                Tooltip t = new Tooltip(entity.getClass().getSimpleName() + " #" + entity.getUID());
                Tooltip.install(node, t);
                t.setShowDelay(Duration.millis(10));
                viewPane.getChildren().add(node);
            }

            // Unmovable entities don't have to be updated
            if (node != null && entity instanceof MovingEntity) {
                ((Ellipse) node).setCenterX(entity.getPosition().getX());
                ((Ellipse) node).setCenterY(entity.getPosition().getY());
            }
        }

        for (String id : getDeletedEntitiesID()) {
            viewPane.getChildren().remove(viewPane.lookup("#" + id));
        }
    }

    private void updateSimulationObjects() {
        for (Entity entity : getAddedEntities()) {
            String className = entity.getClass().getSimpleName();
            String id = getEntityID(entity);

            TreeItem<String> ti = (TreeItem<String>) findNode(className, simulationTreeView.getRoot());
            if (ti == null) {
                ti = new TreeItem<>(className);
                simulationTreeView.getRoot().getChildren().add(ti);
            }
            TreeItem<String> entityTreeItem = new TreeItem<>(id);
            ti.getChildren().add(entityTreeItem);
        }
        for (String entityID : getDeletedEntitiesID())
            removeEntryFromTreeItem(simulationTreeView.getRoot(), entityID);
    }

    public void stopSimulation() {
        throw new UnsupportedOperationException();
    }

    public void pauseSimulation() {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is called from the UI start button.<br>
     * TODO It starts a simulation.
     */
    public void startSimulation() {
        // UI feedback for users so that they are informed that no more input is possible.
        // TODO tooltip
        mainSplitPlane.getChildrenUnmodifiable().get(0).setDisable(true);
        // TODO tooltip
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
        ArrayList<String> deletedEntities = new ArrayList<>(placedEntities.keySet());

        for (Entity e : aas.getWorld().getEntities()) {
            String id = getEntityID(e);
            if (placedEntities.containsKey(id))
                deletedEntities.remove(id);
        }
        return deletedEntities;
    }

    private ArrayList<Entity> getAddedEntities() {
        ArrayList<Entity> addedEntities = new ArrayList<>();

        for (Entity e : aas.getWorld().getEntities()) {
            String id = getEntityID(e);
            if (!placedEntities.containsKey(id))
                addedEntities.add(e);
        }
        return addedEntities;
    }

    private void updatePlacedEntities() {
        for (String string : getDeletedEntitiesID())
            this.placedEntities.remove(string);
        for (Entity entity : getAddedEntities())
            this.placedEntities.put(getEntityID(entity), entity);
    }

    /**
     * Finds a TreeItem in a TreeItem using its value.
     *
     * @param value
     * @param root
     * @return a TreeItem
     */
    private TreeItem<?> findNode(Object value, TreeItem<?> root) {

        if (root.getChildren().isEmpty()) {
            return null;
        } else {

            // Loop through each child node.
            for (TreeItem<?> node : root.getChildren()) {

                if (node.getValue().equals(value)) {
                    node.setExpanded(true);
                    return node;
                } else {// If the current node has children then check them.
                    if (!node.isLeaf()) {
                        return findNode(value, node);
                    }
                }
            }
        }
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


    /**
     * Generates a color from a seed.
     *
     * @param seed The seed from which the color is generated.
     * @return a Color
     */
    private Color generateColor(int seed) {
        Random random = new Random(seed);
        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue = random.nextInt(255);
        return Color.rgb(red, green, blue);
    }

    private String getEntityID(Entity entity) {
        return "#" + entity.getUID();
    }
}