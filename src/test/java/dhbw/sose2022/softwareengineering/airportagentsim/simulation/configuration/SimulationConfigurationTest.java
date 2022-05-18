package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.*;

public class SimulationConfigurationTest {
    SimulationConfiguration testConfiguration;
    int[] randomNumbers = new int[3];

    @Before
    public void setUp() throws Exception {
        Random random = new Random();

        for (int i = 0; i < 3; i++)
            randomNumbers[i] = random.nextInt();

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
                        "          \"test\": 258\n" +
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
                        "      \n" +
                        "      ],\n" +
                        "      \"width\": 0,\n" +
                        "      \"height\": 0,\n" +
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

    @Test
    public void getWorldDimension() {
        assertArrayEquals(new int[]{randomNumbers[2], randomNumbers[1]},
                testConfiguration.getWorldDimension());
    }

    @Test
    public void testToString() throws IOException {
        assertEquals(testConfiguration,
                new SimulationConfiguration(testConfiguration.toString()));
    }

    @Test
    public void exceptionTest() {
        String jsonString1 =
                "{\n" +
                        "  \"seed\": " + randomNumbers[0] + ",\n" +
                        "  \"width\": " + randomNumbers[1] + ",\n" +
                        "  \"height\": " + randomNumbers[2] + ",\n" +
                        "  \"asd\": " + randomNumbers[2] + ",\n" +
                        "  \"placedEntities\": []\n" +
                        "}";
        assertThrows(IOException.class, () ->
                new SimulationConfiguration(jsonString1)
        );

        String jsonString2 =
                "{\n" +
                        "  \"seed\": " + randomNumbers[0] + ",\n" +
                        "  \"width\": " + randomNumbers[1] + ",\n" +
                        "  \"placedEntities\": []\n" +
                        "}";
        assertThrows(IOException.class, () ->
                new SimulationConfiguration(jsonString2)
        );

        // TODO should throw exception if there are redundant keys.
//        String jsonString3 =
//                "{\n" +
//                        "  \"seed\": " + randomNumbers[0] + ",\n" +
//                        "  \"width\": " + randomNumbers[1] + ",\n" +
//                        "  \"height\": " + randomNumbers[2] + ",\n" +
//                        "  \"seed\": " + randomNumbers[2] + ",\n" +
//                        "  \"placedEntities\": []\n" +
//                        "}";
//        assertThrows(IOException.class, () -> {
//            new SimulationConfiguration(jsonString3);
//        });
    }
}