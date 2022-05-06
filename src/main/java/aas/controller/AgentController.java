package aas.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

import aas.controller.export.Export;
import aas.controller.logger.LoggerController;
import aas.controller.logger.LoggerType;
import aas.model.Agent;
import aas.model.AgentFootprint;
import aas.model.communication.Message;
import aas.model.util.Point;

@Root
public class AgentController {
	
	private static final Logger EVENT_LOGGER = Logger.getLogger(LoggerType.EVENT + ".controller");
	private static final Logger LOGGER = Logger.getLogger(AgentController.class.getName());
	
	
	private AgentPattern[] patterns;
	private HashMap<Integer, Agent> agents;
	private Vector<Export> exports;
	private CommunicationController comController;
	private LoggerController loggerController;
	private Double neighbourhoodDistance;
	private Integer simulationCycles;
	
	public AgentController(@ElementArray(name = "agents") AgentPattern[] patterns) {
		this.agents = new HashMap<Integer, Agent>();
		this.exports = new Vector<Export>();
		this.comController = new CommunicationController(this);
		this.loggerController = new LoggerController();
		this.neighbourhoodDistance = 13.0;
		this.simulationCycles = 100;
		setAgents(patterns);
	}
	
	public AgentController(@ElementArray(name = "agents") AgentPattern[] patterns, @Element(name = "neighbourhood-distance", required = false) double neighbourhood, @Element(name = "simulation-cycles", required = false) int simSteps) {
		this.agents = new HashMap<Integer, Agent>();
		this.exports = new Vector<Export>();
		this.comController = new CommunicationController(this);
		this.loggerController = new LoggerController();
		this.neighbourhoodDistance = 13.0;
		this.simulationCycles = 100;
		setAgents(patterns);
		setNeighbourhoodDistance(neighbourhood);
		setSimulationCycles(simSteps);
	}
	
	public AgentController() {
		this.agents = new HashMap<Integer, Agent>();
		this.exports = new Vector<Export>();
		this.comController = new CommunicationController(this);
		this.loggerController = new LoggerController();
		this.neighbourhoodDistance = 13.0;
		this.simulationCycles = 100;
	}
	
	@ElementArray(name = "agents")
	protected AgentPattern[] getAgents() {
		return this.patterns;
	}
	
	public void add(Agent agent) {
		if(agent == null)
			throw new IllegalArgumentException("Unable to add null agent");
		if(this.agents.containsKey(agent.getFootprint().getId()))
			throw new IllegalArgumentException("Unable to add agent with " + agent.getFootprint().getId() + " - Id already exists.");
		this.agents.put(agent.getFootprint().getId(), agent);
	}
	
	public void setExports(Export[] newExports) {
		this.exports.clear();
		this.exports.addAll(Arrays.asList(newExports));
	}
	
	protected Export[] getExports() {
		return this.exports.toArray(new Export[this.exports.size()]);
	}
	
	private void setAgents(AgentPattern[] newAgents) {
		this.patterns = newAgents;
		this.agents.clear();
		AgentFactory factory = new AgentFactory();
		factory.builtAgents(newAgents);
		Agent[] agents;
		for(int length = (agents = factory.getAgents()).length, i = 0; i < length; ++i) {
			Agent agent = agents[i];
			add(agent);
		}
	}
	
	private void removeAllAgents(List<Agent> agents) {
		for(Agent agent : agents)
			this.agents.remove(agent.getFootprint().getId());
	}
	
	public void remove(Agent agent) {
		if(agent == null || !this.agents.containsKey(agent.getFootprint().getId()))
			throw new IllegalArgumentException("Unable to remove agent");
		this.agents.remove(agent);
	}
	
	public void run() {
		for(int index = 0; index < this.simulationCycles; ++index) {
			AgentController.EVENT_LOGGER.info("=== Simulation Step " + index + " ===");
			Vector<Agent> finishedAgents = new Vector<Agent>();
			for(Agent agent : this.agents.values()) {
				int agentId = agent.getFootprint().getId();
				Message[] incomingMessages = this.comController.getOutgoing(agentId);
				AgentFootprint[] neighbours = getNeighbours(agent);
				Message[] outgoingMessages = agent.simulate(index, incomingMessages, neighbours);
				this.comController.addMessages(outgoingMessages);
				if(agent.isDone()) {
					AgentController.EVENT_LOGGER.info(String.valueOf(index) + " - controller: Removing agent " + agent.getFootprint());
					finishedAgents.add(agent);
				}
				notifyExports(index, agent, outgoingMessages);
			}
			this.comController.transfer(index);
			removeAllAgents(finishedAgents);
			finishedAgents.clear();
			AgentController.EVENT_LOGGER.info(String.valueOf(index) + " - controller: " + this.agents.size() + " agents in simulation.");
		}
		finishExports();
	}
	
	private void notifyExports(int time, Agent agent, Message[] message) {
		for(Export export : this.exports)
			export.notify(time, agent, message);
	}
	
	private void finishExports() {
		for(Export export : this.exports)
			export.finish();
	}
	
	public AgentFootprint[] getFootprints(Point source, double distance) {
		Vector<AgentFootprint> footprints = new Vector<AgentFootprint>();
		for(Agent agent : this.agents.values())
			if(agent.getFootprint().getPosition().getDistance(source) <= distance)
				footprints.add(agent.getFootprint());
		return footprints.toArray(new AgentFootprint[footprints.size()]);
	}
	
	public AgentFootprint getAgent(int id) {
		return this.agents.get(id).getFootprint();
	}
	
	private AgentFootprint[] getNeighbours(Agent agent) {
		Vector<AgentFootprint> neighbours = new Vector<AgentFootprint>();
		for(Agent possibleNeighbour : this.agents.values()) {
			if(possibleNeighbour.getFootprint().getId() == agent.getFootprint().getId())
				continue;
			Point p1 = agent.getFootprint().getPosition();
			Point p2 = possibleNeighbour.getFootprint().getPosition();
			if(p1.getDistance(p2) >= this.neighbourhoodDistance)
				continue;
			neighbours.add(possibleNeighbour.getFootprint());
		}
		return neighbours.toArray(new AgentFootprint[neighbours.size()]);
	}
	
	@Element(name = "neighbourhood-distance", required = false)
	public double getNeighbourhoodDistance() {
		return this.neighbourhoodDistance;
	}
	
	public void setNeighbourhoodDistance(Double neighbourhoodDistance) {
		this.neighbourhoodDistance = neighbourhoodDistance;
	}
	
	@Element(name = "simulation-cycles", required = false)
	public Integer getSimulationCycles() {
		return this.simulationCycles;
	}
	
	public void setSimulationCycles(Integer simulationCycles) {
		this.simulationCycles = simulationCycles;
	}
	
}
