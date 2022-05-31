package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui;

import java.io.IOException;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimulationUI extends Application {
	
	private static final String FXML_MAIN_STAGE = "/dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui/MainStage.fxml";
	
	private static volatile AirportAgentSim aas = null;
	
	public static Thread showGUI(AirportAgentSim aas) {
		synchronized(SimulationUI.class) {
			if(SimulationUI.aas != null)
				throw new IllegalStateException();
			SimulationUI.aas = aas;
			Thread uiThread = new Thread(() -> launch(SimulationUI.class), "Airport Agent Simulation UI Thread");
			uiThread.start();
			try {
				SimulationUI.class.wait();
			} catch(InterruptedException e) {}
			return uiThread;
		}
	}
	
	@Override
	public void start(Stage stage) throws IOException {
		synchronized(SimulationUI.class) {
			
			AirportAgentSim aas = SimulationUI.aas;
			SimulationUI.aas = null;
			if(aas == null)
				throw new IllegalStateException();
			SimulationUI.class.notify();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_MAIN_STAGE));
			Parent parent = loader.load();
			
			Object c = loader.getController();
			if(!(c instanceof UIController))
				throw new IllegalArgumentException(c.getClass().getName());
			UIController uiController = (UIController) c;
			uiController.initializeAAS(aas);
			
			Scene scene = new Scene(parent);
			stage.setTitle("Airportagentsimulation");
			stage.setScene(scene);
			
			stage.show();
			
		}
	}
	
}
