package aas.controller.logger;

public enum LoggerType {
	
	DEFAULT("DEFAULT", 0),
	ERROR("ERROR", 1),
	DATA("DATA", 2),
	EVENT("EVENT", 3),
	DEBUG("DEBUG", 4);
	
	private LoggerType(final String name, final int ordinal) {}
	
}
