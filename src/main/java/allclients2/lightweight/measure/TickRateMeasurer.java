package allclients2.lightweight.measure;

import allclients2.lightweight.eventbus.EventBus;
import allclients2.lightweight.eventbus.Subscription;
import allclients2.lightweight.eventbus.events.ClientTickEvent;

import java.util.LinkedList;
import java.util.Queue;

public class TickRateMeasurer {
    private static final boolean shouldSampleOnlyWhenUsing = true;

    private static boolean sampling = true;
    private static float lastTickDelta = 0;
    private static long lastTickTimeNS = System.nanoTime();
    private static final Queue<Long> tickTimeQueue = new LinkedList<>();
    private static Subscription<ClientTickEvent> clientTickSubscription;
    private static final long SAMPLE_NS = 5L * 1_000_000_000L;

    public TickRateMeasurer() {
        this(true);
    }

    public TickRateMeasurer(boolean shouldSample) {
        if (shouldSample) {
            startSampling();
        }
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

    private static void startSampling() {
        if (clientTickSubscription == null) {
            tickTimeQueue.clear();
            lastTickTimeNS = System.nanoTime();
            clientTickSubscription = EventBus.subscribe(ClientTickEvent.class, evt -> {
                final long currentTime = System.nanoTime();
                lastTickDelta = ((float) (currentTime - lastTickTimeNS)) * 1e-9f;
                lastTickTimeNS = currentTime;

                tickTimeQueue.add(currentTime);
                removeOldTicks();
            });
        }
    }

    private static void stopSampling() {
        if (clientTickSubscription != null) {
            EventBus.unsubscribe(clientTickSubscription);
            clientTickSubscription = null;
        }
    }

    public static void toggleUse(boolean isUsing) {
        if (shouldSampleOnlyWhenUsing) {
            if (isUsing && !sampling) {
                startSampling();
            } else if (sampling) {
                stopSampling();
            }
            sampling = isUsing;
        }
    }

    private static void removeOldTicks() {
        final long time = System.nanoTime();
        while (!tickTimeQueue.isEmpty() && tickTimeQueue.peek() + SAMPLE_NS < time) {
            tickTimeQueue.poll(); // Head is last tick
        }
    }
}
