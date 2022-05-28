package dhbw.sose2022.softwareengineering.airportagentsim.simulation.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GenerationAttributes {
    /**
     * The {@code DEFAULT_KEY_SET} is the set of the default keys.
     * <p>These keys must be present in the generation attributes file, otherwise the entity configuration cannot be loaded correctly.
     */
    private static final Set<String> DEFAULT_KEY_SET = new HashSet<>();

    static {
        DEFAULT_KEY_SET.add("type");
        DEFAULT_KEY_SET.add("generationRate");
    }

    /**
     * The type is the id of the entity to be generated.
     */
    String type;
    /**
     * The generationRate indicates how many entities should be generated per hour.
     */
    int generationRate;
    
    /**
     * Constructs {@link GenerationAttributes} without initializing any fields. This should only be used by GSON.
     */
    public GenerationAttributes() {}
    
    /**
     * Constructs {@link GenerationAttributes} with the specified entityType and generationRate.
     *
     * @param entityType     The type is the id of the entity to be generated.
     * @param generationRate The generationRate indicates how many entities should be generated per hour.
     */
    public GenerationAttributes(String entityType, int generationRate) {
        this.type = entityType;
        this.generationRate = generationRate;
    }

    /**
     * The type is the id of the entity to be generated.
     *
     * @return The type.
     */
    public String getType() {
        return type;
    }

    /**
     * The generationRate indicates how many entities should be generated per hour.
     *
     * @return The generationRate.
     */
    public int getGenerationRate() {
        return generationRate;
    }

    /**
     * The {@code DEFAULT_KEY_SET} is the set of the default keys.
     * <p>These keys must be present in the generation attributes file, otherwise the entity configuration cannot be loaded correctly.
     *
     * @return The {@code default keys} as a hash set
     */
    public Set<String> getDefaultKeys() {
        return DEFAULT_KEY_SET;
    }

    /**
     * Returns the string representation of these attributes to generate entities.
     * <p> The string is returned in Json format.
     *
     * @return The string representation of these attributes to generate entities.
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
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        if (!this.type.equals(((GenerationAttributes) obj).getType()))
            return false;
        return (this.generationRate == ((GenerationAttributes) obj).getGenerationRate());
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
     *     #equals(Object) equals} method, then calling the {@code
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
        return this.type.hashCode() + this.generationRate;
    }
}