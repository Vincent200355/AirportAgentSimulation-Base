package aas.model;

import aas.model.util.Point;

public class AgentFootprint {
	
	private Integer id;
	private AgentRole role;
	private String type;
	private String name;
	private Point position;
	
	public AgentFootprint(Integer id, AgentRole role, String type, String name, Point position) {
		this.id = id;
		this.role = role;
		this.type = type;
		this.name = name;
		this.position = position;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public AgentRole getRole() {
		return this.role;
	}
	
	public void setRole(AgentRole role) {
		this.role = role;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Point getPosition() {
		return this.position;
	}
	
	public void setPosition(Point position) {
		this.position = position;
	}
	
	@Override
	public String toString() {
		return "[" + this.id + "] " + this.role + "." + this.type + "." + this.name;
	}
	
}
