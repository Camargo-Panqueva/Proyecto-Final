package util;

import java.time.format.DateTimeFormatter;

public abstract class Logger {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss:SSS");

    public static void logSuccess(String message) {
        log(LogLevel.SUCCESS, message);
    }

    public static void logInfo(String message) {
        log(LogLevel.INFO, message);
    }

    public static void logWarning(String message) {
        log(LogLevel.WARNING, message);
    }

    public static void logError(String message) {
        log(LogLevel.ERROR, message);
    }

    private static void log(LogLevel level, String message) {
        String dateTime = java.time.LocalDateTime.now().format(formatter);
        String boilerplate = String.format("[ %s ][ %s ]", dateTime, level.name().charAt(0));
        System.out.printf("%s%s %s%s\n", level.color, boilerplate, addIndentation(message, boilerplate), ConsoleColors.RESET);
    }

    private static String addIndentation(String message, String boilerplate) {
        int spaces = boilerplate.lastIndexOf("]") + 2;
        return message.replace("\n", "\n" + " ".repeat(spaces));
    }

    public enum LogLevel {
        SUCCESS(ConsoleColors.GREEN),
        INFO(ConsoleColors.BLUE),
        WARNING(ConsoleColors.YELLOW),
        ERROR(ConsoleColors.RED);

        private final ConsoleColors color;

        LogLevel(ConsoleColors color) {
            this.color = color;
        }
    }

    public enum ConsoleColors {
        RESET("\u001B[0m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m");

        private final String color;

        ConsoleColors(String color) {
            this.color = color;
        }

        public String toString() {
            return this.color;
        }
    }
}
