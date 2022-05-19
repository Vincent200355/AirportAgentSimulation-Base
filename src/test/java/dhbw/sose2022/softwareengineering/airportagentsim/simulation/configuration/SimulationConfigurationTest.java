package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.*;

public class SimulationConfigurationTest {
    SimulationConfiguration testConfiguration;
    int[] randomNumbers = new int[3];

    /**
     * Sets up a test SimulationConfiguration for the tests.
     *
     * @throws Exception if the json string is not valid
     */
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

    /**
     * Tests that the world parameters are set returned correctly.
     * <p> Parameters are:
     * <li>{@code seed}</li>
     * <li>{@code width}</li>
     * <li>{@code height}</li>
     */
    @Test
    public void getWorldParameter() {
        assertEquals(randomNumbers[0], testConfiguration.getSeed());
        assertEquals(randomNumbers[1], testConfiguration.getWidth());
        assertEquals(randomNumbers[2], testConfiguration.getHeight());
    }

    /**
     * Tests that the placed entities are set and returned correctly.
     */
    @Test
    public void getPlacedEntities() {
        assertEquals(42, testConfiguration.getPlacedEntities()[0].getHeight());
    }

    /**
     * Tests that the world dimensions are set and returned correctly.
     * <p> World dimensions are:
     * <li>{@code width}</li>
     * <li>{@code height}</li>
     */
    @Test
    public void getWorldDimension() {
        assertArrayEquals(new int[]{randomNumbers[2], randomNumbers[1]},
                testConfiguration.getWorldDimension());
    }

    /**
     * Tests that the toString function returns the expected value.
     */
    @Test
    public void testToString() {
        try {
            assertEquals(testConfiguration,
                    new SimulationConfiguration(testConfiguration.toString()));
        } catch (Exception e) {
            fail(e.toString());
        }

    }

    /**
     * Tests that the corresponding exceptions are thrown in the event of incorrect input.
     */
    @Test
    public void exceptionTest() {
        // more keys than the defaults are present
        String jsonString1 =
                "{\n" +
                        "  \"seed\": " + randomNumbers[0] + ",\n" +
                        "  \"width\": " + randomNumbers[1] + ",\n" +
                        "  \"height\": " + randomNumbers[2] + ",\n" +
                        "  \"test\": " + randomNumbers[2] + ",\n" +
                        "  \"placedEntities\": []\n" +
                        "}";
        assertThrows(IOException.class, () ->
                new SimulationConfiguration(jsonString1)
        );

        // not every default key is present
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