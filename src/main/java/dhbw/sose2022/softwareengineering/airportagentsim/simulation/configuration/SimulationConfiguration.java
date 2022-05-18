package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class SimulationConfiguration {
    /**
     * This is the default path for the configuration file.
     */
    private static final String DEFAULT_PATH = "src/main/resources/configurationFile.jason";
    /**
     * The DEFAULT_KEY_SET is the set of the default keys.
     * <p>These keys must be present in the configuration file, otherwise the
     * entity configuration cannot be loaded correctly.
     */
    private static final Set<String> DEFAULT_KEY_SET = new HashSet<>();

    static {
        DEFAULT_KEY_SET.add("seed");
        DEFAULT_KEY_SET.add("height");
        DEFAULT_KEY_SET.add("width");
        DEFAULT_KEY_SET.add("placedEntities");
    }

    private int seed;
    private int height;
    private int width;
    private List<EntityConfiguration> placedEntities = new ArrayList<>();

    /**
     * Constructs the {@link SimulationConfiguration} from a JSON string.
     *
     * @param jsonString String in JSON format
     * @throws IOException: <li>If not every default key is present.</li>
     *                      <li>If keys other than the default are present.</li>
     */
    public SimulationConfiguration(String jsonString) throws IOException {
        // create Gson instance
        Gson gson = new Gson();

        // convert JSON string to JsonObject
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        if (jsonObject.keySet() != DEFAULT_KEY_SET) {
            Set<String> compare = new HashSet<>();

            if (!jsonObject.keySet().containsAll(DEFAULT_KEY_SET)) {
                compare.addAll(DEFAULT_KEY_SET);
                compare.removeAll(jsonObject.keySet());
                throw new IOException("Not all default keys are present in the configuration. \n" +
                        "missing key(s): " + compare);
            }

            if (!DEFAULT_KEY_SET.containsAll(jsonObject.keySet())) {
                compare.addAll(jsonObject.keySet());
                compare.removeAll(DEFAULT_KEY_SET);
                throw new IOException("There are more than the default keys. \n" +
                        "unnecessary key(s): " + compare);
            }

            seed = jsonObject.getAsJsonPrimitive("seed").getAsInt();
            width = jsonObject.getAsJsonPrimitive("width").getAsInt();
            height = jsonObject.getAsJsonPrimitive("height").getAsInt();
            for (JsonElement ec : jsonObject.getAsJsonArray("placedEntities")) {
                placedEntities.add(new EntityConfiguration(ec.getAsJsonObject()));
            }
        }
    }

    /**
     * Constructs the {@link SimulationConfiguration} from a JSON file.
     *
     * @param path Path to the file.
     * @throws IOException: <li>If not every default key is present.</li>
     *                      <li>If keys other than the default are present.</li>
     */
    public SimulationConfiguration(Path path) throws IOException {
        this(Files.readString(path));
    }

    /**
     * Constructs the {@link SimulationConfiguration} from the file located at the default path.
     *
     * @throws IOException: <li>If not every default key is present.</li>
     *                      <li>If keys other than the default are present.</li>
     */
    public SimulationConfiguration() throws IOException {
        this(Path.of(DEFAULT_PATH));
    }

    /**
     * Returns the dimension of the world set in the configuration.
     *
     * @return The size as integer array.
     */
    public int[] getWorldDimension() {
        return new int[]{this.height, this.width};
    }

    /**
     * Returns the seed set in the configuration.
     *
     * @return The seed as integer.
     */
    public int getSeed() {
        return seed;
    }

    /**
     * Returns the height set in the configuration.
     *
     * @return The height as integer.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the width set in the configuration.
     *
     * @return The width as integer.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the attributes of the entities set in the configuration
     *
     * @return The attributes of the entities as {@link EntityConfiguration} array.
     */
    public EntityConfiguration[] getPlacedEntities() {
        return placedEntities.toArray(EntityConfiguration[]::new);
    }

    /**
     * Returns the string representation of this {@link SimulationConfiguration}.
     * <p> The string is returned in Json format.
     *
     * @return The string representation of this {@link SimulationConfiguration}.
     */
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}