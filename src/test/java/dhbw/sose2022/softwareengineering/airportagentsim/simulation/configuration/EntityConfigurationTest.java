package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class EntityConfigurationTest extends Object {
    EntityConfiguration testEntityConfiguration;
    int[] randomNumbers = new int[5];

    @Before
    public void setUp() throws Exception {
        Random random = new Random();

        for (int i = 0; i < 5; i++)
            randomNumbers[i] = random.nextInt();

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

        JsonObject testConfiguration = new JsonParser().parse(jsonString).getAsJsonObject();
        Reader reader = new CharArrayReader(jsonString.toCharArray());
        testEntityConfiguration = new Gson().fromJson(reader, EntityConfiguration.class);
    }

    @Test
    public void getWidth() {
        assertEquals(1, 1);
    }

    @Test
    public void getHeight() {
    }

    // TODO Test description
    @Test
    public void exceptionTest() {
        String jsonString1 =
                "    {\n" +
                        "      \"type\": \"entrance\",\n" +
                        "      \"position\": [\n" +
                        "        14,\n" +
                        "        13\n" +
                        "      ],\n" +
                        "      \"height\": 42,\n" +
                        "      \"generates\": [\n" +
                        "        {\n" +
                        "          \"type\": \"passenger\",\n" +
                        "          \"generationRate\": 258,\n" +
                        "          \"testvalue\": 258\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"pluginAttributes\": []\n" +
                        "    }";

        assertThrows(IOException.class, () -> {
            new SimulationConfiguration(jsonString1);
        });

        String jsonString2 =
                "    {\n" +
                        "      \"unusedAttribute\": \"entrance\",\n" +
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
                        "          \"generationRate\": 258,\n" +
                        "          \"testvalue\": 258\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"pluginAttributes\": []\n" +
                        "    }";
        assertThrows(IOException.class, () -> {
            new SimulationConfiguration(jsonString2);
        });

        // TODO should throw exception if there are redundant keys.
//        String jsonString3 =
//                "    {\n" +
//                        "      \"type\": \"entrance\",\n" +
//                        "      \"position\": [\n" +
//                        "        14,\n" +
//                        "        13\n" +
//                        "      ],\n" +
//                        "      \"width\": 42,\n" +
//                        "      \"height\": 42,\n" +
//                        "      \"generates\": [\n" +
//                        "        {\n" +
//                        "          \"type\": \"passenger\",\n" +
//                        "          \"generationRate\": 258,\n" +
//                        "          \"testvalue\": 258\n" +
//                        "        }\n" +
//                        "      ],\n" +
//                        "      \"pluginAttributes\": []\n" +
//                        "    }";
//        assertThrows(IOException.class, () -> {
//            new SimulationConfiguration(jsonString3);
//        });

        String jsonString4 =
                "    {\n" +
                        "      \"type\": \"entrance\",\n" +
                        "      \"position\": [\n" +
                        "        14,\n" +
                        "        14,\n" +
                        "        13\n" +
                        "      ],\n" +
                        "      \"width\": 42,\n" +
                        "      \"height\": 42,\n" +
                        "      \"generates\": [\n" +
                        "        {\n" +
                        "          \"type\": \"passenger\",\n" +
                        "          \"generationRate\": 258,\n" +
                        "          \"testvalue\": 258\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"pluginAttributes\": []\n" +
                        "    }";
        assertThrows(IOException.class, () -> {
            new SimulationConfiguration(jsonString4);
        });

        String jsonString5 =
                "    {\n" +
                        "      \"type\": \"entrance\",\n" +
                        "      \"position\": [\n" +
                        "        13\n" +
                        "      ],\n" +
                        "      \"width\": 42,\n" +
                        "      \"height\": 42,\n" +
                        "      \"generates\": [\n" +
                        "        {\n" +
                        "          \"type\": \"passenger\",\n" +
                        "          \"generationRate\": 258,\n" +
                        "          \"testvalue\": 258\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"pluginAttributes\": []\n" +
                        "    }";
        assertThrows(IOException.class, () -> {
            new SimulationConfiguration(jsonString5);
        });
    }
}