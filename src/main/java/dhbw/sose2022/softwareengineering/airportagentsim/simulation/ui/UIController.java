package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.MovingEntity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.StaticEntity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.registry.ConfigurationTypeRegistry;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation.SimulationWorld;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states.PausedSimulation;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states.PreSimulation;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states.RunningSimulate;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states.State;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.update.GUIUpdater;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    private Slider speedSlider;
    @FXML
    private Label speedLabel;
    @FXML
    private TextField feedbackLabel;
    @FXML
    private Label iterationsLabel;

    private AirportAgentSim aas;
    private State currentState;
    private ArrayList<Entity> entityLibraryList = new ArrayList<>();
    private HashMap<String, Entity> placedEntities = new HashMap<>();
    private double mainSceneX;
    private double mainSceneY;

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

        aas.setSimulationPaused(true);
    }

    /**
     * Initializes the GUI based on the {@link AirportAgentSim simulation} data.
     */
    public void initializeGUI() {
        mainSplitPlane.setDividerPosition(1, 0.9);
        initializeSpeedSlider();
        setState(new PreSimulation(this.aas, this));
        initializeLibrary();
        initializeSimulationObjects();
        initializeView();
        updateUI();
    }

    private void initializeSpeedSlider() {
        speedSlider.setMajorTickUnit(10);
        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                speedSlider.setMajorTickUnit(10);
                speedSlider.snapToTicksProperty();
                double speed = 0;
                int value = (int) speedSlider.getValue();
                switch (value / 10) {
                    case 0:
                        speed = ((double) value / 10) * 0.25;
                        break;
                    case 1:
                        speed = 0.25 + ((double) (value % 10) / 10) * 0.25;
                        break;
                    case 2:
                        speed = 0.5 + ((double) (value % 10) / 10) * 0.5;
                        break;
                    case 3:
                        speed = 1 + ((double) (value % 10) / 10) * 0.5;
                        break;
                    case 4:
                        speed = 1.5 + ((double) (value % 10) / 10) * 0.5;
                        break;
                    case 5:
                        speed = 2 + ((double) (value % 10) / 10) * 3;
                        break;
                    case 6:
                        speed = 5 + ((double) (value % 10) / 10) * 5;
                        break;
                    case 7:
                        speed = 10 + ((double) (value % 10) / 10) * 40;
                        break;
                    case 8:
                        speed = 50 + ((double) (value % 10) / 10) * 50;
                        break;
                    case 9:
                        speed = 100 + ((double) (value % 10) / 10) * 100;
                        break;
                    case 10:
                        speed = 200;
                        break;
                    default:
                        speed = 0;
                }
                speedLabel.setText("x" + speed);
                if (speed == 0) {
                    aas.setSimulationPaused(true);
                } else {
                    aas.setSimulationCycleDuration(Math.round(17 / speed));
                }
            }
        });
        speedSlider.setValue(0);
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

                        int stokeWidth = Math.min(placedEntities.get(newValue.getValue()).getWidth(), placedEntities.get(newValue.getValue()).getHeight());
                        stokeWidth /= 5;
                        stokeWidth = Math.max(stokeWidth, 4);

                        shape.setStyle("-fx-stroke: black; -fx-stroke-width:" + stokeWidth + ";");
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

    public void updateUI() {
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

                node.setOnMousePressed(t -> {
                    mainSceneX = t.getSceneX();
                    mainSceneY = t.getSceneY();

                    Ellipse e = (Ellipse) t.getSource();
                    e.toFront();
                });

                node.setOnMouseDragged(t -> {
                    double offsetX = (t.getSceneX() - mainSceneX) / viewPane.getScaleX();
                    double offsetY = (t.getSceneY() - mainSceneY) / viewPane.getScaleX();

                    Ellipse e = (Ellipse) t.getSource();

                    e.setCenterX(e.getCenterX() + offsetX);
                    e.setCenterY(e.getCenterY() + offsetY);

                    mainSceneX = t.getSceneX();
                    mainSceneY = t.getSceneY();
                });

                node.setOnMouseReleased(t -> {
                    double offsetX = (t.getSceneX() - mainSceneX) / viewPane.getScaleX();
                    double offsetY = (t.getSceneY() - mainSceneY) / viewPane.getScaleX();

                    currentState.setPosition(
                            placedEntities.get(((Ellipse) t.getSource()).getId()),
                            (int) Math.round(((Ellipse) t.getSource()).getCenterX() + offsetX),
                            (int) Math.round(((Ellipse) t.getSource()).getCenterY() + offsetY));
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
                node.setOnMouseClicked(mouseEvent -> {
                    findNode(getEntityID(entity), simulationTreeView.getRoot());
                    simulationTreeView.getSelectionModel().select((TreeItem<String>) findNode(getEntityID(entity), simulationTreeView.getRoot()));
                });

                node.setOnMousePressed(t -> {
                    mainSceneX = t.getSceneX();
                    mainSceneY = t.getSceneY();

                    Ellipse e = (Ellipse) t.getSource();
                    e.toFront();
                });

                node.setOnMouseDragged(t -> {
                    double offsetX = (t.getSceneX() - mainSceneX) / viewPane.getScaleX();
                    double offsetY = (t.getSceneY() - mainSceneY) / viewPane.getScaleX();

                    Ellipse e = (Ellipse) t.getSource();

                    e.setCenterX(e.getCenterX() + offsetX);
                    e.setCenterY(e.getCenterY() + offsetY);

                    mainSceneX = t.getSceneX();
                    mainSceneY = t.getSceneY();
                });

                node.setOnMouseReleased(t -> {
                    double offsetX = (t.getSceneX() - mainSceneX) / viewPane.getScaleX();
                    double offsetY = (t.getSceneY() - mainSceneY) / viewPane.getScaleX();

                    currentState.setPosition(
                            placedEntities.get(((Ellipse) t.getSource()).getId()),
                            (int) Math.round(((Ellipse) t.getSource()).getCenterX() + offsetX),
                            (int) Math.round(((Ellipse) t.getSource()).getCenterY() + offsetY));
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

            // Unmovable entities don't have to be updated
            if (node != null && entity instanceof MovingEntity) {
                ((Ellipse) node).setCenterX(entity.getPosition().getX());
                ((Ellipse) node).setCenterY(entity.getPosition().getY());
                ((Ellipse) node).setRadiusX(entity.getWidth());
                ((Ellipse) node).setRadiusY(entity.getHeight());

            }
            if (node != null && entity instanceof StaticEntity) {
                ((Rectangle) node).setX(entity.getPosition().getX());
                ((Rectangle) node).setY(entity.getPosition().getY());
                ((Rectangle) node).setWidth(entity.getWidth());
                ((Rectangle) node).setHeight(entity.getHeight());
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
        setState(new PausedSimulation(this.aas, this));

        mainSplitPlane.getChildrenUnmodifiable().get(0).setDisable(false);
        mainSplitPlane.getChildrenUnmodifiable().get(2).setDisable(false);

        aas.setSimulationPaused(true);
    }

    /**
     * This method is called from the UI start button.<br>
     * TODO It starts a simulation.
     */
    public void startSimulation() {
        // UI feedback for users so that they are informed that no more input is possible.
        Tooltip t = new Tooltip(
                "Customizations are currently not available \n" +
                        "as the simulation is in progress.\n" +
                        "Pause the simulation to adjust it.");
        t.setShowDelay(Duration.millis(10));

        mainSplitPlane.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {

            var node = mainSplitPlane.getChildrenUnmodifiable().stream()
                    .filter(Node::isDisabled)
                    .filter(n -> n.contains(n.parentToLocal(e.getX(), e.getY()))).findFirst();

            if (node.isPresent()) {
                mainSplitPlane.setTooltip(t);
            } else {
                mainSplitPlane.setTooltip(null);
            }
        });

        mainSplitPlane.getChildrenUnmodifiable().get(0).setDisable(true);
        mainSplitPlane.getChildrenUnmodifiable().get(2).setDisable(true);

        settingsAnchorPane.getChildren().clear();
        mainSplitPlane.setDividerPosition(0, 0);
        mainSplitPlane.setDividerPosition(1, 100);

        setState(new RunningSimulate(this.aas, this));
        simulationTreeView.getSelectionModel().clearSelection();

        aas.setSimulationPaused(false);
        if (speedSlider.getValue() == 0)
            speedSlider.setValue(30);
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
                }

                // If the current node has children then check them.
                if (!node.isLeaf()) {
                    TreeItem<?> result = findNode(value, node);
                    if (result != null)
                        return result;
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
    private void removeEntryFromTreeItem(TreeItem<?> treeItem, Object entry) {
        TreeItem<?> node = findNode(entry, treeItem);
        if (node != null) {
            node.getParent().getChildren().remove(node);
            if (node.getParent().getChildren().isEmpty() && node.getParent() != treeItem)
                removeEntryFromTreeItem(treeItem, node.getParent().getValue());
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