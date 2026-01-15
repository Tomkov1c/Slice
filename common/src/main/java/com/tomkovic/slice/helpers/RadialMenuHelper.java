package com.tomkovic.slice.helpers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

import com.tomkovic.slice.Constants;
import com.tomkovic.slice.GlobalConfig;
import com.tomkovic.slice.classes.SlotPosition;
import com.tomkovic.slice.classes.TexturePackCustomValues;

public class RadialMenuHelper {

    public static TexturePackCustomValues textureConfig = new TexturePackCustomValues();

    public static boolean[] getDisabledSlots() {
        boolean[] disabledSlots = new boolean[Constants.SLOT_COUNT];

        disabledSlots[0] = GlobalConfig.DISABLE_SLOT_1;
        disabledSlots[1] = GlobalConfig.DISABLE_SLOT_2;
        disabledSlots[2] = GlobalConfig.DISABLE_SLOT_3;
        disabledSlots[3] = GlobalConfig.DISABLE_SLOT_4;
        disabledSlots[4] = GlobalConfig.DISABLE_SLOT_5;
        disabledSlots[5] = GlobalConfig.DISABLE_SLOT_6;
        disabledSlots[6] = GlobalConfig.DISABLE_SLOT_7;
        disabledSlots[7] = GlobalConfig.DISABLE_SLOT_8;
        disabledSlots[8] = GlobalConfig.DISABLE_SLOT_9;

        return disabledSlots;
    }
    
    public static int[] getVisibleSlots(Inventory inventory, boolean hideUnusedSlots) {
        int count = 0;
        int[] temp = new int[Constants.SLOT_COUNT];

        boolean[] disabledSlots = getDisabledSlots();

        for (int i = 0; i < Constants.SLOT_COUNT; i++) {
            if (!disabledSlots[i] && (!hideUnusedSlots || !inventory.getItem(i).isEmpty())) temp[count++] = i;
        }

        int[] result = new int[count];
        System.arraycopy(temp, 0, result, 0, count);

        return result;
    }

    public static SlotPosition[] calculateSlotPositions(int[] visibleSlots, int centerX, int centerY, int startAngle, int endAngle, boolean counterclockwise, int slotRadius) {
        SlotPosition[] positions = new SlotPosition[visibleSlots.length];
        
        boolean fullCircle = (startAngle == endAngle) || ((startAngle == 0 && endAngle == 360) || ( startAngle == 360 && endAngle == 0));

        double startRad = Math.toRadians(startAngle) - Math.PI / 2;
        double endRad = Math.toRadians(endAngle) - Math.PI / 2;
        double angleRange = fullCircle ? Math.PI * 2 : (endRad - startRad + 2 * Math.PI) % (2 * Math.PI);
        double angleStep = fullCircle ? angleRange / visibleSlots.length : 
                              (visibleSlots.length > 1 ? angleRange / (visibleSlots.length - 1) : 0);

        for (int i = 0; i < visibleSlots.length; i++) {
            int slotIndex = visibleSlots[i];
            int displayIndex = counterclockwise ? (visibleSlots.length - 1 - i) : i;
            double angle = (fullCircle || visibleSlots.length > 1) ? startRad + displayIndex * angleStep : startRad + angleRange / 2;
            
            int baseX = centerX + (int)(Math.cos(angle) * slotRadius);
            int baseY = centerY + (int)(Math.sin(angle) * slotRadius);
            
            positions[i] = new SlotPosition(slotIndex, angle, baseX, baseY);
        }
        
        return positions;
    }

    public static int getHoveredSlot(double mx, double my, SlotPosition[] slotPositions, int slotRadius, int innerDeadzoneRadius, int outerDeadzoneRadius) {
        if (slotPositions == null || slotPositions.length == 0) return -1;

        double dist = Math.sqrt(mx * mx + my * my);
        double innerBoundary = slotRadius - innerDeadzoneRadius;
        double outerBoundary = slotRadius + outerDeadzoneRadius;

        if (dist < innerBoundary || dist > outerBoundary) return -1;

        double mouseAngle = (Math.atan2(my, mx) + 2 * Math.PI) % (2 * Math.PI);

        int bestSlot = -1;
        double bestAngularDistance = Double.MAX_VALUE;

        for (SlotPosition pos : slotPositions) {
            double slotAngle = (pos.angle + 2 * Math.PI) % (2 * Math.PI);

            double angularDistance = Math.abs(mouseAngle - slotAngle);
            if (angularDistance > Math.PI) angularDistance = 2 * Math.PI - angularDistance;

            if (angularDistance < bestAngularDistance) {
                bestAngularDistance = angularDistance;
                bestSlot = pos.slotIndex;
            }
        }

        double maxAcceptable = (Math.PI * 2 / slotPositions.length) / 2 + 0.2;
        if (bestAngularDistance > maxAcceptable) return -1;

        return bestSlot;
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

    public static ResourceLocation guiTexture(String name) {
        return ResourceLocation.fromNamespaceAndPath("slice", "textures/gui/" + name + ".png");
    }

    public static boolean isCursorInSelectionArea(double cursorX, double cursorY, int radialMenuRadius, int innerDeadzoneRadius, int outerDeadzoneRadius) {
        double distanceFromCenter = Math.sqrt(cursorX * cursorX + cursorY * cursorY);
        
        double innerBoundary = radialMenuRadius - innerDeadzoneRadius;
        double outerBoundary = radialMenuRadius + outerDeadzoneRadius;
        
        return distanceFromCenter >= innerBoundary && distanceFromCenter <= outerBoundary;
    }
}
