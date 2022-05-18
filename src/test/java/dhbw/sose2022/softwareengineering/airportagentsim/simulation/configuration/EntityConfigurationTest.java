package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class EntityConfigurationTest {
    EntityConfiguration testEntityConfiguration;
    int[] randomNumbers = new int[5];

    @Before
    public void setUp() {
        Random random = new Random();

        for (int i = 0; i < 5; i++)
            randomNumbers[i] = random.nextInt();

        String jsonString =
                "{\n" +
                        "      \"type\": \"" + randomNumbers[0] + "\",\n" +
                        "      \"position\": [\n" +
                        "        " + randomNumbers[1] + ",\n" +
                        "        " + randomNumbers[2] + "\n" +
                        "      ],\n" +
                        "      \"width\": " + randomNumbers[3] + ",\n" +
                        "      \"height\": " + randomNumbers[4] + ",\n" +
                        "      \"generates\": [\n" +
                        "        {\n" +
                        "          \"type\": \"" + randomNumbers[0] + "\",\n" +
                        "          \"generationRate\": " + randomNumbers[1] + "\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"pluginAttributes\": {\n" +
                        "        \"att1\":" + randomNumbers[0] +
                        "       }\n" +
                        "    }";

        Reader reader = new CharArrayReader(jsonString.toCharArray());
        testEntityConfiguration = new Gson().fromJson(reader, EntityConfiguration.class);
    }

    @Test
    public void getWidth() {
        assertEquals(randomNumbers[3], testEntityConfiguration.getWidth());
    }

    @Test
    public void getHeight() {
        assertEquals(randomNumbers[4], testEntityConfiguration.getHeight());
    }

    @Test
    public void getEntityType() {
        assertEquals(String.valueOf(randomNumbers[0]), testEntityConfiguration.getEntityType());
    }

    @Test
    public void getPosition() {
        assertArrayEquals(new int[]{randomNumbers[1], randomNumbers[2]},
                testEntityConfiguration.getPosition());
    }

    @Test
    public void getPluginAttributes() {
        assertEquals(new Gson().fromJson("{\"att1\":" + randomNumbers[0] + "}", JsonObject.class),
                testEntityConfiguration.getPluginAttributes());
    }

    @Test
    public void getGenerates() {
        assertEquals(String.valueOf(randomNumbers[0]), testEntityConfiguration.getGenerates()[0].getType());
        assertEquals(randomNumbers[1], testEntityConfiguration.getGenerates()[0].getGenerationRate());
    }

    @Test
    public void testToString() {
        assertEquals(testEntityConfiguration,
                new Gson().fromJson(testEntityConfiguration.toString(), EntityConfiguration.class));
    }

    // TODO Test description
    @Test
    public void exceptionTest() {
        String jsonString1 =
                """
                {
                "type": "entrance",
                "position": [
                  14,
                  13
                ],
                "height": 42,
                "generates": [
                  {
                    "type": "passenger",
                    "generationRate": 258
                  }
                ],
                "pluginAttributes": {}
                }""";
        assertThrows(IOException.class, () -> new SimulationConfiguration(jsonString1));

        String jsonString2 =
                """
                {
                "type": "entrance",
                "unusedAttribute": "bla",
                "position": [
                  13
                ],
                "height": 42,
                "width": 42,
                "generates": [
                  {
                    "type": "passenger",
                    "generationRate": 258
                  }
                ],
                "pluginAttributes": {}
                }""";
        assertThrows(IOException.class, () -> new SimulationConfiguration(jsonString2));

        // TODO should throw exception if there are redundant keys.
//        String jsonString3 =
//                 """
//                {
//                "type": "entrance",
//                "position": [
//                  14,
//                  13
//                ],
//                "height": 42,
//                "height": 42,
//                "width": 42,
//                "generates": [
//                  {
//                    "type": "passenger",
//                    "generationRate": 258
//                  }
//                ],
//                "pluginAttributes": {}
//                }""";
//        assertThrows(IOException.class, () -> {
//            new SimulationConfiguration(jsonString3);
//        });

        String jsonString4 =
                """
                {
                "type": "entrance",
                "unusedAttribute": "bla",
                "position": [
                  14,
                  14,
                  13
                ],
                "height": 42,
                "width": 42,
                "generates": [
                  {
                    "type": "passenger",
                    "generationRate": 258
                  }
                ],
                "pluginAttributes": {}
                }""";
        assertThrows(IOException.class, () -> new SimulationConfiguration(jsonString4));

        String jsonString5 =
                """
                {
                "type": "entrance",
                "unusedAttribute": "bla",
                "position": [
                  13
                ],
                "height": 42,
                "width": 42,
                "generates": [
                  {
                    "type": "passenger",
                    "generationRate": 258
                  }
                ],
                "pluginAttributes": {}
                }""";
        assertThrows(IOException.class, () -> new SimulationConfiguration(jsonString5));
    }
}