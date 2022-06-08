package dhbw.sose2022.softwareengineering.airportagentsim.simulation.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.AirportAgentSimulationAPI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("ALL")
public class SimulationConfiguration {
    /**
     * This is the default path for the configuration file.
     */
    private static final String DEFAULT_PATH = "src/main/resources/configurationFile.jason";
    /**
     * The DEFAULT_KEY_SET is the set of the default keys.
     * <p>These keys must be present in the configuration file, otherwise the
     * entity configuration cannot be loaded correctly.
     */
    public static final TreeMap<String, Class> DEFAULT_KEY_MAP_WORLD = new TreeMap<>();

    static {
        DEFAULT_KEY_MAP_WORLD.put("seed", int.class);
        DEFAULT_KEY_MAP_WORLD.put("duration", long.class);
        DEFAULT_KEY_MAP_WORLD.put("height", int.class);
        DEFAULT_KEY_MAP_WORLD.put("width", int.class);
        DEFAULT_KEY_MAP_WORLD.put("placedEntities", EntityConfiguration.class);
    }

    private int seed;
    private long duration;
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

        JsonObject jsonObject = checkFile(jsonString);

            seed = jsonObject.getAsJsonPrimitive("seed").getAsInt();
            duration = jsonObject.getAsJsonPrimitive("duration").getAsLong();
            width = jsonObject.getAsJsonPrimitive("width").getAsInt();
            height = jsonObject.getAsJsonPrimitive("height").getAsInt();
            for (JsonElement ec : jsonObject.getAsJsonArray("placedEntities")) {
                placedEntities.add(new EntityConfiguration(ec.getAsJsonObject()));
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
     * Checks if the given JSONSting meets the requirements. If it doesn't, an IOException is thrown.
     *
     * @param jsonString
     * @return
     * @throws IOException
     */
    private JsonObject checkFile(String jsonString) throws IOException {
        // create Gson instance
        Gson gson = new Gson();

        // convert JSON string to JsonObject
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        // check world part of the JSON
        if (jsonObject.keySet() != DEFAULT_KEY_MAP_WORLD.keySet()) {
            Set<String> compare = new HashSet<>();

            if (!jsonObject.keySet().containsAll(DEFAULT_KEY_MAP_WORLD.keySet())) {
                compare.addAll(DEFAULT_KEY_MAP_WORLD.keySet());
                compare.removeAll(jsonObject.keySet());
                throw new IOException("Not all default keys are present in the configuration. \n" +
                        "missing key(s): " + compare);
            }

            if (!DEFAULT_KEY_MAP_WORLD.keySet().containsAll(jsonObject.keySet())) {
                compare.addAll(jsonObject.keySet());
                compare.removeAll(DEFAULT_KEY_MAP_WORLD.keySet());
                throw new IOException("There are more than the default keys. \n" +
                        "unnecessary key(s): " + compare);
            }
        }
        return jsonObject;
    }

    /**
     * Returns the dimension of the world set in the configuration.
     * <li> First element is {@code height}</li>
     * <li> Second element is {@code width}</li>
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
     * Returns the duration of the simulatione set in the configuration.
     *
     * @return The duration as long.
     */
    public long getDuration() {
        return duration;
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

    public void update(int worldHeight, int worldWidth, Collection<Entity> entities) {
        this.height = worldHeight;
        this.width = worldWidth;
        List<EntityConfiguration> placedEnteties = new ArrayList<>();
        for (Entity e : entities) {
            placedEnteties.add(new EntityConfiguration(
                    //TODO ID instead of name
                    AirportAgentSimulationAPI.getLoadedPlugin(e.getPlugin()).getID(),
                    new int[]{e.getPosition().getX(), e.getPosition().getY()},
                    e.getWidth(),
                    e.getHeight()
            ));
        }
        this.placedEntities = placedEnteties;
    }

    public void saveToJSON() {
        Gson gson = new Gson();
        File confFile = new File(DEFAULT_PATH);
        System.out.println(gson.toJson(this));
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
        if (this.width != ((SimulationConfiguration) obj).getWidth())
            return false;
        if (this.height != ((SimulationConfiguration) obj).getHeight())
            return false;
        return (this.seed == ((SimulationConfiguration) obj).getSeed());
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
     * <li>If two objects are equal according to the {@link #equals(Object)
     *     equals} method, then calling the {@code
     *     hashCode} method on each of the two objects must produce the
     *     same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     *     according to the {@link #equals(Object) equals} method, then
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
        return this.seed + this.width + this.height + this.placedEntities.size();
    }
}