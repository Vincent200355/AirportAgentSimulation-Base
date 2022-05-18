package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;

import java.io.CharArrayReader;
import java.io.Reader;

import static org.junit.Assert.assertEquals;

public class EntityConfigurationTest extends Object {
    EntityConfiguration testEntityConfiguration;

    @Before
    public void setUp() throws Exception {
        String jsonString =
                "{\n" +
                        "      \"type\": \"entrance\",\n" +
                        "      \"position\": [\n" +
                        "        14,\n" +
                        "        13\n" +
                        "      ],\n" +
                        "      \"width\": 42,\n" +
                        "      \"height\": 42,\n" +
                        "      \"generates\": [\n" +
                        "        {\n" +
                        "          \"type\": \"passenger\",\n" +
                        "          \"generationRate\": 258\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"pluginAttributes\": []\n" +
                        "    }";

        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        Reader reader = new CharArrayReader(jsonString.toCharArray());
        testEntityConfiguration = new Gson().fromJson(reader, EntityConfiguration.class);

        System.out.println(testEntityConfiguration);
    }

    @Test
    public void getWidth() {
        assertEquals(1, 1);
    }

    @Test
    public void getHeight() {
    }
}