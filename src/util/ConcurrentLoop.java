package util;

/**
 * Represents a concurrent loop that can be used to run a function at a target TPS.
 * <p>
 * This class represents a concurrent loop that can be used to run a function at a target TPS.
 * It provides a structure for running a function in a loop at a target TPS inside his own
 * thread.
 * </p>
 */
public final class ConcurrentLoop implements Runnable {

    /**
     * The function to run in the loop.
     */
    private final VoidFunction function;

    /**
     * The name of the thread.
     */
    private final String threadName;

    /**
     * The target TPS of the loop.
     */
    private final double targetTPS;

    /**
     * The thread of the loop.
     */
    private Thread thread;

    private boolean isLooping;
    private double counterTPS;
    private double currentTPS;

    /**
     * Creates a new ConcurrentLoop with the given function, target TPS, and thread name.
     *
     * @param function   the function to run in the loop.
     * @param targetTPS  the target TPS of the loop.
     * @param threadName the name of the thread.
     */
    public ConcurrentLoop(VoidFunction function, double targetTPS, String threadName) {
        this.function = function;
        this.targetTPS = targetTPS;
        this.threadName = threadName;

        this.isLooping = false;
        this.currentTPS = 0;
    }

    /**
     * Runs the loop.
     * <p>
     * This method runs the loop and executes the function at the target TPS.
     * The loop runs until the isLooping flag is set to false.
     * The loop calculates the delta time between frames and executes the function
     * at the target TPS.
     * </p>
     */
    @Override
    public void run() {
        final double NS_PER_SECOND = 1_000_000_000;
        final double NS_PER_FRAME = NS_PER_SECOND / this.targetTPS;

        long tickRef = System.nanoTime();
        long countRef = System.nanoTime();

        double time;
        double deltaTPS = 0;

        double measurementScale = 1 / Math.min(1, this.targetTPS);

        while (this.isLooping) {
            final long start = System.nanoTime();

            time = start - tickRef;
            tickRef = start;

            deltaTPS += time / NS_PER_FRAME;

            while (deltaTPS >= 1) {
                this.tick();
                deltaTPS--;
            }

            if (System.nanoTime() - countRef > NS_PER_SECOND * measurementScale){
                this.currentTPS = this.counterTPS / measurementScale;
                this.counterTPS = 0;
                countRef = System.nanoTime();
            }
        }
    }

    /**
     * Executes the function.
     * <p>
     * This method executes the function and increments the counterTPS.
     * It's a private and self-implemented method by the class.
     * </p>
     */
    private void tick() {
        this.function.run();
        this.counterTPS++;
    }

    /**
     * Starts the loop.
     * <p>
     * This method starts the loop and the thread.
     * </p>
     */
    public void start() {
        this.isLooping = true;

        this.thread = new Thread(this, this.threadName + " thread");
        this.thread.start();
    }

    /**
     * Stops the loop.
     * <p>
     * This method stops the loop and the thread.
     * </p>
     */
    public void stop() {
        this.isLooping = false;

        try {
            this.thread.join();
        } catch (InterruptedException e) {
            System.out.println("Could not join thread " + this.threadName);
        }
    }

    /**
     * Gets the current TPS of the loop.
     *
     * @return the current TPS of the loop.
     */
    public double getCurrentTPS() {
        return currentTPS;
    }

    /**
     * Gets the thread of the loop.
     *
     * @return the thread of the loop.
     */
    public Thread getThread() {
        return thread;
    }
}
