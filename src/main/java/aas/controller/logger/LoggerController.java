package aas.controller.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class LoggerController {
	
	private static final Logger EVENT_LOGGER = Logger.getLogger(LoggerType.EVENT.name());
	
	public LoggerController() {
		initEventLogger();
	}
	
	private void initEventLogger() {
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new EventLoggerFormat());
		LoggerController.EVENT_LOGGER.setUseParentHandlers(false);
		LoggerController.EVENT_LOGGER.addHandler(handler);
	}
	
}
