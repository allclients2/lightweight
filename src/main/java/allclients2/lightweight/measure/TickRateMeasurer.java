package allclients2.lightweight.measure;

import allclients2.lightweight.eventbus.EventBus;
import allclients2.lightweight.eventbus.events.ClientTickEvent;

import java.util.LinkedList;
import java.util.Queue;

public class TickRateMeasurer {
    private static float lastTickDelta = 0;
    private static long lastTickTimeNS = System.nanoTime();
    private static final Queue<Long> tickTimeQueue = new LinkedList<>();
    private static final long SAMPLE_NS = 5L * 1_000_000_000L;

    public TickRateMeasurer() {
        startMeasure();
    }

    public static float getLastTickRate() {
        return (1.0f / lastTickDelta); // Inverse lastTickDelta
    }

    public static float getAverageTickRate() { // From last 5 seconds
        removeOldTicks();

        if (tickTimeQueue.isEmpty()) {
            return 0;
        }

        long last = tickTimeQueue.peek();
        float sum = 0;
        for (long time : tickTimeQueue) {
            sum += ((float) (time - last)) * 1e-9f; // The first tick won't affect the average.
            last = time;
        }

        final float average = (sum / (tickTimeQueue.size() - 1));

        return 1.0f / average;
    }

    private static void startMeasure() {
        EventBus.subscribe(ClientTickEvent.class, evt -> {
            final long currentTime = System.nanoTime();
            lastTickDelta = ((float) (currentTime - lastTickTimeNS)) * 1e-9f;
            lastTickTimeNS = currentTime;

            tickTimeQueue.add(currentTime);

            removeOldTicks();
        });
    }

    private static void removeOldTicks() {
        final long time = System.nanoTime();
        while (!tickTimeQueue.isEmpty() && tickTimeQueue.peek() + SAMPLE_NS < time) {
            tickTimeQueue.poll(); // Head is last tick
        }
    }
}
