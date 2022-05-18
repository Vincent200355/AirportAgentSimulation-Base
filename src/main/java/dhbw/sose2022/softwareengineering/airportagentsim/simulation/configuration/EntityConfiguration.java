package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class EntityConfiguration {
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
    private JsonObject pluginAttributes;

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
        int positionArraySize = jsonObject.getAsJsonArray("position").size();
        if (positionArraySize != 2) {
            throw new IOException("Only two dimensions are allowed for the position of placedEntities. \n" +
                    "actual dimension count: " + positionArraySize);
        }

        // define object parameters
        type = jsonObject.getAsJsonPrimitive("type").getAsString();
        position = gson.fromJson(jsonObject.getAsJsonArray("position"), int[].class);
        width = jsonObject.getAsJsonPrimitive("width").getAsInt();
        height = jsonObject.getAsJsonPrimitive("height").getAsInt();
        generates = gson.fromJson(jsonObject.getAsJsonArray("generates"), GenerationAttributes[].class);
        pluginAttributes = jsonObject.getAsJsonObject("pluginAttributes");
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
    public JsonObject getPluginAttributes() {
        return pluginAttributes;
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

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     *
     * <p>
     * An equivalence relation partitions the elements it operates on
     * into <i>equivalence classes</i>; all the members of an
     * equivalence class are equal to each other. Members of an
     * equivalence class are substitutable for each other, at least
     * for some purposes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @implSpec The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * In other words, under the reference equality equivalence
     * relation, each equivalence class only has a single element.
     * @apiNote It is generally necessary to override the {@link #hashCode hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {
        // compare every class parameter
        if (obj == null)
            return this == null;
        if (obj.getClass() != this.getClass())
            return false;
        if (!this.type.equals(((EntityConfiguration) obj).getEntityType()))
            return false;
        if (!Arrays.equals(this.position, ((EntityConfiguration) obj).getPosition()))
            return false;
        if (this.width != ((EntityConfiguration) obj).getWidth())
            return false;
        if (this.height != ((EntityConfiguration) obj).getHeight())
            return false;
        if (!this.getPluginAttributes().equals(((EntityConfiguration) obj).getPluginAttributes()))
            return false;
        return (Arrays.equals(this.getGenerates(), ((EntityConfiguration) obj).getGenerates()));
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link HashMap}.
     * <p>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     *     an execution of a Java application, the {@code hashCode} method
     *     must consistently return the same integer, provided no information
     *     used in {@code equals} comparisons on the object is modified.
     *     This integer need not remain consistent from one execution of an
     *     application to another execution of the same application.
     * <li>If two objects are equal according to the {@link
     *     #equals(Object)} method, then calling the {@code
     *     hashCode} method on each of the two objects must produce the
     *     same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     *     according to the {@link #equals(Object)} method, then
     *     calling the {@code hashCode} method on each of the two objects
     *     must produce distinct integer results.  However, the programmer
     *     should be aware that producing distinct integer results for
     *     unequal objects may improve the performance of hash tables.
     * </ul>
     *
     * @return a hash code value for this object.
     * @implSpec As far as is reasonably practical, the {@code hashCode} method defined
     * by class {@code Object} returns distinct integers for distinct objects.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode() {
    	return this.type.hashCode() + this.width + this.height + this.generates.length;
    }
}