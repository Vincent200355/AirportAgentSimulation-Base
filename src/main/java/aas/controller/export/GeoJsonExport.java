package aas.controller.export;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import aas.model.Agent;
import aas.model.Moveable;
import aas.model.StaticObject;
import aas.model.communication.Message;
import aas.model.util.Point;

public class GeoJsonExport implements Export {
	
	private static final Logger LOGGER = Logger.getLogger(GeoJsonExport.class.getName());
	
	
	private String fileName;
	private Double referenceLatitude;
	private Double referenceLongitude;
	private HashMap<String, Vector<Point>> lines;
	private HashMap<String, Point[]> objects;
	
	public GeoJsonExport(String fileName) {
		this.referenceLatitude = 53.63099;
		this.referenceLongitude = 10.00564;
		this.lines = new HashMap<String, Vector<Point>>();
		this.objects = new HashMap<String, Point[]>();
		this.fileName = fileName;
	}
	
	public void addPostion(final String name, final long time, final Point point) {
		if(!this.lines.containsKey(name))
			this.lines.put(name, new Vector<Point>());
		this.lines.get(name).add(point);
	}
	
	@Override
	public void finish() {
		try {
			FileWriter writer = new FileWriter(this.fileName);
			writer.write("{ \"type\": \"FeatureCollection\",\n\"features\": [\n");
			String objectStrings = new String();
			for(String agentName : this.objects.keySet())
				objectStrings = String.valueOf(objectStrings) + getPolygon(agentName) + ",\n";
			objectStrings = objectStrings.substring(0, objectStrings.length() - 2);
			writer.write(String.valueOf(objectStrings) + ",");
			String lineStrings = new String();
			for(String agentName2 : this.lines.keySet())
				lineStrings = String.valueOf(lineStrings) + getLineString(agentName2) + ",\n";
			lineStrings = lineStrings.substring(0, lineStrings.length() - 2);
			writer.write(lineStrings);
			writer.write("]}");
			writer.flush();
			writer.close();
		} catch(IOException e) {
			GeoJsonExport.LOGGER.log(Level.SEVERE, "Could not open file", e);
		}
	}
	
	private String getLineString(final String agentName) {
		String line = new String();
		line = String.valueOf(line) + "{ \"type\": \"Feature\",\n\"geometry\": {\n" + "\"type\": \"LineString\",\n\"coordinates\": [";
		for(Point point : this.lines.get(agentName)) {
			double latitude = this.convertYToLatitude(point.getY());
			double longitude = this.convertXToLongitude(point.getX(), latitude);
			line = String.valueOf(line) + "[" + longitude + "," + latitude + "],";
		}
		line = line.substring(0, line.length() - 1);
		line = String.valueOf(line) + "]},\n\"properties\": {\"name\":\"" + agentName + "\"}}";
		return line;
	}
	
	private String getPolygon(final String agentName) {
		String line = new String();
		line += "{ \"type\": \"Feature\",\n\"geometry\": {\n" + "\"type\": \"Polygon\",\n\"coordinates\": [[";
		Point[] array;
		for(int length = (array = this.objects.get(agentName)).length, i = 0; i < length; ++i) {
			Point point = array[i];
			double latitude = this.convertYToLatitude(point.getY());
			double longitude = this.convertXToLongitude(point.getX(), latitude);
			line = String.valueOf(line) + "[" + longitude + "," + latitude + "],";
		}
		line = line.substring(0, line.length() - 1);
		line += "]]},\n\"properties\": {\"name\":\"" + agentName + "\"}}";
		return line;
	}
	
	private Double convertYToLatitude(double y) {
		return this.referenceLatitude + y / 111111.0;
	}
	
	private Double convertXToLongitude(double x, double latitude) {
		return this.referenceLongitude + x / 111111.0 * Math.cos(latitude);
	}
	
	@Override
	public void notify(int time, Agent agent, Message[] messages) {
		if(agent instanceof Moveable)
			addPostion(agent.getFootprint().getName(), time, agent.getFootprint().getPosition());
		if(agent instanceof StaticObject) {
			StaticObject objectAgent = (StaticObject) agent;
			if(!this.objects.containsKey(agent.getFootprint().getName())) {
				Point center = agent.getFootprint().getPosition();
				double width = objectAgent.getWidth();
				double length = objectAgent.getLength();
				Point p1 = new Point(center.getX() - width / 2.0, center.getY() - length / 2.0);
				Point p2 = new Point(center.getX() + width / 2.0, center.getY() - length / 2.0);
				Point p3 = new Point(center.getX() + width / 2.0, center.getY() + length / 2.0);
				Point p4 = new Point(center.getX() - width / 2.0, center.getY() + length / 2.0);
				this.objects.put(agent.getFootprint().getName(), new Point[] { p1, p4, p3, p2, p1 });
			}
		}
	}
	
}
