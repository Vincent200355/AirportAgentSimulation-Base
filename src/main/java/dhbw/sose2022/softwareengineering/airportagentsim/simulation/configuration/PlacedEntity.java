package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import com.google.gson.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Math.toIntExact;

public class PlacedEntity extends TreeMap { // TODO implement MapMethods
    private Map<String, Object> attributes = new TreeMap<>();
    private JsonObject entityJSON;

    public enum varTypes {
        STRING, // TODO comments
        INTEGER,
        INTEGERARRAY
    }

    private final Map<String, varTypes> entityAttributesTemplate = new HashMap<>();

    {
        // TODO handle false keys / to much attributes
        entityAttributesTemplate.put("type", varTypes.STRING);
        entityAttributesTemplate.put("width", varTypes.INTEGER);
        entityAttributesTemplate.put("height", varTypes.INTEGER);
        entityAttributesTemplate.put("position", varTypes.INTEGERARRAY);
        entityAttributesTemplate.put("pluginAttributes", varTypes.STRING);
    }

    public PlacedEntity(JsonObject entityJSON) {
        this.entityJSON = entityJSON;
        Map<String, Object> entity = new HashMap<>();

        for (String attributeKey : entityAttributesTemplate.keySet()) {
            this.setAttribute(attributeKey, entityJSON.get(attributeKey));
        }
    }

    public PlacedEntity(String entityString) {
        JsonParser parser = new JsonParser();
        JsonObject entityJSON;

        entityJSON = (JsonObject) parser.parse(entityString);
        this.entityJSON = entityJSON;
        Map<String, Object> entity = new HashMap<>();

        for (String attributeKey : entityAttributesTemplate.keySet()) {
            this.setAttribute(attributeKey, entityJSON.get(attributeKey));
        }

    }

    public Object getAttribute(String attributeKey) {
        // TODO incorrect attribute key
        return this.attributes.get(attributeKey);
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @implSpec The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return super.getOrDefault(key, defaultValue);
    }

    public void setAttribute(String attributeKey, Object value) { // TODO redundant methods for each value type
        if (attributes.containsKey(attributeKey))
            attributes.remove(attributeKey);

        switch (entityAttributesTemplate.get(attributeKey)) {
            case STRING:
                if (value.getClass() == JsonObject.class) {
                    this.attributes.put(attributeKey, ((JsonObject) value).toString());
                } else {
                    this.attributes.put(attributeKey, value.toString());
                }
                break;
            case INTEGER:
                this.attributes.put(attributeKey, toIntExact((Long) value));
                break;
            case INTEGERARRAY:
                int[] integerArray;
                if (value instanceof JsonArray) {
                    JsonArray jsonArray = new JsonArray();
                    jsonArray = (JsonArray) value;

                    String[] stringArray = jsonArray.toString()
                            .replaceAll("\\[", "")
                            .replaceAll("\\]", "")
                            .replaceAll("\\s", "")
                            .split(",");

                    integerArray = new int[stringArray.length];

                    for (int i = 0; i < stringArray.length; i++) {
                        integerArray[i] = Integer.valueOf(stringArray[i]);
                    }
                } else if (value instanceof int[])
                    integerArray = (int[]) value;
                else {
                    integerArray = new int[1];
                }
                this.attributes.put(attributeKey, integerArray);
                break;
            default:
                // TODO Logger
        }

        // TODO incorrect attribute key
    }

    public JsonObject getJSONObject() {
        return entityJSON;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(entityJSON);
    }
}
