package aas.controller.logger;

import java.util.logging.LogRecord;
import java.util.logging.Formatter;

public class EventLoggerFormat extends Formatter {
	
	@Override
	public String format(LogRecord record) {
		return record.getMessage() + "\n";
	}
	
}
