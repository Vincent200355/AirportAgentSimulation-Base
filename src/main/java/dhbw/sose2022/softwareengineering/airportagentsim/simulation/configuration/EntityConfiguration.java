package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import com.google.gson.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

class EntityConfiguration {
    /**
     * The DEFAULT_KEY_SET is the set of the default keys.
     * <p>These keys must be present in the configuration file, otherwise the
     * entity configuration cannot be loaded correctly.
     */
    static final Set<String> DEFAULT_KEY_SET = new HashSet<>();

    static {
        DEFAULT_KEY_SET.add("type");
        DEFAULT_KEY_SET.add("position");
        DEFAULT_KEY_SET.add("width");
        DEFAULT_KEY_SET.add("height");
        DEFAULT_KEY_SET.add("generates");
        DEFAULT_KEY_SET.add("pluginAttributes");
    }

    private String type;
    private int[] position;
    private int width;
    private int height;
    private GenerationAttributes[] generates;
    private JsonArray pluginAttributes;

    /**
     * Constructs {@link EntityConfiguration} from a JSON object.
     *
     * @param jsonObject com.google.gson.JsonObject
     * @throws IOException: <li>If not every default key is present.</li>
     *                      <li>If keys other than the default are present.</li>
     *                      <li>If the positions array has more than two dimensions.</li>
     */
    EntityConfiguration(JsonObject jsonObject) throws IOException {
        // create Gson instance
        Gson gson = new Gson();

        // checks whether exactly the keys that are required are available
        if (jsonObject.keySet() != DEFAULT_KEY_SET) {
            Set<String> compare = new HashSet<>();

            if (!jsonObject.keySet().containsAll(DEFAULT_KEY_SET)) {
                compare.addAll(DEFAULT_KEY_SET);
                compare.removeAll(jsonObject.keySet());
                throw new IOException("Not all default keys are present in the configuration of placedEntities.. \n" +
                        "missing key(s): " + compare);
            }

            if (!DEFAULT_KEY_SET.containsAll(jsonObject.keySet())) {
                compare.addAll(jsonObject.keySet());
                compare.removeAll(DEFAULT_KEY_SET);
                throw new IOException("There are more than the default keys of placedEntities. \n" +
                        "unnecessary key(s): " + compare);
            }
        }
        // check position array size is 2
        if (jsonObject.getAsJsonArray("position").size() != 2) {
            throw new IOException("Only two dimensions are allowed for the position of placedEntities. \n" +
                    "actual dimension count: " + jsonObject.getAsJsonArray("position").size());
        }

        type = jsonObject.getAsJsonPrimitive("type").getAsString();
        position = gson.fromJson(jsonObject.getAsJsonArray("position"), int[].class);
        width = jsonObject.getAsJsonPrimitive("width").getAsInt();
        height = jsonObject.getAsJsonPrimitive("height").getAsInt();
        generates = gson.fromJson(jsonObject.getAsJsonArray("generates"), GenerationAttributes[].class);
        pluginAttributes = jsonObject.getAsJsonArray("pluginAttributes");
    }

    /**
     * Constructs {@link EntityConfiguration} from a JSON string.
     *
     * @param jsonString String in JSON format
     * @throws IOException: <li>If not every default key is present.</li>
     *                      <li>If keys other than the default are present.</li>
     *                      <li>If the positions array has more than two dimensions.</li>
     */
    EntityConfiguration(String jsonString) throws IOException {
        this(new Gson().fromJson(jsonString, JsonObject.class));
    }

    /**
     * Returns the id of the placed entity.
     *
     * @return The id as string.
     */
    public String getEntityType() {
        return type;
    }

    /**
     * Returns the position set in the configuration.
     *
     * @return The position as integer array.
     */
    public int[] getPosition() {
        return position;
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
     * Returns the height set in the configuration.
     *
     * @return The height as integer.
     */
    public int getHeight() {
        return height;
    }

    /**
     * The string representation of the plugin attributes
     * <p>The string is returned in Json format.
     *
     * @return The plugin attributes as string.
     */
    public String getPluginAttributes() {
        return pluginAttributes.toString();
    }

    /**
     * The ids of the entities to be generated and the generation rates are stored in the GenerationAttributes.
     *
     * @return The array of GenerationAttributes.
     */
    public GenerationAttributes[] getGenerates() {
        return generates;
    }

    /**
     * Returns the string representation of this {@link EntityConfiguration}.
     * <p> The string is returned in Json format.
     *
     * @return The string representation of this {@link EntityConfiguration}.
     */
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}