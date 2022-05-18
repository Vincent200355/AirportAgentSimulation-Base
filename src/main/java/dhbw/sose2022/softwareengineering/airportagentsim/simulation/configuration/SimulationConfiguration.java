package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class SimulationConfiguration {
    private int seed, worldHeight, worldWidth;
    List<PlacedEntity> placedEntities;
    private List<GeneratedEntities> generatedEntities;
    private File configurationFile;
    private JsonObject config = new JsonObject();

    public SimulationConfiguration(File configurationFile) throws FileNotFoundException {
        JsonParser jasonParser = new JsonParser();

        this.configurationFile = configurationFile;
        // Create JsonObject from configurationFile
        config = (JsonObject) jasonParser.parse(new FileReader(configurationFile));

    }

    public SimulationConfiguration(String jsonString) {
        JsonParser jasonParser = new JsonParser();

        JsonObject jsonObject = jasonParser.parse(jsonString).getAsJsonObject();
        this.config = jsonObject;

        this.configurationFile = new File("src/main/resources/configurationFile.jason");
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(configurationFile);
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonArray placedEntitiesJASON = (JsonArray) config.get("placedEntities");


        placedEntitiesJASON.forEach(item -> {
            JsonObject entityJSON = (JsonObject) item;
            this.placedEntities.add(new PlacedEntity(entityJSON));
        });
    }

    public SimulationConfiguration(Path path) throws FileNotFoundException {
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
        JsonParser parser = new JsonParser();
        this.placedEntities.add(new PlacedEntity(new Gson().toJson(objectMap)));
    }

    public void replacePlacedEntity(int index, String objectString) {
        JsonParser parser = new JsonParser();
        this.placedEntities.set(index, new PlacedEntity(objectString));
    }

    public void replacePlacedEntity(int index, Map<String, Object> objectMap) {
        JsonParser parser = new JsonParser();
        this.placedEntities.set(index, new PlacedEntity(new Gson().toJson(objectMap)));
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