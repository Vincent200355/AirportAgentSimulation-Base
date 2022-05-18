package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collection;

public class EntityConfiguration {
    private String type;
    private int[] position;
    private int width;
    private int height;
    private GeneratedEntities[] generates;
    private Collection pluginAttributes;

    public String getEntityType() {
        return type;
    }

    public int[] getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Collection getPluginAttributes() {
        return pluginAttributes;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(this);
        return jsonString;
    }
}
