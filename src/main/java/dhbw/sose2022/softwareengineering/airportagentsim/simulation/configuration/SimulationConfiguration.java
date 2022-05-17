package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class SimulationConfiguration {
    private int seed, worldHeight, worldWidth;
    List<PlacedEntity> placedEntities;
    private List<GeneratedEntities> generatedEntities;
    private File configurationFile;
    private JSONObject config = new JSONObject();

    public SimulationConfiguration(File configurationFile) {
        JSONParser jasonParser = new JSONParser();

        this.configurationFile = configurationFile;
        // Create JSONObject from configurationFile
        try {
            config = (JSONObject) jasonParser.parse(new FileReader(configurationFile));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public SimulationConfiguration(String jsonString) throws ParseException {
        JSONParser jasonParser = new JSONParser();

        JSONObject jsonObject = new JSONObject((Map) jasonParser.parse(jsonString));
        this.config = jsonObject;

        this.configurationFile = new File("src/main/resources/configurationFile.jason");
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(configurationFile);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray placedEntitiesJASON = (JSONArray) config.get("placedEntities");


        placedEntitiesJASON.forEach(item -> {
            JSONObject entityJSON = (JSONObject) item;
            this.placedEntities.add(new PlacedEntity(entityJSON));
        });
    }

    public SimulationConfiguration(Path path) {
        // TODO configurationFile doesn't exist.
        this(new File(path.toString()));
    }

    /**
     * Returns the value of the searched dimension of the world as an integer.
     *
     * @param dimension the searched dimension
     * @return the size
     */
    public int getWorldDimension(String dimension) {
        // TODO value is not compatible to Integer.
        // TODO the requested dimension does not exist.
        return Integer.parseInt(config.get(dimension).toString());
    }

    public void generateConfigurationFile() {

    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public void setWorldHeight(int worldHeight) {
        this.worldHeight = worldHeight;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public void setWorldWidth(int worldWidth) {
        this.worldWidth = worldWidth;
    }

    public void setPlacedEntities(List<PlacedEntity> placedEntities) {
        this.placedEntities = placedEntities;
    }

    public void addPlacedEntity(String objectString) {
        this.placedEntities.add(new PlacedEntity(objectString));
    }

    public void addPlacedEntity(Map<String, Object> objectMap) {
        JSONParser parser = new JSONParser();
        this.placedEntities.add(new PlacedEntity(JSONObject.toJSONString(objectMap)));
    }

    public void replacePlacedEntity(int index, String objectString) {
        JSONParser parser = new JSONParser();
        this.placedEntities.set(index, new PlacedEntity(objectString));
    }

    public void replacePlacedEntity(int index, Map<String, Object> objectMap) {
        JSONParser parser = new JSONParser();
        this.placedEntities.set(index, new PlacedEntity(JSONObject.toJSONString(objectMap)));
    }

    public List<GeneratedEntities> getGeneratedEntities() {
        return generatedEntities;
    }

    public void setGeneratedEntities(List<GeneratedEntities> generatedEntities) {
        this.generatedEntities = generatedEntities;
    }

    public String toString() {
        return config.toString();
    }

    public void save() {
        save(configurationFile.toPath());
    }

    public void save(Path path) {

    }

    public void export(Path path) {
        save(path);
    }
}