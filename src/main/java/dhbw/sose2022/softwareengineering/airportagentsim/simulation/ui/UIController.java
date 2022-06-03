package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.MovingEntity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.EntityConfiguration;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.SimulationConfiguration;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation.SimulationWorld;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.states.PreSimulation;
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
import java.util.*;

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
    private List<String> simulationEntityPlugins = new ArrayList<>();
    private List<String> placedEntities = new ArrayList<>();

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

        Iterator<String> itr = aas.getPluginManager().getActivePluginIDs().iterator();
        while (itr.hasNext()) {
            // Todo display EntityID instead.
            TreeItem<String> node = new TreeItem<>(aas.getPluginManager().getActivePluginByID(itr.next()).getName());
            // TODO this.entityLibrarySet.add();
            root.getChildren().add(node);
        }
        libraryTreeView.setRoot(root);
        libraryTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        libraryTreeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    int index = libraryTreeView.getRoot().getChildren().indexOf(newValue);
                    currentState.addEntity((Entity) entityLibrarySet.toArray()[index]);
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
                    }

                    if (newValue != null && newValue.isLeaf()) {

                        Shape shape = (Shape) viewPane.lookup("#" + newValue.getValue());
                        shape.setStyle("-fx-stroke: black; -fx-stroke-width: 5;");
                    }

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

        viewPane.getChildren().add(world);
        viewPane.setLayoutX(world.getLayoutX());
        viewPane.setLayoutY(world.getLayoutY());
        ;
        viewPane.setLayoutY(world.getLayoutY());

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

            if (node != null && entity instanceof MovingEntity) {
                ((Circle) node).setCenterX(entity.getPosition().getX());
                ((Circle) node).setCenterY(entity.getPosition().getY());
            }
        }
        // Unmovable entities don't have to be updated
        for (String id : getDeletedEntitiesID()) {
            viewPane.getChildren().remove("#" + viewPane.lookup(id));
        }
    }

    private void updateSimulationObjects() {
        for (Entity entity : getAddedEntities()) {
            String className = entity.getClass().getSimpleName();
            String id = Integer.toHexString(System.identityHashCode(entity));

            TreeItem ti = getTreeItemByValue(className, simulationTreeView.getRoot());
            System.out.println(ti);
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

    private ArrayList<String> getDeletedEntitiesID() {
        ArrayList<String> deletedEntities = new ArrayList<>();
        deletedEntities.addAll(placedEntities);

        for (Entity e : aas.getWorld().getEntities()) {
            String id = Integer.toHexString(System.identityHashCode(e));
            if (placedEntities.contains(id))
                deletedEntities.remove(id);
        }
        return deletedEntities;
    }

    private ArrayList<String> getAddedEntitiesID() {
        ArrayList<String> addedEntities = new ArrayList<>();

        for (Entity e : aas.getWorld().getEntities()) {
            String id = Integer.toHexString(System.identityHashCode(e));
            if (!placedEntities.contains(id))
                addedEntities.add(id);
        }
        return addedEntities;
    }

    private ArrayList<Entity> getAddedEntities() {
        ArrayList<Entity> addedEntities = new ArrayList<>();

        for (Entity e : aas.getWorld().getEntities()) {
            String id = Integer.toHexString(System.identityHashCode(e));
            if (!placedEntities.contains(id))
                addedEntities.add(e);
        }
        return addedEntities;
    }

    private void updatePlacedEntities() {
        this.placedEntities.remove(getDeletedEntitiesID());
        this.placedEntities.addAll(getAddedEntitiesID());
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
                if (treeItem.getChildren().size() == 0)
                    treeItem.getParent().getChildren().remove(treeItem);
            }
        }
    }
}