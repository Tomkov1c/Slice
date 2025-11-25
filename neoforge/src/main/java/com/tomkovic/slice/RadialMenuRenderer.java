package com.tomkovic.slice;

import java.lang.reflect.Field;

import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class RadialMenuRenderer {

    private static int itemSize = Constants.DEFAULT_ITEM_SIZE;
    private static int slotSize = Constants.DEFAULT_SLOT_SIZE;
    private static int slotRadius = Constants.DEFAULT_SLOT_RADIUS;
    private static boolean counterclockwise = false;
    private static boolean hideUnusedSlots = false;

    private static boolean hideSlotNumber = false;
    private static boolean hideSlotSprite = false;
    private static boolean[] disabledSlots = new boolean[Constants.SLOT_COUNT];

    private int hoveredSlot = -1;
    private int activeSlot = -1;
    private double mouseStartX = 0;
    private double mouseStartY = 0;
    private static Field selectedField = null;

    static {
        try {
            selectedField = Inventory.class.getDeclaredField("selected");
            selectedField.setAccessible(true);
        } catch (Exception e) {
            Constants.LOG.error("Failed to access Inventory.selected field", e);
        }
    }

    public RadialMenuRenderer() { }

    public static void updateFromConfig() {
        itemSize = Config.CONFIG.itemSize.get();
        slotSize = Config.CONFIG.slotSize.get();
        slotRadius = Config.CONFIG.radialMenuRadius.get();
        counterclockwise = Config.CONFIG.counterclockwiseRotation.get();
        hideUnusedSlots = Config.CONFIG.hideUnusedSlots.get();
        hideSlotNumber = Config.CONFIG.hideSlotNumber.get();
        hideSlotSprite = Config.CONFIG.hideSlotSprite.get();
        
        // Update disabled slots array
        disabledSlots[0] = Config.CONFIG.disableSlot1.get();
        disabledSlots[1] = Config.CONFIG.disableSlot2.get();
        disabledSlots[2] = Config.CONFIG.disableSlot3.get();
        disabledSlots[3] = Config.CONFIG.disableSlot4.get();
        disabledSlots[4] = Config.CONFIG.disableSlot5.get();
        disabledSlots[5] = Config.CONFIG.disableSlot6.get();
        disabledSlots[6] = Config.CONFIG.disableSlot7.get();
        disabledSlots[7] = Config.CONFIG.disableSlot8.get();
        disabledSlots[8] = Config.CONFIG.disableSlot9.get();
    }

    public void onMenuOpen() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.mouseHandler != null) {
            mouseStartX = mc.mouseHandler.xpos();
            mouseStartY = mc.mouseHandler.ypos();
        }
        hoveredSlot = -1;
    }

    public void onMenuClose() {
        if (hoveredSlot >= 0 && hoveredSlot < Constants.SLOT_COUNT && (activeSlot != hoveredSlot)) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player != null) {
                Inventory inventory = player.getInventory();

                try {
                    if (selectedField != null) {
                        selectedField.setInt(inventory, hoveredSlot);
                    }
                } catch (Exception e) {
                    Constants.LOG.error("Failed to set selected slot", e);
                }

                if (mc.getConnection() != null) {
                    mc.getConnection().send(new net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket(hoveredSlot));
                }
            }
        }
        hoveredSlot = -1;
    }

    public void render(GuiGraphics graphics, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        JsonObject json = ResourceHelper.readJsonFromResources(mc.getResourceManager(), "textures/texture_config.json");

        LocalPlayer player = mc.player;
        if (player == null) return;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        double mouseX = mc.mouseHandler.xpos() * screenWidth / mc.getWindow().getScreenWidth() - centerX;
        double mouseY = mc.mouseHandler.ypos() * screenHeight / mc.getWindow().getScreenHeight() - centerY;

        hoveredSlot = getHoveredSlot(mouseX, mouseY);

        Inventory inventory = player.getInventory();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        boolean offsetFromCenter = Utils.getBooleanOrDefault(json, Constants.JSON_OFFSET_FROM_CENTER, false);

        // Count non-empty
        int visibleSlotCount = 0;
        if (hideUnusedSlots) {
            for (int i = 0; i < Constants.SLOT_COUNT; i++) {
                if (!disabledSlots[i] && !inventory.getItem(i).isEmpty()) {
                    visibleSlotCount++;
                }
            }
        } else {
            for (int i = 0; i < Constants.SLOT_COUNT; i++) {
                if (!disabledSlots[i]) {
                    visibleSlotCount++;
                }
            }
        }

        // Render only visible
        int renderedSlots = 0;
        for (int i = 0; i < Constants.SLOT_COUNT; i++) {
            // Skip disabled slots
            if (disabledSlots[i]) {
                continue;
            }
            
            ItemStack stack = inventory.getItem(i);
            
            // Skip empty
            if (hideUnusedSlots && stack.isEmpty()) {
                continue;
            }

            // Calculate slot position
            double angle = Utils.calculateSlotAngle(renderedSlots, visibleSlotCount, counterclockwise);
            
            activeSlot = player.getInventory().getSelectedSlot();
            boolean isActive = (i == activeSlot);
            boolean isHovered = (i == hoveredSlot);

            String xOffsetKey, yOffsetKey;
            if (isActive) {
                xOffsetKey = Constants.JSON_X_OFFSET_ACTIVE;
                yOffsetKey = Constants.JSON_Y_OFFSET_ACTIVE;
            } else if (isHovered) {
                xOffsetKey = Constants.JSON_X_OFFSET_HOVERED;
                yOffsetKey = Constants.JSON_Y_OFFSET_HOVERED;
            } else {
                xOffsetKey = Constants.JSON_X_OFFSET;
                yOffsetKey = Constants.JSON_Y_OFFSET;
            }
            
            int xOffset = Utils.getIntOrDefault(json, xOffsetKey, Utils.getIntOrDefault(json, Constants.JSON_X_OFFSET, 0));
            int yOffset = Utils.getIntOrDefault(json, yOffsetKey, Utils.getIntOrDefault(json, Constants.JSON_Y_OFFSET, 0));
            
            int baseX = centerX + (int) (Math.cos(angle) * slotRadius);
            int baseY = centerY + (int) (Math.sin(angle) * slotRadius);
            
            int x, y;
            if (offsetFromCenter) {
                // Positive offsets move toward center (subtract), negative offsets move outward (add)
                double normalizedX = Math.cos(angle);
                double normalizedY = Math.sin(angle);
                x = baseX - (int) (xOffset * normalizedX);
                y = baseY - (int) (yOffset * normalizedY);
            } else {
                x = baseX + xOffset;
                y = baseY + yOffset;
            }

            if (!hideSlotSprite) {
                renderSlot(graphics, x, y, isActive, isHovered);
            }

            if (!stack.isEmpty()) {
                renderItem(graphics, mc, json, stack, x, y, isActive, isHovered);
            }

            if (!hideSlotNumber) {
                renderSlotNumber(graphics, mc, json, i, x, y, isActive, isHovered);
            }
            
            renderedSlots++;
        }

        bufferSource.endBatch();
    }



    // Private
    private void renderSlot(GuiGraphics graphics, int x, int y, boolean isActive, boolean isHovered) {
        ResourceLocation texture;
        if (isHovered && !isActive) {
            texture = Constants.SLOT_HOVERED_TEXTURE;
        } else if (isActive) {
            texture = Constants.SLOT_ACTIVE_TEXTURE;
        } else {
            texture = Constants.SLOT_TEXTURE;
        }

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            texture,
            x - slotSize / 2,
            y - slotSize / 2,
            0.0f, 0.0f,
            slotSize,
            slotSize,
            slotSize,
            slotSize
        );
    }

    private void renderItem(GuiGraphics graphics, Minecraft mc, JsonObject json, ItemStack stack, int x, int y, boolean isActive, boolean isHovered) {
        String xOffsetKey, yOffsetKey;
        if (isActive) {
            xOffsetKey = Constants.JSON_ITEM_X_OFFSET_ACTIVE;
            yOffsetKey = Constants.JSON_ITEM_Y_OFFSET_ACTIVE;
        } else if (isHovered) {
            xOffsetKey = Constants.JSON_ITEM_X_OFFSET_HOVERED;
            yOffsetKey = Constants.JSON_ITEM_Y_OFFSET_HOVERED;
        } else {
            xOffsetKey = Constants.JSON_ITEM_X_OFFSET;
            yOffsetKey = Constants.JSON_ITEM_Y_OFFSET;
        }
        
        int itemX = x + Utils.getIntOrDefault(json, xOffsetKey, Utils.getIntOrDefault(json, Constants.JSON_ITEM_X_OFFSET, 0));
        int itemY = y + Utils.getIntOrDefault(json, yOffsetKey, Utils.getIntOrDefault(json, Constants.JSON_ITEM_Y_OFFSET, 0));

        graphics.pose().pushMatrix();
        graphics.pose().translate(itemX + 8, itemY + 8);

        float scale = itemSize / 16.0f;
        graphics.pose().scale(scale, scale);

        graphics.pose().translate(-(itemX + 8), -(itemY + 8));

        graphics.renderItem(stack, itemX, itemY);
        graphics.renderItemDecorations(mc.font, stack, itemX, itemY);

        graphics.pose().popMatrix();
    }

    private void renderSlotNumber(GuiGraphics graphics, Minecraft mc, JsonObject json, int slotIndex, 
                                   int x, int y, boolean isActive, boolean isHovered) {
        String slotNum = String.valueOf(slotIndex + 1);
        
        String xOffsetKey, yOffsetKey;
        if (isActive) {
            xOffsetKey = Constants.JSON_SLOT_NUMBER_X_OFFSET_ACTIVE;
            yOffsetKey = Constants.JSON_SLOT_NUMBER_Y_OFFSET_ACTIVE;
        } else if (isHovered) {
            xOffsetKey = Constants.JSON_SLOT_NUMBER_X_OFFSET_HOVERED;
            yOffsetKey = Constants.JSON_SLOT_NUMBER_Y_OFFSET_HOVERED;
        } else {
            xOffsetKey = Constants.JSON_SLOT_NUMBER_X_OFFSET;
            yOffsetKey = Constants.JSON_SLOT_NUMBER_Y_OFFSET;
        }
        
        int xOffset = Utils.getIntOrDefault(json, xOffsetKey, Utils.getIntOrDefault(json, Constants.JSON_SLOT_NUMBER_X_OFFSET, 0));
        int yOffset = Utils.getIntOrDefault(json, yOffsetKey, Utils.getIntOrDefault(json, Constants.JSON_SLOT_NUMBER_Y_OFFSET, 0));
        
        int textX = x - (mc.font.width(slotNum) / 2) + xOffset;
        int textY = y + (itemSize / 2) + yOffset;

        int colorDefault = Utils.parseColor(json, Constants.JSON_SLOT_NUMBER_COLOR, Constants.DEFAULT_SLOT_NUMBER_COLOR);
        int colorHovered = Utils.parseColor(json, Constants.JSON_SLOT_NUMBER_COLOR_HOVERED, Constants.DEFAULT_SLOT_NUMBER_COLOR_HOVERED);
        int colorActive = Utils.parseColor(json, Constants.JSON_SLOT_NUMBER_COLOR_ACTIVE, Constants.DEFAULT_SLOT_NUMBER_COLOR_ACTIVE);

        int color;
        if (isHovered && !isActive) {
            color = colorHovered;
        } else if (isActive) {
            color = colorActive;
        } else {
            color = colorDefault;
        }

        graphics.drawString(mc.font, slotNum, textX, textY, color);
    }

    private int countNonEmptySlots(Inventory inventory) {
        int count = 0;
        for (int i = 0; i < Constants.SLOT_COUNT; i++) {
            if (!disabledSlots[i] && !inventory.getItem(i).isEmpty()) {
                count++;
            }
        }
        return count;
    }

    private int getHoveredSlot(double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return -1;

        double distance = Math.sqrt(mouseX * mouseX + mouseY * mouseY);

        if (distance < Constants.MIN_MOUSE_DISTANCE) return -1;

        double angle = Math.atan2(mouseY, mouseX);
        angle += Math.PI / 2;
        
        // Normalize angle
        if (counterclockwise) {
            angle = -angle;
        }
        angle = Utils.normalizeAngle(angle);

        // Get visible slots
        Inventory inventory = player.getInventory();
        int[] visibleSlots = new int[Constants.SLOT_COUNT];
        int visibleSlotCount = 0;
        
        for (int i = 0; i < Constants.SLOT_COUNT; i++) {
            if (!disabledSlots[i] && (!hideUnusedSlots || !inventory.getItem(i).isEmpty())) {
                visibleSlots[visibleSlotCount++] = i;
            }
        }
        
        if (visibleSlotCount == 0) return -1;

        double slotAngle = Math.PI * 2 / visibleSlotCount;
        int hoveredIndex = (int) Math.round(angle / slotAngle) % visibleSlotCount;
        
        return visibleSlots[hoveredIndex];
    }
}