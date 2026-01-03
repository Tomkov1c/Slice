package com.tomkovic.slice;

import com.google.gson.JsonObject;

public class Utils {



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