package com.tomkovic.slice;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class RadialMenuRenderer {
    private static final int SLOT_COUNT = 9;

    private static int itemSize = 16;
    private static int slotSize = 32;
    private static int slotRadius = 80;
    private static boolean counterclockwise = false;
    private static boolean hideUnusedSlots = false;

    // Texture locations
    private static final ResourceLocation SLOT_TEXTURE = ResourceLocation.fromNamespaceAndPath("slice", "textures/gui/radial_slot.png");
    private static final ResourceLocation SLOT_HOVERED_TEXTURE = ResourceLocation.fromNamespaceAndPath("slice", "textures/gui/radial_slot_hovered.png");
    private static final ResourceLocation SLOT_ACTIVE_TEXTURE = ResourceLocation.fromNamespaceAndPath("slice", "textures/gui/radial_slot_active.png");


    private int hoveredSlot = -1;
    private double mouseStartX = 0;
    private double mouseStartY = 0;
    private static Field selectedField = null;

    static {
        try {
            selectedField = Inventory.class.getDeclaredField("selected");
            selectedField.setAccessible(true);
        } catch (Exception e) {
            Slice.LOGGER.error("Failed to access Inventory.selected field", e);
        }
    }

    public RadialMenuRenderer() { }

    public static void updateFromConfig() {
        itemSize = Config.CONFIG.itemSize.get();
        slotSize = Config.CONFIG.slotSize.get();
        slotRadius = Config.CONFIG.radialMenuRadius.get();
        //counterclockwise = Config.COUNTERCLOCKWISE_ROTATION.get();
        hideUnusedSlots = Config.CONFIG.hideUnusedSlots.get();
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
        if (hoveredSlot >= 0 && hoveredSlot < SLOT_COUNT) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player != null) {
                Inventory inventory = player.getInventory();

                try {
                    if (selectedField != null) {
                        selectedField.setInt(inventory, hoveredSlot);
                    }
                } catch (Exception e) {
                    Slice.LOGGER.error("Failed to set selected slot", e);
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

        // Count non-empty
        int visibleSlotCount = SLOT_COUNT;
        if (hideUnusedSlots) {
            visibleSlotCount = 0;
            for (int i = 0; i < SLOT_COUNT; i++) {
                if (!inventory.getItem(i).isEmpty()) {
                    visibleSlotCount++;
                }
            }
        }

        // Render only visible slots
        int renderedSlots = 0;
        for (int i = 0; i < SLOT_COUNT; i++) {
            ItemStack stack = inventory.getItem(i);
            
            // Skip if enabled
            if (hideUnusedSlots && stack.isEmpty()) {
                continue;
            }

            // Calculate clockwise-ness
            double angleMultiplier = counterclockwise ? -1.0 : 1.0;
            double angle = (Math.PI * 2 * renderedSlots / visibleSlotCount) * angleMultiplier - Math.PI / 2;
            
            int x = centerX + (int) (Math.cos(angle) * slotRadius);
            int y = centerY + (int) (Math.sin(angle) * slotRadius);

            int selectedSlot = player.getInventory().getSelectedSlot();
            boolean isActive = (i == selectedSlot);
            boolean isHovered = (i == hoveredSlot);

            ResourceLocation texture;
            if (isHovered) {
                texture = SLOT_HOVERED_TEXTURE;
            } else if (isActive) {
                texture = SLOT_ACTIVE_TEXTURE;
            } else {
                texture = SLOT_TEXTURE;
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

            // Draw item scaled and centered
            if (!stack.isEmpty()) {
                int itemX = x - 8;
                int itemY = y - 9; // TODO: Less hardcoded

                graphics.pose().pushMatrix();

                graphics.pose().translate(itemX + 8, itemY + 8);

                float scale = itemSize / 16.0f;
                graphics.pose().scale(scale, scale);

                graphics.pose().translate(-(itemX + 8), -(itemY + 8));

                graphics.renderItem(stack, itemX, itemY);
                graphics.renderItemDecorations(mc.font, stack, itemX, itemY);

                graphics.pose().popMatrix();
            }

            // Draw slot number
            String slotNum = String.valueOf(i + 1);
            int textX = x - mc.font.width(slotNum) / 2;
            int textY = y + itemSize / 2 + 12;
            graphics.drawString(mc.font, slotNum, textX, textY, isHovered ? 0xFFFFFF00 : 0xFFFFFFFF);
            
            renderedSlots++;
        }

        bufferSource.endBatch();
    }

    private int getHoveredSlot(double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return -1;

        double distance = Math.sqrt(mouseX * mouseX + mouseY * mouseY);

        if (distance < 20) return -1;

        double angle = Math.atan2(mouseY, mouseX);
        angle += Math.PI / 2;
        
        // Angle
        if (counterclockwise) {
            angle = -angle;
            if (angle < 0) angle += Math.PI * 2;
        } else {
            if (angle < 0) angle += Math.PI * 2;
        }

        // Visaul slots
        Inventory inventory = player.getInventory();
        int[] visibleSlots = new int[SLOT_COUNT];
        int visibleSlotCount = 0;
        
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!hideUnusedSlots || !inventory.getItem(i).isEmpty()) {
                visibleSlots[visibleSlotCount++] = i;
            }
        }
        
        if (visibleSlotCount == 0) return -1;

        double slotAngle = Math.PI * 2 / visibleSlotCount;
        int hoveredIndex = (int) Math.round(angle / slotAngle) % visibleSlotCount;
        
        return visibleSlots[hoveredIndex];
    }
}