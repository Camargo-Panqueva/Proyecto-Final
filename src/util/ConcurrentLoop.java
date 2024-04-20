package util;

public final class ConcurrentLoop implements Runnable {

    private final VoidFunction function;
    private final String threadName;
    private final int targetTPS;
    private ConsumerFunction<Integer> tickConsumer;
    private Thread thread;
    private boolean isLooping;
    private int currentTPS;

    public ConcurrentLoop(VoidFunction function, int targetTPS, String threadName) {
        this.function = function;
        this.targetTPS = targetTPS;
        this.threadName = threadName;

        this.isLooping = false;
        this.currentTPS = 0;
    }

    @Override
    public void run() {
        final double NS_PER_SECOND = 1_000_000_000;
        final double NS_PER_FRAME = NS_PER_SECOND / this.targetTPS;

        long tickRef = System.nanoTime();
        long countRef = System.nanoTime();

        double time;
        double deltaTPS = 0;

        while (this.isLooping) {
            final long start = System.nanoTime();

            time = start - tickRef;
            tickRef = start;

            deltaTPS += time / NS_PER_FRAME;

            while (deltaTPS >= 1) {
                this.tick();
                deltaTPS--;
            }

            if (this.tickConsumer != null && System.nanoTime() - countRef > NS_PER_SECOND) {
                tickConsumer.run(this.currentTPS);
                currentTPS = 0;
                countRef = System.nanoTime();
            }
        }
    }

    private void tick() {
        this.function.run();
        this.currentTPS++;
    }

    public void start() {
        this.isLooping = true;

        this.thread = new Thread(this, this.threadName + " thread");
        this.thread.start();
    }

    public void stop() {
        this.isLooping = false;

        try {
            this.thread.join();
        } catch (InterruptedException e) {
            System.out.println("Could not join thread " + this.threadName);
        }
    }

    public void setTickConsumer(ConsumerFunction<Integer> tickConsumer) {
        this.tickConsumer = tickConsumer;
    }
}
