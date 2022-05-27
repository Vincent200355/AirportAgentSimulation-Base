package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui;

import javafx.geometry.Insets;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation.SimulationWorld;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group; 
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage; 
import javafx.scene.shape.Rectangle;

public class SimulationUI extends Application { 
   @Override 
   public void start(Stage stage) { 
	  Color white = Color.rgb(255,255,255);
	  Color lightGray = Color.rgb(210,210,210);
	  Color black = Color.rgb(0,0,0);
	  Color green = Color.rgb(0,255,0);
	
      //Drawing a Rectangle 
      Rectangle world = new Rectangle();  
      
      //Setting the properties of the rectangle 
      world.setX(0f); 
      world.setY(0f); 
      world.setWidth(600f); 
      world.setHeight(300f);    
      world.setFill(lightGray);
      
      // creating all the other objects
      Rectangle object = new Rectangle();
      object.setX(10f); 
      object.setY(10f); 
      object.setWidth(10f); 
      object.setHeight(30f);    
      object.setFill(green);
      object.setRotate(0f);
      
      //Creating a AnchorPane 
      AnchorPane anchorPane = new AnchorPane();
      
      //Retrieving the observable list of the Stack Pane 
      ObservableList list = anchorPane.getChildren(); 
      
      //Adding all the nodes to the pane 
      list.addAll(world, object); 
      
      //Creating a scene object 
      Scene scene = new Scene(anchorPane);  
      
      //Setting title to the Stage 
      stage.setTitle("Simulation"); 
         
      //Adding scene to the stage 
      stage.setScene(scene); 
         
      //Displaying the contents of the stage 
      stage.show(); 
   }      
   public static void main(String args[]){ 
      launch(args); 
   } 
} 