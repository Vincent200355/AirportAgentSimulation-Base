package aas;

import aas.controller.AgentController;
import aas.controller.export.CsvExport;
import aas.controller.export.Export;
import aas.controller.export.GeoJsonExport;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AirportAgentSimulation {
	
	private static final Logger LOGGER = Logger.getLogger(AirportAgentSimulation.class.getName());
	
	private static AgentController controller;
	
	public static void main(String[] args) {
		if(args.length > 0 && new File(args[0]).exists()) {
			loadProperties(new File(args[0]));
		} else {
			String fileName = getFile();
			loadProperties(new File(fileName));
		}
		if(AirportAgentSimulation.controller == null) {
			AirportAgentSimulation.LOGGER.severe("Controller could not be initialized");
			System.exit(-1);
		}
		Export[] exports = {
			new GeoJsonExport("position.geojson"),
			new CsvExport("position.csv", "communication.csv")
		};
		AirportAgentSimulation.controller.setExports(exports);
		AirportAgentSimulation.controller.run();
	}
	
	private static void loadProperties(File file) {
		Serializer serializer = new Persister();
		try {
			AirportAgentSimulation.controller = serializer.read((Class<? extends AgentController>) AgentController.class, file);
		} catch(Exception e) {
			AirportAgentSimulation.LOGGER.log(Level.SEVERE, "Config file could not be parsed", e);
			System.exit(-1);
		}
	}
	
	private static String getFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("./"));
		chooser.setFileFilter(new XMLFileFilter());
		int result = chooser.showOpenDialog(null);
		if(result == 1)
			System.exit(0);
		return chooser.getSelectedFile().getAbsolutePath();
	}

	private static class XMLFileFilter extends FileFilter {
		
		@Override
		public boolean accept(File f) {
			return f.isDirectory() || f.getAbsolutePath().endsWith(".xml");
		}
		
		@Override
		public String getDescription() {
			return "XML-Files";
		}
		
	}
	
}
