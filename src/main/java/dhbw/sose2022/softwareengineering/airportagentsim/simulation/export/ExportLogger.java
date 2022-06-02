package dhbw.sose2022.softwareengineering.airportagentsim.simulation.export;

public abstract sealed class ExportLogger permits AirportSimExporter {
    /**
     * Method to be called when the Simulation is initialized and ready to run.
     */
    public abstract void afterInit();

    /**
     * Method to be called after each iteration of the Simulation.
     */
    public abstract void afterTick();
}
