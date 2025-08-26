package renderer;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Static helper for running pixel/ray tasks concurrently with a fixed number of workers.
 */
public final class Proccesses {
    private static final int MAX_WORKERS =
            Math.max(1, Runtime.getRuntime().availableProcessors());

    private static final ExecutorService pool =
            Executors.newFixedThreadPool(MAX_WORKERS, new ThreadFactory() {
                private final AtomicInteger count = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "Proccess-" + count.getAndIncrement());
                    t.setDaemon(true);
                    return t;
                }
            });

    /** Prevent instantiation */
    private Proccesses() {}

    @FunctionalInterface
    public interface QuadConsumer<A,B,C,D> {
        void accept(A a, B b, C c, D d) throws Exception;
    }

    /**
     * Run a 4-argument void task for every pixel in an nX by nY grid.
     */
    public static void run(int nX, int nY, QuadConsumer<Integer,Integer,Integer,Integer> task) {
        CountDownLatch latch = new CountDownLatch(nX * nY);
        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                final int ii = i, jj = j;
                pool.execute(() -> {
                    try {
                        task.accept(nX, nY, jj, ii);
                    } catch (Exception e) {
                        e.printStackTrace(); // or handle properly
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }
        try {
            latch.await(); // wait for all to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Gracefully shut down the thread pool (call at end of program). */
    public static void shutdown() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
