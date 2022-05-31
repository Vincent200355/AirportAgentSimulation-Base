package dhbw.sose2022.softwareengineering.airportagentsim.simulation.export;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import com.opencsv.CSVWriter;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation.SimulationWorld;

public class AirportSimExport {
    private Path exportPath;
    private String format;
    private Map<String, String[]> simBuffer;
    private String configBuffer;

    public AirportSimExport(Path exportPath, String format) {
        this.exportPath = exportPath;
        this.format = format;
    }

    // build simBuffer from SimulationWorld
    private void buildSimBuffer(AirportAgentSim sim) {
        // SimulationWorld world = sim.getSimulationWorld();
        // this.simBuffer.put(0, world.getEntities());
    }

    // build configBuffer from SimulationConfig
    private void buildConfigBuffer(AirportAgentSim sim) {
        // TODO: implement
    }

    // export simBuffer to .csv file with openCSV
    public void exportSimToCsv(String fileName, AirportAgentSim sim) {
        buildSimBuffer(sim);

        File exportFile = new File(exportPath + fileName + ".csv");
        try {
            CSVWriter csvWriter = new CSVWriter(new FileWriter(exportFile));
            // CSVWriter csvWriter = new CSVWriter(new FileWriter(exportFile), ';');
            String[] header = { "uid", "step", "entityId", "posX", "posY", "messages" };
            csvWriter.writeNext(header);
            Iterator<Map.Entry<String, String[]>> iterator = simBuffer.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String[]> entry = iterator.next();
                String[] key = { entry.getKey() };
                String[] line = Arrays.copyOf(key, entry.getValue().length + 1);
                System.arraycopy(entry.getValue(), 0, line, line.length, entry.getValue().length);
                csvWriter.writeNext(line);
            }
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // export configBuffer to .json file
    public void exportConfigToJson(String fileName, AirportAgentSim sim) {
        buildConfigBuffer(sim);

        File exportFile = new File(exportPath + fileName + ".json");
        try {
            FileWriter fileWriter = new FileWriter(exportFile);
            fileWriter.write(configBuffer);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
