package aas.controller;

import aas.model.util.Point;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Element;
import java.util.HashMap;

public class AgentPattern {
	
	private String agentClass;
	private String agentName;
	private Integer startX;
	private Integer startY;
	private HashMap<String, String> parameters;
	
	public AgentPattern(@Element(name = "class") String agentClass, @Element(name = "name") String agentName, @Element(name = "x") Integer startX, @Element(name = "y") Integer startY, @ElementMap(entry = "parameter", key = "key", attribute = true, inline = true, required = false) HashMap<String, String> parameters) {
		this.parameters = new HashMap<String, String>();
		setAgentClass(agentClass);
		setAgentName(agentName);
		setStartX(startX);
		setStartY(startY);
		setParameters(parameters);
	}
	
	@Element(name = "class")
	public String getAgentClass() {
		return this.agentClass;
	}
	
	protected void setAgentClass(String agentClass) {
		this.agentClass = agentClass.toLowerCase();
	}
	
	@Element(name = "name")
	public String getAgentName() {
		return this.agentName;
	}
	
	protected void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	@Element(name = "x")
	public Integer getStartX() {
		return this.startX;
	}
	
	protected void setStartX(Integer startX) {
		this.startX = startX;
	}
	
	@Element(name = "y")
	public Integer getStartY() {
		return this.startY;
	}
	
	protected void setStartY(Integer startY) {
		this.startY = startY;
	}
	
	@ElementMap(entry = "parameter", key = "key", attribute = true, inline = true, required = false)
	protected HashMap<String, String> getParameters() {
		return this.parameters;
	}
	
	public String getParameter(String key) {
		if(!this.parameters.containsKey(key))
			throw new IllegalArgumentException("Key " + key + " is not defined for " + this.agentName);
		return this.parameters.get(key);
	}
	
	public boolean hasParameter(String key) {
		return this.parameters.keySet().contains(key);
	}
	
	protected void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}
	
	public Point getStart() {
		return new Point(this.startX, this.startY);
	}
	
}
