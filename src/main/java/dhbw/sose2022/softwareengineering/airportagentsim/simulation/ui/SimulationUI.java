package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;


public class SimulationUI extends Application {
   public static void main(String[] args) {
      launch();
   }

   @Override
   public void start(Stage stage) throws IOException {
//	  Color white = Color.rgb(255,255,255);
//	  Color lightGray = Color.rgb(210,210,210);
//	  Color black = Color.rgb(0,0,0);
//	  Color green = Color.rgb(0,255,0);
//
//      //Drawing a Rectangle
//      Rectangle world = new Rectangle();
//
//      //Setting the properties of the rectangle
//      world.setX(0f);
//      world.setY(0f);
//      world.setWidth(600f);
//      world.setHeight(300f);
//      world.setFill(lightGray);
//
//      // creating all the other objects
//      Rectangle object = new Rectangle();
//      object.setX(10f);
//      object.setY(10f);
//      object.setWidth(10f);
//      object.setHeight(30f);
//      object.setFill(green);
//      object.setRotate(0f);
//
//      //Creating a AnchorPane
//      AnchorPane anchorPane = new AnchorPane();
//
//      //Retrieving the observable list of the Stack Pane
//      ObservableList list = anchorPane.getChildren();
//
//      //Adding all the nodes to the pane
//      list.addAll(world, object);
//
//      //Creating a scene object
//      Scene scene = new Scene((Parent) menuBar);
//
//      //Setting title to the Stage
//      stage.setTitle("Simulation");
//
//      //Adding scene to the stage
//      stage.setScene(scene);
//
//      //Displaying the contents of the stage
//      stage.show();

      URL url = new File("src/main/resources/dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui/MainStage.fxml").toURI().toURL();
      FXMLLoader loader = new FXMLLoader(url);
      Scene scene = new Scene(loader.load());

      loader.getController().getClass();
      stage.setTitle("Airportagentsimulation");
      stage.setScene(scene);
      stage.show();
   }
}