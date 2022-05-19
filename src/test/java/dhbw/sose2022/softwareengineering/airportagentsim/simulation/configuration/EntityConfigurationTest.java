package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.junit.Before;
import org.junit.Test;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;

import static org.junit.Assert.*;

public class EntityConfigurationTest {
    EntityConfiguration testEntityConfiguration;
    int[] randomNumbers = new int[5];

    /**
     * Sets up a test EntityConfiguration for the tests.
     */
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
                        "      \"pluginAttributes\": [\n" +
                        "       {\"att1\":" + randomNumbers[0] +
                        "       }\n" +
                        "       ]\n" +
                        "    }";

        Reader reader = new CharArrayReader(jsonString.toCharArray());
        testEntityConfiguration = new Gson().fromJson(reader, EntityConfiguration.class);
    }

    /**
     * Tests that the width is set and returned correctly.
     */
    @Test
    public void getWidth() {
        assertEquals(randomNumbers[3], testEntityConfiguration.getWidth());
    }

    /**
     * Tests that the height is set and returned correctly.
     */
    @Test
    public void getHeight() {
        assertEquals(randomNumbers[4], testEntityConfiguration.getHeight());
    }

    /**
     * Tests that the entityType is set and returned correctly.
     */
    @Test
    public void getEntityType() {
        assertEquals(String.valueOf(randomNumbers[0]), testEntityConfiguration.getEntityType());
    }

    /**
     * Tests that the position is set and returned correctly.
     */
    @Test
    public void getPosition() {
        assertArrayEquals(new int[]{randomNumbers[1], randomNumbers[2]},
                testEntityConfiguration.getPosition());
    }

    /**
     * Tests that the plugin attributes are set and returned correctly.
     */
    @Test
    public void getPluginAttributes() {
        assertEquals(new Gson().fromJson("[\n{\"att1\":" + randomNumbers[0] + "}]", JsonArray.class).toString(),
                testEntityConfiguration.getPluginAttributes());
    }

    /**
     * Tests that the generation attributes are set and returned correctly.
     */
    @Test
    public void getGenerates() {
        assertEquals(String.valueOf(randomNumbers[0]), testEntityConfiguration.getGenerates()[0].getType());
        assertEquals(randomNumbers[1], testEntityConfiguration.getGenerates()[0].getGenerationRate());
    }

    /**
     * Tests that the toString function returns the expected value.
     */
    @Test
    public void testToString() {
        assertEquals(testEntityConfiguration,
                new Gson().fromJson(testEntityConfiguration.toString(), EntityConfiguration.class));
    }

    /**
     * Tests that the corresponding exceptions are thrown in the event of incorrect input.
     */
    @Test
    public void exceptionTest() {
        // not every default key is present
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
                "pluginAttributes": []
                }""";
        assertThrows(IOException.class, () -> new SimulationConfiguration(jsonString1));

        // more keys than the defaults are present
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
                "pluginAttributes": []
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
//                "pluginAttributes": []
//                }""";
//        assertThrows(IOException.class, () -> {
//            new SimulationConfiguration(jsonString3);
//        });

        // position has more than two dimensions
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
                "pluginAttributes": []
                }""";
        assertThrows(IOException.class, () -> new SimulationConfiguration(jsonString4));

        // position has less than two dimensions
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
                "pluginAttributes": []
                }""";
        assertThrows(IOException.class, () -> new SimulationConfiguration(jsonString5));

        // not every default keys of generates ara present
        String jsonString6 =
                """
                {
                "type": "entrance",
                "unusedAttribute": "bla",
                "position": [
                  13,
                  13
                ],
                "height": 42,
                "width": 42,
                "generates": [
                  {
                    "generationRate": 258
                  }
                ],
                "pluginAttributes": []
                }""";
        assertThrows(IOException.class, () -> new SimulationConfiguration(jsonString6));

        // more than the default keys of generates ara present
        String jsonString7 =
                """
                {
                "type": "entrance",
                "unusedAttribute": "bla",
                "position": [
                  13,
                  13
                ],
                "height": 42,
                "width": 42,
                "generates": [
                  {
                    "type": "passenger",
                    "test": "passenger",
                    "generationRate": 258
                  }
                ],
                "pluginAttributes": []
                }""";
        assertThrows(IOException.class, () -> new SimulationConfiguration(jsonString7));
    }
}