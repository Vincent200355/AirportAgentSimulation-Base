package dhbw.sose2022.softwareengineering.airportagentsim.simulation.export;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.opencsv.CSVWriter;

import javax.imageio.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.Message;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.AirportAgentSimulationAPI;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation.SimulationWorld;

public final class AirportSimExporter extends ExportLogger {
    private Path exportPath;
    private String format;
    private final ArrayList<String[]> simBuffer = new ArrayList<String[]>();
    private int currentTick;
    private SimulationWorld world;
    private AirportAgentSim aas;

    Boolean withSnapshots = false;
    ExecutorService pool;

    /**
     * Constructor for the exporter.
     * 
     * @param exportPath
     *           The path to the export file.
     * @param format
     *          The format of the export file.
     * @param aas
     *         The airport agent sim.
     * @param withSnapshots
     *        If the snapshots should be exported. (default: false; increases runtime a lot!)
     */
    public AirportSimExporter(Path exportPath, String format, AirportAgentSim aas, Boolean withSnapshots) {
        this.aas = aas;
        this.world = aas.getWorld();
        this.exportPath = exportPath;
        this.format = format;

        this.withSnapshots = withSnapshots;
    }
    public AirportSimExporter(Path exportPath, String format, AirportAgentSim aas) {
        this.aas = aas;
        this.world = aas.getWorld();
        this.exportPath = exportPath;
        this.format = format;
    }

    // lifecycle methods
    @Override
    public void afterInit() {
        new File(exportPath.toString()).mkdirs();
        if (this.withSnapshots) {
            this.pool = Executors.newCachedThreadPool();
            new File(exportPath + "/snapshots").mkdir();
            File[] oldImg = new File(exportPath + "/snapshots").listFiles();
            for (File f : oldImg) {
                f.delete();
            }
        }
        currentTick = 0;
        int width = world.getWidth();
        int height = world.getHeight();
        this.simBuffer.add(csvLineFactory(0, "world", "", 0, 0, width, height, ""));
        grabEntities();
    }

    @Override
    public void afterTick() {
        currentTick++;
        grabEntities();
        if (this.withSnapshots) {
            drawAndSaveSnapshot((double) 1920 / this.world.getWidth());
        }
    }

    @Override
    public void afterSimFinished() {
        try {
            if (this.withSnapshots) {
                this.pool.shutdown();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    //helper functions to build export line
    private String[] csvLineFactory(int uid, String type, String plugin, int posX, int posY, int width, int height, String messages) {
        return new String[] { (simBuffer == null) ? "0" : Integer.toString(simBuffer.size()), Integer.toString(currentTick), Integer.toString(uid), type, plugin, Integer.toString(posX), Integer.toString(posY),
                Integer.toString(width), Integer.toString(height), messages };
    }

    // helper to save current entity state
    private void grabEntities() {
        Collection<Entity> entities = world.getEntities();
        for (Entity entity : entities) {
            int uid = entity.getUID();
            Point pos = entity.getPosition();
            int width = entity.getWidth();
            int height = entity.getHeight();
            String type = entity.getClass().getSimpleName();
            String plugin = (entity.getPlugin() == null) ? "anonymous" : AirportAgentSimulationAPI.getLoadedPlugin(entity.getPlugin()).getName();
            ArrayList<Message> messages = world.getMessages();
            String messageBuffer = "";
            for (Message message : messages) {
                if (message.getOrigin().getUID() == uid) {
                    messageBuffer += (messageBuffer.length() == 0) ? message.toString() : "#" + message.toString();
                }
            }
            this.simBuffer.add(csvLineFactory(uid, type, plugin, pos.getX(), pos.getY(), width, height, messageBuffer));
        }
    }

    // export simBuffer to .csv file with openCSV
    public void exportSimToCsv(String fileName) {
        File exportFile = new File(exportPath + "/" + fileName + "." + format);
        try {
            CSVWriter csvWriter = new CSVWriter(new FileWriter(exportFile), ';', Character.MIN_VALUE, Character.MIN_VALUE, "\r\n");
            String[] header = { "lNo", "tick", "entityId", "entityType", "plugin", "posX", "posY", "wX", "wY", "messages" };
            csvWriter.writeNext(header);
            for (String[] row : simBuffer) {
                csvWriter.writeNext(row);
            }
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // export configBuffer to .json file
    public void exportConfigToJson(String fileName) {
        String configBuffer = aas.getConfiguration().toString();

        File exportFile = new File(exportPath + "/" + fileName + ".json");
        try {
            FileWriter fileWriter = new FileWriter(exportFile);
            fileWriter.write(configBuffer);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //helper to draw and save tick snapshot
    private void drawAndSaveSnapshot(double scaleFactor) {
        int padding = (int) Math.round(world.getWidth() * 0.1 * scaleFactor);
        int maxWidth = (int) Math.round(world.getWidth() * scaleFactor);
        int maxHeight = (int) Math.round(world.getHeight() * scaleFactor);
        final BufferedImage image = new BufferedImage(maxWidth + (2 * padding), maxHeight + (2 * padding), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = image.createGraphics();
        graphics2D.setPaint(Color.WHITE);
        graphics2D.fillRect(0,0, maxWidth + (2 * padding), maxHeight + (2 * padding));
        graphics2D.setPaint(Color.GRAY);
        graphics2D.drawRect(padding, padding, maxWidth, maxHeight);
        graphics2D.setPaint(Color.BLACK);
        
        graphics2D.setFont(new Font("Arial", Font.PLAIN, (int) Math.round(1920 * 0.01)));
        graphics2D.drawString("Tick: " + currentTick, (int) Math.floor(padding * 0.2), (int) Math.floor(padding * 0.2));

        Collection<Entity> entities = world.getEntities();
        for (Entity entity : entities) {
            Point pos = entity.getPosition();
            int x = (int) Math.round(pos.getX() * scaleFactor + padding);
            int y = (int) Math.round(pos.getY() * scaleFactor + padding);
            int width = (int) Math.round(entity.getWidth() * scaleFactor);
            int height = (int) Math.round(entity.getHeight() * scaleFactor);
            graphics2D.setPaint(generateColor(entity.getUID()));
            graphics2D.fillOval(x, y, width, height);
            graphics2D.setPaint(Color.BLACK);
            String label = entity.getClass().getSimpleName() + " #" + entity.getUID();
            graphics2D.drawString(label, Math.round(x + ( 0.5 * width) - (0.5 * graphics2D.getFontMetrics().stringWidth(label))), Math.round(y + (0.5 * height)));
        }
        
        graphics2D.dispose();

        
        this.pool.execute(new PNGSaver(image, exportPath, String.format("%08d", this.currentTick)));
    }
    
    class PNGSaver implements Runnable {
        private BufferedImage image;
        Path exportPath;
        private String fileName;

        public PNGSaver(BufferedImage image, Path exportPath, String fileName) {
            this.image = image;
            this.exportPath = exportPath;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            try {
                ImageIO.write(image, "png", new File(exportPath + "/snapshots/" + fileName + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //helper for color generation
    private Color generateColor(int seed) {
        Random random = new Random(seed);
        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue = random.nextInt(255);
        return new Color(red, green, blue);
    }
}
