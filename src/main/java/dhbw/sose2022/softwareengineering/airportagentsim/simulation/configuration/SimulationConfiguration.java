package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class SimulationConfiguration {
    //TODO should throw error with wrong configuration
    private final static String defaultPath = "src/main/resources/configurationFile.jason";

    private int seed, height, width;
    private EntityConfiguration[] placedEntities;

    public SimulationConfiguration(String jsonString) {
        try {
            // create Gson instance
            Gson gson = new Gson();

            // convert JSON string to User object
            JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

            seed = jsonObject.getAsJsonPrimitive("seed").getAsInt();
            width = jsonObject.getAsJsonPrimitive("width").getAsInt();
            height = jsonObject.getAsJsonPrimitive("height").getAsInt();

            JsonArray json_placedEntities = jsonObject.getAsJsonArray("placedEntities");
            placedEntities = gson.fromJson(json_placedEntities, EntityConfiguration[].class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public SimulationConfiguration(Path path) throws IOException {
        this(Files.readString(path));
    }

    public SimulationConfiguration() throws IOException {
        this(Path.of(defaultPath));
    }

    /**
     * Returns the value of the searched dimension of the world as an integer.
     *
     * @param dimension the searched dimension
     * @return the size
     */
    public int[] getWorldDimension(String dimension) {
        return new int[]{this.height, this.width};
    }

    public int getSeed() {
        return seed;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public EntityConfiguration[] getPlacedEntities() {
        return placedEntities;
    }

    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(this);
        return jsonString;
    }
}