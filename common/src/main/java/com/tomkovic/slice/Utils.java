package com.tomkovic.slice;

import com.google.gson.JsonObject;

public class Utils {

    public static int parseColor(JsonObject json, String key, int fallback) {
        if (!json.has(key)) return fallback;
        String colorStr = json.get(key).getAsString().replace("#", "");
        try {
            long value = Long.parseLong(colorStr, 16);
            if (colorStr.length() <= 6) {
                
                value |= 0xFF000000L;
            }
            return (int) value;
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
    

    private Utils() { }
}
