package dhbw.sose2022.softwareengineering.airportagentsim.simulation.logging;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.logging.PluginLogger;

public final class Log4jPluginLogger implements PluginLogger {
	
	private final Logger log4jLogger;
	private final Marker log4jMarker;
	
	public Log4jPluginLogger(Logger log4jLogger, Marker log4jMarker) {
		this.log4jLogger = log4jLogger;
		this.log4jMarker = log4jMarker;
	}
	
	public Logger getLog4jLogger() {
		return this.log4jLogger;
	}
	
	public Marker getLog4jMarker() {
		return this.log4jMarker;
	}
	
	@Override
	public boolean isTraceEnabled() {
		return this.log4jLogger.isTraceEnabled(this.log4jMarker);
	}
	
	@Override
	public void trace(Message message) {
		this.log4jLogger.trace(this.log4jMarker, message);
	}
	
	@Override
	public void trace(Message message, Throwable throwable) {
		this.log4jLogger.trace(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void trace(MessageSupplier messageSupplier) {
		this.log4jLogger.trace(this.log4jMarker, messageSupplier);
	}
	
	@Override
	public void trace(MessageSupplier messageSupplier, Throwable throwable) {
		this.log4jLogger.trace(this.log4jMarker, messageSupplier, throwable);
	}
	
	@Override
	public void trace(CharSequence message) {
		this.log4jLogger.trace(this.log4jMarker, message);
	}
	
	@Override
	public void trace(CharSequence message, Throwable throwable) {
		this.log4jLogger.trace(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void trace(Object message) {
		this.log4jLogger.trace(this.log4jMarker, message);
	}
	
	@Override
	public void trace(Object message, Throwable throwable) {
		this.log4jLogger.trace(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void trace(String message) {
		this.log4jLogger.trace(this.log4jMarker, message);
	}
	
	@Override
	public void trace(String message, Object... params) {
		this.log4jLogger.trace(this.log4jMarker, message, params);
	}
	
	@Override
	public void trace(String message, Supplier<?>... paramSuppliers) {
		this.log4jLogger.trace(this.log4jMarker, message, paramSuppliers);
	}
	
	@Override
	public void trace(String message, Throwable throwable) {
		this.log4jLogger.trace(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void trace(Supplier<?> messageSupplier) {
		this.log4jLogger.trace(this.log4jMarker, messageSupplier);
	}
	
	@Override
	public void trace(Supplier<?> messageSupplier, Throwable throwable) {
		this.log4jLogger.trace(this.log4jMarker, messageSupplier, throwable);
	}
	
	@Override
	public void trace(String message, Object p0) {
		this.log4jLogger.trace(this.log4jMarker, message, p0);
	}
	
	@Override
	public void trace(String message, Object p0, Object p1) {
		this.log4jLogger.trace(this.log4jMarker, message, p0, p1);
	}
	
	@Override
	public void trace(String message, Object p0, Object p1, Object p2) {
		this.log4jLogger.trace(this.log4jMarker, message, p0, p1, p2);
	}
	
	@Override
	public void trace(String message, Object p0, Object p1, Object p2, Object p3) {
		this.log4jLogger.trace(this.log4jMarker, message, p0, p1, p2, p3);
	}
	
	@Override
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		this.log4jLogger.trace(this.log4jMarker, message, p0, p1, p2, p3, p4);
	}
	
	@Override
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		this.log4jLogger.trace(this.log4jMarker, message, p0, p1, p2, p3, p4, p5);
	}
	
	@Override
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		this.log4jLogger.trace(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6);
	}
	
	@Override
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
		this.log4jLogger.trace(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7);
	}
	
	@Override
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
		this.log4jLogger.trace(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}
	
	@Override
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
		this.log4jLogger.trace(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}
	
	@Override
	public boolean isDebugEnabled() {
		return this.log4jLogger.isDebugEnabled(this.log4jMarker);
	}
	
	@Override
	public void debug(Message message) {
		this.log4jLogger.debug(this.log4jMarker, message);
	}
	
	@Override
	public void debug(Message message, Throwable throwable) {
		this.log4jLogger.debug(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void debug(MessageSupplier messageSupplier) {
		this.log4jLogger.debug(this.log4jMarker, messageSupplier);
	}
	
	@Override
	public void debug(MessageSupplier messageSupplier, Throwable throwable) {
		this.log4jLogger.debug(this.log4jMarker, messageSupplier, throwable);
	}
	
	@Override
	public void debug(CharSequence message) {
		this.log4jLogger.debug(this.log4jMarker, message);
	}
	
	@Override
	public void debug(CharSequence message, Throwable throwable) {
		this.log4jLogger.debug(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void debug(Object message) {
		this.log4jLogger.debug(this.log4jMarker, message);
	}
	
	@Override
	public void debug(Object message, Throwable throwable) {
		this.log4jLogger.debug(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void debug(String message) {
		this.log4jLogger.debug(this.log4jMarker, message);
	}
	
	@Override
	public void debug(String message, Object... params) {
		this.log4jLogger.debug(this.log4jMarker, message, params);
	}
	
	@Override
	public void debug(String message, Supplier<?>... paramSuppliers) {
		this.log4jLogger.debug(this.log4jMarker, message, paramSuppliers);
	}
	
	@Override
	public void debug(String message, Throwable throwable) {
		this.log4jLogger.debug(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void debug(Supplier<?> messageSupplier) {
		this.log4jLogger.debug(this.log4jMarker, messageSupplier);
	}
	
	@Override
	public void debug(Supplier<?> messageSupplier, Throwable throwable) {
		this.log4jLogger.debug(this.log4jMarker, messageSupplier, throwable);
	}
	
	@Override
	public void debug(String message, Object p0) {
		this.log4jLogger.debug(this.log4jMarker, message, p0);
	}
	
	@Override
	public void debug(String message, Object p0, Object p1) {
		this.log4jLogger.debug(this.log4jMarker, message, p0, p1);
	}
	
	@Override
	public void debug(String message, Object p0, Object p1, Object p2) {
		this.log4jLogger.debug(this.log4jMarker, message, p0, p1, p2);
	}
	
	@Override
	public void debug(String message, Object p0, Object p1, Object p2, Object p3) {
		this.log4jLogger.debug(this.log4jMarker, message, p0, p1, p2, p3);
	}
	
	@Override
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		this.log4jLogger.debug(this.log4jMarker, message, p0, p1, p2, p3, p4);
	}
	
	@Override
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		this.log4jLogger.debug(this.log4jMarker, message, p0, p1, p2, p3, p4, p5);
	}
	
	@Override
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		this.log4jLogger.debug(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6);
	}
	
	@Override
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
		this.log4jLogger.debug(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7);
	}
	
	@Override
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
		this.log4jLogger.debug(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}
	
	@Override
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
		this.log4jLogger.debug(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}
	
	@Override
	public boolean isInfoEnabled() {
		return this.log4jLogger.isInfoEnabled(this.log4jMarker);
	}
	
	@Override
	public void info(Message message) {
		this.log4jLogger.info(this.log4jMarker, message);
	}
	
	@Override
	public void info(Message message, Throwable throwable) {
		this.log4jLogger.info(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void info(MessageSupplier messageSupplier) {
		this.log4jLogger.info(this.log4jMarker, messageSupplier);
	}
	
	@Override
	public void info(MessageSupplier messageSupplier, Throwable throwable) {
		this.log4jLogger.info(this.log4jMarker, messageSupplier, throwable);
	}
	
	@Override
	public void info(CharSequence message) {
		this.log4jLogger.info(this.log4jMarker, message);
	}
	
	@Override
	public void info(CharSequence message, Throwable throwable) {
		this.log4jLogger.info(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void info(Object message) {
		this.log4jLogger.info(this.log4jMarker, message);
	}
	
	@Override
	public void info(Object message, Throwable throwable) {
		this.log4jLogger.info(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void info(String message) {
		this.log4jLogger.info(this.log4jMarker, message);
	}
	
	@Override
	public void info(String message, Object... params) {
		this.log4jLogger.info(this.log4jMarker, message, params);
	}
	
	@Override
	public void info(String message, Supplier<?>... paramSuppliers) {
		this.log4jLogger.info(this.log4jMarker, message, paramSuppliers);
	}
	
	@Override
	public void info(String message, Throwable throwable) {
		this.log4jLogger.info(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void info(Supplier<?> messageSupplier) {
		this.log4jLogger.info(this.log4jMarker, messageSupplier);
	}
	
	@Override
	public void info(Supplier<?> messageSupplier, Throwable throwable) {
		this.log4jLogger.info(this.log4jMarker, messageSupplier, throwable);
	}
	
	@Override
	public void info(String message, Object p0) {
		this.log4jLogger.info(this.log4jMarker, message, p0);
	}
	
	@Override
	public void info(String message, Object p0, Object p1) {
		this.log4jLogger.info(this.log4jMarker, message, p0, p1);
	}
	
	@Override
	public void info(String message, Object p0, Object p1, Object p2) {
		this.log4jLogger.info(this.log4jMarker, message, p0, p1, p2);
	}
	
	@Override
	public void info(String message, Object p0, Object p1, Object p2, Object p3) {
		this.log4jLogger.info(this.log4jMarker, message, p0, p1, p2, p3);
	}
	
	@Override
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		this.log4jLogger.info(this.log4jMarker, message, p0, p1, p2, p3, p4);
	}
	
	@Override
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		this.log4jLogger.info(this.log4jMarker, message, p0, p1, p2, p3, p4, p5);
	}
	
	@Override
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		this.log4jLogger.info(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6);
	}
	
	@Override
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
		this.log4jLogger.info(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7);
	}
	
	@Override
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
		this.log4jLogger.info(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}
	
	@Override
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
		this.log4jLogger.info(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}
	
	@Override
	public boolean isWarnEnabled() {
		return this.log4jLogger.isWarnEnabled(this.log4jMarker);
	}
	
	@Override
	public void warn(Message message) {
		this.log4jLogger.warn(this.log4jMarker, message);
	}
	
	@Override
	public void warn(Message message, Throwable throwable) {
		this.log4jLogger.warn(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void warn(MessageSupplier messageSupplier) {
		this.log4jLogger.warn(this.log4jMarker, messageSupplier);
	}
	
	@Override
	public void warn(MessageSupplier messageSupplier, Throwable throwable) {
		this.log4jLogger.warn(this.log4jMarker, messageSupplier, throwable);
	}
	
	@Override
	public void warn(CharSequence message) {
		this.log4jLogger.warn(this.log4jMarker, message);
	}
	
	@Override
	public void warn(CharSequence message, Throwable throwable) {
		this.log4jLogger.warn(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void warn(Object message) {
		this.log4jLogger.warn(this.log4jMarker, message);
	}
	
	@Override
	public void warn(Object message, Throwable throwable) {
		this.log4jLogger.warn(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void warn(String message) {
		this.log4jLogger.warn(this.log4jMarker, message);
	}
	
	@Override
	public void warn(String message, Object... params) {
		this.log4jLogger.warn(this.log4jMarker, message, params);
	}
	
	@Override
	public void warn(String message, Supplier<?>... paramSuppliers) {
		this.log4jLogger.warn(this.log4jMarker, message, paramSuppliers);
	}
	
	@Override
	public void warn(String message, Throwable throwable) {
		this.log4jLogger.warn(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void warn(Supplier<?> messageSupplier) {
		this.log4jLogger.warn(this.log4jMarker, messageSupplier);
	}
	
	@Override
	public void warn(Supplier<?> messageSupplier, Throwable throwable) {
		this.log4jLogger.warn(this.log4jMarker, messageSupplier, throwable);
	}
	
	@Override
	public void warn(String message, Object p0) {
		this.log4jLogger.warn(this.log4jMarker, message, p0);
	}
	
	@Override
	public void warn(String message, Object p0, Object p1) {
		this.log4jLogger.warn(this.log4jMarker, message, p0, p1);
	}
	
	@Override
	public void warn(String message, Object p0, Object p1, Object p2) {
		this.log4jLogger.warn(this.log4jMarker, message, p0, p1, p2);
	}
	
	@Override
	public void warn(String message, Object p0, Object p1, Object p2, Object p3) {
		this.log4jLogger.warn(this.log4jMarker, message, p0, p1, p2, p3);
	}
	
	@Override
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		this.log4jLogger.warn(this.log4jMarker, message, p0, p1, p2, p3, p4);
	}
	
	@Override
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		this.log4jLogger.warn(this.log4jMarker, message, p0, p1, p2, p3, p4, p5);
	}
	
	@Override
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		this.log4jLogger.warn(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6);
	}
	
	@Override
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
		this.log4jLogger.warn(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7);
	}
	
	@Override
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
		this.log4jLogger.warn(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}
	
	@Override
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
		this.log4jLogger.warn(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}
	
	@Override
	public boolean isErrorEnabled() {
		return this.log4jLogger.isErrorEnabled(this.log4jMarker);
	}
	
	@Override
	public void error(Message message) {
		this.log4jLogger.error(this.log4jMarker, message);
	}
	
	@Override
	public void error(Message message, Throwable throwable) {
		this.log4jLogger.error(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void error(MessageSupplier messageSupplier) {
		this.log4jLogger.error(this.log4jMarker, messageSupplier);
	}
	
	@Override
	public void error(MessageSupplier messageSupplier, Throwable throwable) {
		this.log4jLogger.error(this.log4jMarker, messageSupplier, throwable);
	}
	
	@Override
	public void error(CharSequence message) {
		this.log4jLogger.error(this.log4jMarker, message);
	}
	
	@Override
	public void error(CharSequence message, Throwable throwable) {
		this.log4jLogger.error(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void error(Object message) {
		this.log4jLogger.error(this.log4jMarker, message);
	}
	
	@Override
	public void error(Object message, Throwable throwable) {
		this.log4jLogger.error(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void error(String message) {
		this.log4jLogger.error(this.log4jMarker, message);
	}
	
	@Override
	public void error(String message, Object... params) {
		this.log4jLogger.error(this.log4jMarker, message, params);
	}
	
	@Override
	public void error(String message, Supplier<?>... paramSuppliers) {
		this.log4jLogger.error(this.log4jMarker, message, paramSuppliers);
	}
	
	@Override
	public void error(String message, Throwable throwable) {
		this.log4jLogger.error(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void error(Supplier<?> messageSupplier) {
		this.log4jLogger.error(this.log4jMarker, messageSupplier);
	}
	
	@Override
	public void error(Supplier<?> messageSupplier, Throwable throwable) {
		this.log4jLogger.error(this.log4jMarker, messageSupplier, throwable);
	}
	
	@Override
	public void error(String message, Object p0) {
		this.log4jLogger.error(this.log4jMarker, message, p0);
	}
	
	@Override
	public void error(String message, Object p0, Object p1) {
		this.log4jLogger.error(this.log4jMarker, message, p0, p1);
	}
	
	@Override
	public void error(String message, Object p0, Object p1, Object p2) {
		this.log4jLogger.error(this.log4jMarker, message, p0, p1, p2);
	}
	
	@Override
	public void error(String message, Object p0, Object p1, Object p2, Object p3) {
		this.log4jLogger.error(this.log4jMarker, message, p0, p1, p2, p3);
	}
	
	@Override
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		this.log4jLogger.error(this.log4jMarker, message, p0, p1, p2, p3, p4);
	}
	
	@Override
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		this.log4jLogger.error(this.log4jMarker, message, p0, p1, p2, p3, p4, p5);
	}
	
	@Override
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		this.log4jLogger.error(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6);
	}
	
	@Override
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
		this.log4jLogger.error(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7);
	}
	
	@Override
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
		this.log4jLogger.error(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}
	
	@Override
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
		this.log4jLogger.error(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}
	
	@Override
	public boolean isFatalEnabled() {
		return this.log4jLogger.isFatalEnabled(this.log4jMarker);
	}
	
	@Override
	public void fatal(Message message) {
		this.log4jLogger.fatal(this.log4jMarker, message);
	}
	
	@Override
	public void fatal(Message message, Throwable throwable) {
		this.log4jLogger.fatal(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void fatal(MessageSupplier messageSupplier) {
		this.log4jLogger.fatal(this.log4jMarker, messageSupplier);
	}
	
	@Override
	public void fatal(MessageSupplier messageSupplier, Throwable throwable) {
		this.log4jLogger.fatal(this.log4jMarker, messageSupplier, throwable);
	}
	
	@Override
	public void fatal(CharSequence message) {
		this.log4jLogger.fatal(this.log4jMarker, message);
	}
	
	@Override
	public void fatal(CharSequence message, Throwable throwable) {
		this.log4jLogger.fatal(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void fatal(Object message) {
		this.log4jLogger.fatal(this.log4jMarker, message);
	}
	
	@Override
	public void fatal(Object message, Throwable throwable) {
		this.log4jLogger.fatal(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void fatal(String message) {
		this.log4jLogger.fatal(this.log4jMarker, message);
	}
	
	@Override
	public void fatal(String message, Object... params) {
		this.log4jLogger.fatal(this.log4jMarker, message, params);
	}
	
	@Override
	public void fatal(String message, Supplier<?>... paramSuppliers) {
		this.log4jLogger.fatal(this.log4jMarker, message, paramSuppliers);
	}
	
	@Override
	public void fatal(String message, Throwable throwable) {
		this.log4jLogger.fatal(this.log4jMarker, message, throwable);
	}
	
	@Override
	public void fatal(Supplier<?> messageSupplier) {
		this.log4jLogger.fatal(this.log4jMarker, messageSupplier);
	}
	
	@Override
	public void fatal(Supplier<?> messageSupplier, Throwable throwable) {
		this.log4jLogger.fatal(this.log4jMarker, messageSupplier, throwable);
	}
	
	@Override
	public void fatal(String message, Object p0) {
		this.log4jLogger.fatal(this.log4jMarker, message, p0);
	}
	
	@Override
	public void fatal(String message, Object p0, Object p1) {
		this.log4jLogger.fatal(this.log4jMarker, message, p0, p1);
	}
	
	@Override
	public void fatal(String message, Object p0, Object p1, Object p2) {
		this.log4jLogger.fatal(this.log4jMarker, message, p0, p1, p2);
	}
	
	@Override
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3) {
		this.log4jLogger.fatal(this.log4jMarker, message, p0, p1, p2, p3);
	}
	
	@Override
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		this.log4jLogger.fatal(this.log4jMarker, message, p0, p1, p2, p3, p4);
	}
	
	@Override
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		this.log4jLogger.fatal(this.log4jMarker, message, p0, p1, p2, p3, p4, p5);
	}
	
	@Override
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		this.log4jLogger.fatal(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6);
	}
	
	@Override
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
		this.log4jLogger.fatal(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7);
	}
	
	@Override
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
		this.log4jLogger.fatal(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
	}
	
	@Override
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
		this.log4jLogger.fatal(this.log4jMarker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
	}
	
}
