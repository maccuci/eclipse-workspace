package me.tony.commons.core.data.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommonLogger {
	
	private static final String LOG_PREFIX_FORMAT = "[%s]";
	
	private final Logger handle;
	private final CommonLogger parent;
	private final boolean debugMode;
	private String prefix;
	private String logPrefix;
	
	public CommonLogger(Logger handle, String prefix, boolean debugMode) {
		this.handle = handle;
		this.parent = null;
		this.prefix = prefix;
		this.debugMode = debugMode;

		this.logPrefix = buildPrefix(new StringBuilder());
	}

	public CommonLogger(CommonLogger parent, String prefix) {
		this.handle = parent.handle;
		this.parent = parent;
		this.prefix = prefix;
		this.debugMode = parent.hasDebugMode();

		this.logPrefix = buildPrefix(new StringBuilder());
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
		this.logPrefix = buildPrefix(new StringBuilder());
	}

	private String buildPrefix(StringBuilder builder) {
		if (prefix != null) {
			builder.insert(0, String.format(LOG_PREFIX_FORMAT, prefix));
		}
		return parent == null ? builder.length() < 1 ? "" : builder + " " : parent.buildPrefix(builder);
	}
	
	public void log(Level level, String format, Object... args) {
		log(level, null, String.format(format, args));
	}

	public void log(Level level, String message) {
		log(level, null, message);
	}
	
	public void log(Level level, Throwable ex, String format, Object... args) {
		log(level, ex, String.format(format, args));
	}
	
	public void log(Level level, Throwable throwable, String message) {
		handle.log(level, logPrefix + message, throwable);
	}
	
	public boolean hasDebugMode() {
		return debugMode;
	}
}
