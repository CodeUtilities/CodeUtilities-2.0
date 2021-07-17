package io.github.codeutilities.util.networking;

import java.util.ArrayList;
import java.util.List;

public class TPSUtil {

    private static long lastTpsTimestamp = 0;
    private static List<Long> measurements = new ArrayList<>();

    public static void addTick() {
        synchronized (measurements) {
            measurements.add(System.currentTimeMillis());

            if (measurements.size() > 15) measurements.remove(0);
        }
    }

    public static void reset() {
        synchronized (measurements) {
            measurements.clear();
        }
    }

    public static double calculateTps() {
        long current = measurements.get(measurements.size() - 1);
        long previous = measurements.get(0);

        double secDiff = Math.max((current - previous) / (1000.0 * (measurements.size() - 1)), 1.0);

        return 20.0f / secDiff;
    }
}
