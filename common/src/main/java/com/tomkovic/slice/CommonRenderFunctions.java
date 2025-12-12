package com.tomkovic.slice;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import java.lang.reflect.Field;



public class CommonRenderFunctions {

    public CommonRenderFunctions() {

    }

    public static int[] getVisibleSlots(Inventory inventory, boolean hideUnusedSlots, boolean[] disabledSlots) {
        int[] temp = new int[Constants.SLOT_COUNT];
        int count = 0;
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
            double angle = (fullCircle || visibleSlots.length > 1) ? 
                startRad + displayIndex * angleStep : 
                startRad + angleRange / 2;
            
            int baseX = centerX + (int)(Math.cos(angle) * slotRadius);
            int baseY = centerY + (int)(Math.sin(angle) * slotRadius);
            
            positions[i] = new SlotPosition(slotIndex, angle, baseX, baseY);
        }
        
        return positions;
    }

    public static int getHoveredSlot(double mx, double my, SlotPosition[] slotPositions, int slotRadius, int innerDeadzoneRadius, int outerDeadzoneRadius) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.isPaused()) return -1;

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

    @SuppressWarnings("null")
    public static void selectSlot(int index) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) return;

        Field selectedField;

        try {
            selectedField = Inventory.class.getDeclaredField("selected");
            selectedField.setAccessible(true);

            selectedField.setInt(player.getInventory(), index);
        }catch(Exception ex) {
            Constants.LOG.error("Failed to access Inventory.selected field", ex);
            return;
        }

        if (mc.getConnection() != null) mc.getConnection().send( new net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket(index) );

    }
}
