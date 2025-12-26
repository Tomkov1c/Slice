package com.tomkovic.slice;

import com.google.gson.JsonObject;

public class Utils {


    public static int parseColor(JsonObject json, String key, int fallback) {
        if (!json.has(key)) return fallback;
        String colorStr = json.get(key).getAsString().replace("#", "");
        try {
            if (colorStr.length() == 6) {
                int r = Integer.parseInt(colorStr.substring(0, 2), 16);
                int g = Integer.parseInt(colorStr.substring(2, 4), 16);
                int b = Integer.parseInt(colorStr.substring(4, 6), 16);
                return (0xFF << 24) | (r << 16) | (g << 8) | b;
            } else if (colorStr.length() == 8) {
                int r = Integer.parseInt(colorStr.substring(0, 2), 16);
                int g = Integer.parseInt(colorStr.substring(2, 4), 16);
                int b = Integer.parseInt(colorStr.substring(4, 6), 16);
                int a = Integer.parseInt(colorStr.substring(6, 8), 16);
                return (a << 24) | (r << 16) | (g << 8) | b;
            }
            return fallback;
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static int getIntOrDefault(JsonObject json, String key, int fallback) {
        if (!json.has(key)) return fallback;
        try {
            return json.get(key).getAsInt();
        } catch (Exception e) {
            return fallback;
        }
    }

    public static double calculateSlotAngle(int slotIndex, int totalSlots, boolean counterclockwise) {
        double angleMultiplier = counterclockwise ? -1.0 : 1.0;
        return (Math.PI * 2 * slotIndex / totalSlots) * angleMultiplier - Math.PI / 2;
    }

    public static double normalizeAngle(double angle) {
        while (angle < 0) angle += Math.PI * 2;
        while (angle >= Math.PI * 2) angle -= Math.PI * 2;
        return angle;
    }

    public static boolean getBooleanOrDefault(JsonObject json, String key, boolean defaultValue) {
        if (json != null && json.has(key)) {
            return json.get(key).getAsBoolean();
        }
        return defaultValue;
    }

    private Utils() { }
}