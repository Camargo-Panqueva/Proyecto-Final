package util;

/**
 * Represents a timeout that can be used to run a function after a target time.
 * <p>
 * This class represents a timeout that can be used to run a function after a target time.
 * It provides a structure for running a function after a target time inside his own
 * thread.
 * </p>
 */
public final class Timeout implements Runnable {

    private final int timeOut;
    private final VoidFunction function;
    private Thread thread;

    /**
     * Creates a new Timeout with the given function and target time.
     *
     * @param function the function to run after the target time.
     * @param millis   the target time in milliseconds.
     */
    public Timeout(VoidFunction function, int millis) {
        this.function = function;
        this.timeOut = millis;
    }

    /**
     * Starts a new Timeout with the given function and target time.
     *
     * @param function the function to run after the target time.
     * @param millis   the target time in milliseconds.
     * @return the new Timeout.
     */
    public static Timeout startTimeout(VoidFunction function, int millis) {
        Timeout timeOut = new Timeout(function, millis);
        timeOut.start();

        return timeOut;
    }

    /**
     * Starts the timeout.
     * <p>
     * This method starts the timeout and runs the function after the target time.
     * </p>
     */
    public void start() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * Stops the timeout.
     * <p>
     * This method stops the timeout and interrupts the thread.
     * </p>
     */
    public void stop() {
        this.thread.interrupt();
    }

    /**
     * Runs the timeout.
     */
    @Override
    public void run() {
        try {
            Thread.sleep(this.timeOut);
            this.function.run();
        } catch (InterruptedException e) {
            Logger.error("Timeout interrupted: " + e.getMessage());
        }

        this.thread = null;
    }
}
