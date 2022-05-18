package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class SimulationConfigurationTest extends Object {
    SimulationConfiguration testConfiguration;
    int[] randomNumbers = new int[5];
    
    @Before
    public void setUp() throws Exception {
        Random random = new Random();
        randomNumbers = new int[3];

        for (int i = 0; i < 3; i++) {
            randomNumbers[i] = random.nextInt();
        }

        String jsonString =
                "{\n" +
                        "  \"seed\": " + randomNumbers[0] + ",\n" +
                        "  \"width\": " + randomNumbers[1] + ",\n" +
                        "  \"height\": " + randomNumbers[2] + ",\n" +
                        "  \"placedEntities\": [\n" +
                        "    {\n" +
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
                        "    },\n" +
                        "    {\n" +
                        "      \"type\": \"officer\",\n" +
                        "      \"position\": [\n" +
                        "        14,\n" +
                        "        13\n" +
                        "      ],\n" +
                        "      \"generates\": [\n" +
                        "\n" +
                        "      ],\n" +
                        "      \"width\": null,\n" +
                        "      \"height\": null,\n" +
                        "      \"pluginAttributes\": []\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";

        testConfiguration = new SimulationConfiguration(jsonString);
    }

    @Test
    public void getWorldParameter() {
        assertEquals(randomNumbers[0], testConfiguration.getSeed());
        assertEquals(randomNumbers[1], testConfiguration.getWidth());
        assertEquals(randomNumbers[2], testConfiguration.getHeight());
    }

    @Test
    public void getPlacedEntities() {
        assertEquals(42, testConfiguration.getPlacedEntities()[0].getHeight());
    }
}