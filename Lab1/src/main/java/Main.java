import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    private static int counter = 0;
    private static final int operationCount = 10000000;

    private static synchronized void incrementSynchronized() {
        counter++;
    }
    private static synchronized void decrementSynchronized() {
        counter--;
    }

    private static void notSynced() throws InterruptedException {
        Thread incrementThread = new Thread(() -> {
            for(int i = 0; i < operationCount; i++) {
                counter++;
            }
        });

        Thread decrementThread = new Thread(() -> {
            for (int i = 0; i < operationCount; i++) {
                counter--;
            }
        });

        decrementThread.start();
        incrementThread.start();
        decrementThread.join();
        incrementThread.join();
    }

    private static void synced() throws InterruptedException {
        Thread incrementThread = new Thread(() -> {
            for(int i = 0; i < operationCount; i++) {
                incrementSynchronized();
            }
        });

        Thread decrementThread = new Thread(() -> {
            for (int i = 0; i < operationCount; i++) {
                decrementSynchronized();
            }
        });

        incrementThread.start();
        decrementThread.start();
        incrementThread.join();
        decrementThread.join();
    }

    private static void createThreads() {
        while (true) {
            int threadId = counter;
            Thread thread = new Thread(() -> {
                incrementSynchronized();
                // System.out.println(counter);
                try (PrintWriter writer = new PrintWriter(new FileWriter("threads.txt", true))) {
                    writer.println(threadId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true) {
                    continue;
                }
            });
            thread.start();
        }
    }

    public static void twoThreads() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        notSynced();
        long enlapsedTime = System.currentTimeMillis() - startTime;
        System.out.println(counter);
        System.out.println("Time: " + enlapsedTime + "ms");

        counter = 0;
        startTime = System.currentTimeMillis();
        synced();
        enlapsedTime = System.currentTimeMillis() - startTime;
        System.out.println(counter);
        System.out.println("Time: " + enlapsedTime + "ms");
    }

    public static void main(String[] args) throws InterruptedException {
        twoThreads();
        createThreads();
    }
}