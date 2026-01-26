package com.tomkovic.slice.helpers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.Identifier;

import com.tomkovic.slice.GlobalConfig;
import com.tomkovic.slice.classes.SlotPosition;

public class RadialMenuHelper {

    public static boolean[] getDisabledSlots() {
        boolean[] disabledSlots = new boolean[Inventory.getSelectionSize()];

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

    public static int[] getVisibleSlots(Inventory inventory) {
        int count = 0;
        int[] temp = new int[Inventory.getSelectionSize()];

        boolean[] disabledSlots = getDisabledSlots();

        for (int i = 0; i < Inventory.getSelectionSize(); i++) {
            if (!disabledSlots[i] && (!GlobalConfig.HIDE_UNUSED_SLOTS || !inventory.getItem(i).isEmpty())) temp[count++] = i;
        }

        int[] result = new int[count];
        System.arraycopy(temp, 0, result, 0, count);

        return result;
    }

    public static SlotPosition[] calculateSlotPositions(int[] visibleSlots, int centerX, int centerY) {

        SlotPosition[] positions = new SlotPosition[visibleSlots.length];

        double startDeg = ((GlobalConfig.START_ANGLE % 360) + 360) % 360;
        double endDeg   = ((GlobalConfig.END_ANGLE   % 360) + 360) % 360;

        boolean fullCircle = startDeg == endDeg;

        double startRad = Math.toRadians(startDeg) - Math.PI / 2;
        double endRad   = Math.toRadians(endDeg)   - Math.PI / 2;

        double angleRange;
        if (fullCircle)
            angleRange = Math.PI * 2;
        else {
            angleRange = endRad - startRad;

            while (angleRange <= 0) { angleRange += 2 * Math.PI;}
            while (angleRange > 2 * Math.PI) { angleRange -= 2 * Math.PI;}
        }

        double angleStep = fullCircle ? angleRange / visibleSlots.length :
                              (visibleSlots.length > 1 ? angleRange / (visibleSlots.length - 1) : 0);

        for (int i = 0; i < visibleSlots.length; i++) {
            int slotIndex = visibleSlots[i];
            int displayIndex = GlobalConfig.REVERSE_ROTATION ? (visibleSlots.length - 1 - i) : i;
            double angle = (fullCircle || visibleSlots.length > 1) ? startRad + displayIndex * angleStep : startRad + angleRange / 2;

            int baseX = centerX + (int)(Math.cos(angle) * GlobalConfig.MENU_RADIUS);
            int baseY = centerY + (int)(Math.sin(angle) * GlobalConfig.MENU_RADIUS);

            positions[i] = new SlotPosition(slotIndex, angle, baseX, baseY);
        }

        return positions;
    }

    public static int getHoveredSlot(double mx, double my, SlotPosition[] slotPositions) {
        if (slotPositions == null || slotPositions.length == 0) return -1;

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

    public static Identifier guiTexture(String name) {
        return Identifier.fromNamespaceAndPath("slice", "textures/gui/" + name + ".png");
    }

    public static boolean isCursorInSelectionArea(double cursorX, double cursorY) {
        double distanceFromCenter = Math.sqrt(cursorX * cursorX + cursorY * cursorY);

        double innerBoundary = GlobalConfig.MENU_RADIUS - GlobalConfig.INNER_DEADZONE;
        double outerBoundary = GlobalConfig.MENU_RADIUS + GlobalConfig.OUTER_DEADZONE;

        return distanceFromCenter >= innerBoundary && distanceFromCenter <= outerBoundary;
    }
}
