package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GenerationAttributes {
    /**
     * The type is the id of the entity to be generated.
     */
    String type;
    /**
     * The generationRate indicates how many entities should be generated per hour.
     */
    int generationRate;

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
}