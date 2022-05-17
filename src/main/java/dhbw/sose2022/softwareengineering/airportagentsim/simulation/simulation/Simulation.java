
public class Simulation {
    
    public Simulation () {
        SimulationConfiguration simulationConfig = getConfiguration();
        initializeWorld();
    }

    public SimulationConfiguration getConfiguration(String path = "") {
        return new SimulationConfiguration(path);
    }

    public void initializeWorld () {
        SimulationWorld world = new SimulationWorld(simulationConfig.width, simulationConfig.height);

        for (Map<String, Object> entityConfig : simulationConfig.entrySet()) {
            // create Plugin
            LoadedPlugin plugin = ...;
            // add Plugin to World
            world.add();       
        }
    }

    public void moveEntity (Entity e) {
        throw new UnsupportedOperationException(); 
    }

}