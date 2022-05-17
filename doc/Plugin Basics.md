# Airport Agent Simulation: Plugin Basics

## General structure
Every plugin to the Airport Agent Simulation is a single `.jar` archive. This archive may contain arbitrary classes and resources, but must define a plugin manifest. The plugin manifest is a text file, which contains the basic information needed to load the plugin. The plugin manifest must be called `plugin.txt` and must be located at the outermost level of the `.jar` file's entry hierarchy.
The plugin manifest consists of key-value-pairs. Every line denotes a single key-value-pair. Empty lines and lines prefixed by `#` are ignored.
The plugin manifest must define values for the following keys:
- `id`: A unique ID to identify the plugins. This ID is used if another plugin wishes to use the defining plugin as a dependency.
- `name`: A display name for the plugin. This name is used if the plugin's name is presented to the user. It is also prepended to log messages created by the plugin.
- `authors`: A non-empty, comma-separated list of author names. 
- `entrypoint`: The fully qualified name of the plugin's base class.

The plugin manifest may define values for the following keys:
- `dependencies`: A comma-separated list of plugins which must be present for the plugin to be able to function. If a plugin present in this list is not available, the plugin will not be activated. Furthermore, it is guaranteed that all dependencies have already been activated once the plugin itself is activated.

The plugin manifest must not define values for any other keys.

## The Plugin interface
Implementors of plugins are required to define a class which implements the `dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.plugin.Plugin` interface. This class is the plugin's base class. Note that implementing this class is not sufficient to cause the plugin to be loaded.
The application will create an instance of the base class to load the plugin. If the plugin can be activated, the application will invoke the `loadEntityTypes` method on that instance to activate the plugin. Plugins must not interact with any Airport Agent Simulation API before they are activated, except for logging-releated API.

## Airport Agent Simulation API
The main API class for plugin interaction is the `dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.AirportAgentSimulation` class. As this class is expected to declare further operations in the future, the reader is encouraged to confer to the Javadoc of this class for information on the plugin API.
