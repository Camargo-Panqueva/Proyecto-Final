package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;

/**
 * Represents a logger that can be used to log messages to the console and a log file.
 * <p>
 * This class represents a logger that can be used to log messages to the console and a log file.
 * It provides a structure for logging messages with different levels of severity.
 * The logger can log messages with the levels of success, info, warning, and error.
 * The logger can log messages to the console, a log file, or both.
 * </p>
 */
public abstract class Logger {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss:SSS");
    private static final String LOG_FILE = "Quoripoob.log";
    private static LogTarget defaultTarget = LogTarget.BOTH;

    private static boolean isNewSession = true;
    private static boolean isNewLogFile = true;

    static {
        if (new File(LOG_FILE).exists()) {
            isNewLogFile = false;
        }
    }

    /**
     * Logs a success message to the console and a log file.
     * <p>
     * This method logs a success message to the {@link Logger#defaultTarget} log target.
     * The message is logged with the level of success.
     * </p>
     *
     * @param message the message to log.
     */
    public static void success(String message) {
        success(message, Logger.defaultTarget);
    }

    /**
     * Logs a success message to the console and a log file with the given target.
     * <p>
     * This method logs a success message to the given target.
     * The message is logged with the level of success.
     * </p>
     *
     * @param message the message to log.
     * @param target  the target to log the message to.
     */
    public static void success(String message, LogTarget target) {
        log(LogLevel.SUCCESS, target, message);
    }

    /**
     * Logs an info message to the console and a log file.
     * <p>
     * This method logs an info message to the {@link Logger#defaultTarget} log target.
     * The message is logged with the level of info.
     * </p>
     *
     * @param message the message to log.
     */
    public static void info(String message) {
        info(message, Logger.defaultTarget);
    }

    /**
     * Logs an info message to the console and a log file with the given target.
     * <p>
     * This method logs an info message to the given target.
     * The message is logged with the level of info.
     * </p>
     *
     * @param message the message to log.
     * @param target  the target to log the message to.
     */
    public static void info(String message, LogTarget target) {
        log(LogLevel.INFO, target, message);
    }

    /**
     * Logs a warning message to the console and a log file.
     * <p>
     * This method logs a warning message to the {@link Logger#defaultTarget} log target.
     * The message is logged with the level of warning.
     * </p>
     *
     * @param message the message to log.
     */
    public static void warning(String message) {
        warning(message, Logger.defaultTarget);
    }

    /**
     * Logs a warning message to the console and a log file with the given target.
     * <p>
     * This method logs a warning message to the given target.
     * The message is logged with the level of warning.
     * </p>
     *
     * @param message the message to log.
     * @param target  the target to log the message to.
     */
    public static void warning(String message, LogTarget target) {
        log(LogLevel.WARNING, target, message);
    }

    /**
     * Logs an error message to the console and a log file.
     * <p>
     * This method logs an error message to the {@link Logger#defaultTarget} log target.
     * The message is logged with the level of error.
     * </p>
     *
     * @param message the message to log.
     */
    public static void error(String message) {
        error(message, Logger.defaultTarget);
    }

    /**
     * Logs an error message to the console and a log file with the given target.
     * <p>
     * This method logs an error message to the given target.
     * The message is logged with the level of error.
     * </p>
     *
     * @param message the message to log.
     * @param target  the target to log the message to.
     */
    public static void error(String message, LogTarget target) {
        log(LogLevel.ERROR, target, message);
    }

    /**
     * Logs a message to the console with the given level.
     * <p>
     * This method logs a message to the console with the given level.
     * The message is logged with the given level of severity.
     * The message is not logged to a log file.
     * </p>
     *
     * @param level the level of the message.
     * @param log   the message to log.
     */
    private static void logToConsole(LogLevel level, String log) {
        System.out.printf("%s%s%s %s\n", level.color, addIndentation(log), ConsoleColor.RESET, getCallerHyperlink());
    }

    /**
     * Logs a message to a log file.
     * <p>
     * This method logs a message to a log file.
     * The message is logged to a log file.
     * </p>
     *
     * @param log the message to log.
     */
    private static void logToFile(String log) {

        if (isNewSession) {
            log = "\n" + log;
            isNewSession = false;
        }

        if (isNewLogFile) {
            log = log.stripLeading();
        }

        try (OutputStream out = new FileOutputStream(LOG_FILE, true)) {
            out.write((log + "\n").getBytes());
        } catch (Exception e) {
            error("Failed to write to log file: " + e.getMessage(), LogTarget.CONSOLE);
        }
    }

    /**
     * Logs a message with the given level and target.
     * <p>
     * This method logs a message with the given level and target.
     * The message is logged with the given level of severity.
     * The message is logged to the given target.
     * </p>
     *
     * @param level   the level of the message.
     * @param target  the target to log the message to.
     * @param message the message to log.
     */
    private static void log(LogLevel level, LogTarget target, String message) {
        String dateTime = java.time.LocalDateTime.now().format(formatter);
        String boilerplate = String.format("[ %s ][ %s ]", dateTime, level.name().charAt(0));
        String log = addIndentation(String.format("%s %s", boilerplate, message));

        if (target == LogTarget.CONSOLE || target == LogTarget.BOTH) {
            logToConsole(level, log);
        }

        if (target == LogTarget.FILE || target == LogTarget.BOTH) {
            logToFile(log);
        }
    }

    /**
     * Sets the default target to log a message to.
     * <p>
     * This method sets the default target to log a message to.
     * The default target is used when a target is not specified.
     * </p>
     *
     * @param target the default target to log a message to.
     */
    public static void setDefaultTarget(LogTarget target) {
        defaultTarget = target;
    }

    /**
     * Adds indentation to a message.
     * <p>
     * This method adds indentation to a message.
     * The indentation is added to the message.
     * </p>
     *
     * @param message the message to add indentation to.
     * @return the message with indentation added.
     */
    private static String addIndentation(String message) {
        int spaces = message.lastIndexOf("]") + 2;
        return message.replace("\n", "\n" + " ".repeat(spaces));
    }

    /**
     * Gets the hyperlink of the caller of the logger.
     * <p>
     * This method gets the hyperlink of the caller of the logger.
     * The hyperlink is the class name, file name, and line number of the caller.
     * </p>
     *
     * @return the hyperlink of the caller of the logger.
     */
    private static String getCallerHyperlink() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (int i = 2; i < stackTraceElements.length; i++) {
            if (!stackTraceElements[i].getClassName().equals(Logger.class.getName())) {
                return String.format(
                        "%s%s%s.(%s:%d)%s",
                        ConsoleColor.DIMMED,
                        ConsoleColor.WHITE,
                        stackTraceElements[i].getClassName(),
                        stackTraceElements[i].getFileName(),
                        stackTraceElements[i].getLineNumber(),
                        ConsoleColor.RESET
                );
            }
        }
        return "";
    }

    /**
     * Represents the target to log a message to.
     * <p>
     * This enum represents the target to log a message to.
     * It provides a structure for managing the targets to log a message to.
     * </p>
     */
    public enum LogTarget {
        /**
         * The target to log a message to the console.
         */
        CONSOLE,
        /**
         * The target to log a message to a log file.
         */
        FILE,
        /**
         * The target to log a message to both the console and a log file.
         */
        BOTH
    }

    /**
     * Represents the level of severity of a log message.
     * <p>
     * This enum represents the level of severity of a log message.
     * It provides a structure for managing the levels of severity of a log message.
     * </p>
     */
    public enum LogLevel {
        /**
         * The level of success of a log message.
         */
        SUCCESS(ConsoleColor.GREEN),
        /**
         * The level of info of a log message.
         */
        INFO(ConsoleColor.BLUE),
        /**
         * The level of warning of a log message.
         */
        WARNING(ConsoleColor.YELLOW),
        /**
         * The level of error of a log message.
         */
        ERROR(ConsoleColor.RED);

        private final ConsoleColor color;

        LogLevel(ConsoleColor color) {
            this.color = color;
        }
    }

    /**
     * Represents the color of a console.
     * <p>
     * This enum represents the color of a console.
     * It provides a structure for managing the colors of a console.
     * </p>
     */
    private enum ConsoleColor {
        RESET("\u001B[0m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        WHITE("\u001B[37m"),
        DIMMED("\u001B[2m");

        private final String color;

        ConsoleColor(String color) {
            this.color = color;
        }

        public String toString() {
            return this.color;
        }
    }
}
