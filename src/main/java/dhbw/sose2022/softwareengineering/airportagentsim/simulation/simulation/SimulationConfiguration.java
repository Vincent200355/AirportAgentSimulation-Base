package dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class SimulationConfiguration {
    File configurationFile;
    JSONParser jasonParser = new JSONParser();
    JSONObject config = new JSONObject();

    public SimulationConfiguration(File configurationFile) {
        this.configurationFile = configurationFile;
        // Create JSONObject from configurationFile
        try {
            config = (JSONObject) jasonParser.parse(new FileReader(configurationFile));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public SimulationConfiguration(String path) {
        // TODO configurationFile doesn't exist.
        this(new File(path));
    }

    /**
     * Returns the value of the searched dimension of the world as an integer.
     *
     * @param dimension the searched dimension
     * @return the size
     */
    public int getWorldDimension(String dimension) {
        // TODO value is not compatible to Integer.
        // TODO the requested dimension does not exist.
        return Integer.parseInt(config.get(dimension).toString());
    }

    /**
     * @return A list of maps, where each map represents an entity.
     */
    public List<Map<String, Object>> getPlacedEntities() {
        List<Map<String, Object>> placedEntities = new ArrayList<>();

        // TODO Move keys to a central class for easier customization
        String[] entityAttributesKeys = {
                "type",
                "with",
                "height",
                "position",
                "pluginAttributes"
        };

        Map<String, Class<?>> entityAttributesTemplate = new HashMap<>();
        entityAttributesTemplate.put("type", String.class);
        entityAttributesTemplate.put("with", Integer.class);
        entityAttributesTemplate.put("height", Integer.class);
        entityAttributesTemplate.put("position", int[].class);
        entityAttributesTemplate.put("pluginAttributes", String.class);

        JSONArray placedEntitiesJASON = (JSONArray) config.get("placedEntities");

        placedEntitiesJASON.forEach(item -> {
            Class<?> format;
            JSONObject entityJSON = (JSONObject) item;
            Map<String, Object> entity = new HashMap<>();

            for (String attribute : entityAttributesKeys) {
                format = entityAttributesTemplate.get(attribute);
                entity.put(attribute, format.cast(entityJSON.get(attribute)));
                placedEntities.add(entity);
            }
        });

        return placedEntities;
    }

}