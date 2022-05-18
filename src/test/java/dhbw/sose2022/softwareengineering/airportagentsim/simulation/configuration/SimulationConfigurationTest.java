package dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration.SimulationConfiguration;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class SimulationConfigurationTest extends Object {

    @Test
    public void getWorldDimension() throws FileNotFoundException {
        Path testFilePath = Paths.get("src/main/resources/ConfigurationFile.jason");
        SimulationConfiguration config = new SimulationConfiguration(testFilePath);
        assertTrue("Unable to read configfile correct", config.getWorldDimension("width") == 50);
        System.out.println(config.getWorldDimension("width"));
    }


}