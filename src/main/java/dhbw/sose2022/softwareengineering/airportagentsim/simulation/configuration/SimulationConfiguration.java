package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

class SimulationConfiguration {
    //TODO should throw error with wrong configuration
    private final static String defaultPath = "src/main/resources/configurationFile.jason";
    private final static Set<String> defaultKeySet = new HashSet<>();

    {
        defaultKeySet.add("seed");
        defaultKeySet.add("height");
        defaultKeySet.add("width");
        defaultKeySet.add("placedEntities");
    }

    private int seed, height, width;
    private EntityConfiguration[] placedEntities;

    public SimulationConfiguration(String jsonString) throws IOException {
        // create Gson instance
        Gson gson = new Gson();

        // convert JSON string to User object
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        if (jsonObject.keySet() != defaultKeySet) {
            Set<String> compare = new HashSet<>();

            if (!jsonObject.keySet().containsAll(defaultKeySet)) {
                compare.addAll(defaultKeySet);
                compare.removeAll(jsonObject.keySet());
                throw new IOException("Not all default keys are present in the configuration. \n" +
                        "missing key(s): " + compare.toString());
            }

            if (!defaultKeySet.containsAll(jsonObject.keySet())) {
                compare.addAll(jsonObject.keySet());
                compare.removeAll(defaultKeySet);
                throw new IOException("There are more than the default keys. \n" +
                        "unnecessary key(s): " + compare);
            }

            seed = jsonObject.getAsJsonPrimitive("seed").getAsInt();
            width = jsonObject.getAsJsonPrimitive("width").getAsInt();
            height = jsonObject.getAsJsonPrimitive("height").getAsInt();

            JsonArray json_placedEntities = jsonObject.getAsJsonArray("placedEntities");
            placedEntities = gson.fromJson(json_placedEntities, EntityConfiguration[].class);

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