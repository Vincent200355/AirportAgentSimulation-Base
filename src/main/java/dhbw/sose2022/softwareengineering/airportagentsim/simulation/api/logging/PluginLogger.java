package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.logging;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;

public interface PluginLogger {
	
	public boolean isTraceEnabled();
	
	public void trace(Message message);
	
	public void trace(Message message, Throwable throwable);
	
	public void trace(MessageSupplier messageSupplier);
	
	public void trace(MessageSupplier messageSupplier, Throwable throwable);
	
	public void trace(CharSequence message);
	
	public void trace(CharSequence message, Throwable throwable);
	
	public void trace(Object message);
	
	public void trace(Object message, Throwable throwable);
	
	public void trace(String message);
	
	public void trace(String message, Object... params);
	
	public void trace(String message, Supplier<?>... paramSuppliers);
	
	public void trace(String message, Throwable throwable);
	
	public void trace(Supplier<?> messageSupplier);
	
	public void trace(Supplier<?> messageSupplier, Throwable throwable);
	
	public void trace(String message, Object p0);
	
	public void trace(String message, Object p0, Object p1);
	
	public void trace(String message, Object p0, Object p1, Object p2);
	
	public void trace(String message, Object p0, Object p1, Object p2, Object p3);
	
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4);
	
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5);
	
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6);
	
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7);
	
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8);
	
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9);
	
	public boolean isDebugEnabled();
	
	public void debug(Message message);
	
	public void debug(Message message, Throwable throwable);
	
	public void debug(MessageSupplier messageSupplier);
	
	public void debug(MessageSupplier messageSupplier, Throwable throwable);
	
	public void debug(CharSequence message);
	
	public void debug(CharSequence message, Throwable throwable);
	
	public void debug(Object message);
	
	public void debug(Object message, Throwable throwable);
	
	public void debug(String message);
	
	public void debug(String message, Object... params);
	
	public void debug(String message, Supplier<?>... paramSuppliers);
	
	public void debug(String message, Throwable throwable);
	
	public void debug(Supplier<?> messageSupplier);
	
	public void debug(Supplier<?> messageSupplier, Throwable throwable);
	
	public void debug(String message, Object p0);
	
	public void debug(String message, Object p0, Object p1);
	
	public void debug(String message, Object p0, Object p1, Object p2);
	
	public void debug(String message, Object p0, Object p1, Object p2, Object p3);
	
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4);
	
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5);
	
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6);
	
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7);
	
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8);
	
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9);
	
	public boolean isInfoEnabled();
	
	public void info(Message message);
	
	public void info(Message message, Throwable throwable);
	
	public void info(MessageSupplier messageSupplier);
	
	public void info(MessageSupplier messageSupplier, Throwable throwable);
	
	public void info(CharSequence message);
	
	public void info(CharSequence message, Throwable throwable);
	
	public void info(Object message);
	
	public void info(Object message, Throwable throwable);
	
	public void info(String message);
	
	public void info(String message, Object... params);
	
	public void info(String message, Supplier<?>... paramSuppliers);
	
	public void info(String message, Throwable throwable);
	
	public void info(Supplier<?> messageSupplier);
	
	public void info(Supplier<?> messageSupplier, Throwable throwable);
	
	public void info(String message, Object p0);
	
	public void info(String message, Object p0, Object p1);
	
	public void info(String message, Object p0, Object p1, Object p2);
	
	public void info(String message, Object p0, Object p1, Object p2, Object p3);
	
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4);
	
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5);
	
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6);
	
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7);
	
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8);
	
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9);
	
	public boolean isWarnEnabled();
	
	public void warn(Message message);
	
	public void warn(Message message, Throwable throwable);
	
	public void warn(MessageSupplier messageSupplier);
	
	public void warn(MessageSupplier messageSupplier, Throwable throwable);
	
	public void warn(CharSequence message);
	
	public void warn(CharSequence message, Throwable throwable);
	
	public void warn(Object message);
	
	public void warn(Object message, Throwable throwable);
	
	public void warn(String message);
	
	public void warn(String message, Object... params);
	
	public void warn(String message, Supplier<?>... paramSuppliers);
	
	public void warn(String message, Throwable throwable);
	
	public void warn(Supplier<?> messageSupplier);
	
	public void warn(Supplier<?> messageSupplier, Throwable throwable);
	
	public void warn(String message, Object p0);
	
	public void warn(String message, Object p0, Object p1);
	
	public void warn(String message, Object p0, Object p1, Object p2);
	
	public void warn(String message, Object p0, Object p1, Object p2, Object p3);
	
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4);
	
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5);
	
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6);
	
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7);
	
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8);
	
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9);
	
	public boolean isErrorEnabled();
	
	public void error(Message message);
	
	public void error(Message message, Throwable throwable);
	
	public void error(MessageSupplier messageSupplier);
	
	public void error(MessageSupplier messageSupplier, Throwable throwable);
	
	public void error(CharSequence message);
	
	public void error(CharSequence message, Throwable throwable);
	
	public void error(Object message);
	
	public void error(Object message, Throwable throwable);
	
	public void error(String message);
	
	public void error(String message, Object... params);
	
	public void error(String message, Supplier<?>... paramSuppliers);
	
	public void error(String message, Throwable throwable);
	
	public void error(Supplier<?> messageSupplier);
	
	public void error(Supplier<?> messageSupplier, Throwable throwable);
	
	public void error(String message, Object p0);
	
	public void error(String message, Object p0, Object p1);
	
	public void error(String message, Object p0, Object p1, Object p2);
	
	public void error(String message, Object p0, Object p1, Object p2, Object p3);
	
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4);
	
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5);
	
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6);
	
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7);
	
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8);
	
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9);
	
	public boolean isFatalEnabled();
	
	public void fatal(Message message);
	
	public void fatal(Message message, Throwable throwable);
	
	public void fatal(MessageSupplier messageSupplier);
	
	public void fatal(MessageSupplier messageSupplier, Throwable throwable);
	
	public void fatal(CharSequence message);
	
	public void fatal(CharSequence message, Throwable throwable);
	
	public void fatal(Object message);
	
	public void fatal(Object message, Throwable throwable);
	
	public void fatal(String message);
	
	public void fatal(String message, Object... params);
	
	public void fatal(String message, Supplier<?>... paramSuppliers);
	
	public void fatal(String message, Throwable throwable);
	
	public void fatal(Supplier<?> messageSupplier);
	
	public void fatal(Supplier<?> messageSupplier, Throwable throwable);
	
	public void fatal(String message, Object p0);
	
	public void fatal(String message, Object p0, Object p1);
	
	public void fatal(String message, Object p0, Object p1, Object p2);
	
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3);
	
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4);
	
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5);
	
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6);
	
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7);
	
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8);
	
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9);
	
}
