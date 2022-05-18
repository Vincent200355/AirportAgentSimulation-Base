module dhbw.sose2022.softwareengineering.AASBase {
	
	requires java.base;
	requires java.logging;
	requires java.desktop;
	
	requires transitive org.apache.logging.log4j;
	
	requires org.apache.commons.lang3;
	requires org.apache.logging.log4j.core;
	
	requires simple.xml;
    requires gson;

    exports dhbw.sose2022.softwareengineering.airportagentsim.simulation.api;
    exports dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config;
    exports dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry;
    exports dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.logging;
    exports dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.plugin;
    exports dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation;
    exports dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity;
    exports dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message;

    opens dhbw.sose2022.softwareengineering.airportagentsim.simulation.configuration to gson;
}
