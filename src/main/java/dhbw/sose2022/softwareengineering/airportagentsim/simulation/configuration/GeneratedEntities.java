package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GeneratedEntities {
    String type;
    int generationRate;

    public GeneratedEntities(String entityType, int generationRate) {
        this.type = entityType;
        this.generationRate = generationRate;
    }

    public String getType() {
        return type;
    }

    public int getGenerationRate() {
        return generationRate;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(this);
        return jsonString;
    }
}
