package com.tomkovic.slice;

import java.lang.reflect.Field;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class RadialMenuRenderer {
    private static int itemSize = Config.CONFIG.itemSize.getDefault();
    private static int slotSize = Config.CONFIG.slotSize.getDefault();
    private static int slotRadius = Config.CONFIG.radialMenuRadius.getDefault();
    private static boolean counterclockwise = Config.CONFIG.counterclockwiseRotation.getDefault();
    private static boolean hideUnusedSlots = Config.CONFIG.hideUnusedSlots.getDefault();
    private static boolean hideSlotNumber = Config.CONFIG.hideSlotNumber.getDefault();
    private static boolean hideSlotSprite = Config.CONFIG.hideSlotSprite.getDefault();
    private static boolean[] disabledSlots = new boolean[Constants.SLOT_COUNT];
    private static int startAngle = Config.CONFIG.startAngle.getDefault();
    private static int endAngle = Config.CONFIG.endAngle.getDefault();
    private static int backgroundDarkenOpacity = Config.CONFIG.backgroundDarkenOpacity.getDefault();
    private static int innerDeadzoneRadius = Config.CONFIG.innerDeadzone.getDefault();
    private static int outerDeadzoneRadius = Config.CONFIG.outerDeadzone.getDefault();
    public static boolean clickToSelect = Config.CONFIG.clickToSelect.getDefault();
    private int hoveredSlot = -1;
    private int activeSlot = -1;
    private double mouseStartX = 0;
    private double mouseStartY = 0;
    private static Field selectedField = null;

    public RadialMenuRenderer() {
        try {
            selectedField = Inventory.class.getDeclaredField("selected");
            selectedField.setAccessible(true);
        } catch (Exception e) {
            Constants.LOG.error("Failed to access Inventory.selected field", e);
        }
    }
    
    public static void updateFromConfig() {
        itemSize = Config.CONFIG.itemSize.get();
        slotSize = Config.CONFIG.slotSize.get();
        slotRadius = Config.CONFIG.radialMenuRadius.get();
        counterclockwise = Config.CONFIG.counterclockwiseRotation.get();
        hideUnusedSlots = Config.CONFIG.hideUnusedSlots.get();
        hideSlotNumber = Config.CONFIG.hideSlotNumber.get();
        hideSlotSprite = Config.CONFIG.hideSlotSprite.get();

        startAngle = Config.CONFIG.startAngle.get();
        endAngle = Config.CONFIG.endAngle.get();
        
        backgroundDarkenOpacity = Config.CONFIG.backgroundDarkenOpacity.get();
        
        innerDeadzoneRadius = Config.CONFIG.innerDeadzone.get();
        outerDeadzoneRadius = Config.CONFIG.outerDeadzone.get();

        disabledSlots[0] = Config.CONFIG.disableSlot1.get();
        disabledSlots[1] = Config.CONFIG.disableSlot2.get();
        disabledSlots[2] = Config.CONFIG.disableSlot3.get();
        disabledSlots[3] = Config.CONFIG.disableSlot4.get();
        disabledSlots[4] = Config.CONFIG.disableSlot5.get();
        disabledSlots[5] = Config.CONFIG.disableSlot6.get();
        disabledSlots[6] = Config.CONFIG.disableSlot7.get();
        disabledSlots[7] = Config.CONFIG.disableSlot8.get();
        disabledSlots[8] = Config.CONFIG.disableSlot9.get();

        clickToSelect = Config.CONFIG.clickToSelect.get();
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
        if (!clickToSelect) selectHoveredSlot();
        hoveredSlot = -1;
    }

    public void render(GuiGraphics graphics, float partialTick) {
        
        class RenderHelper {
            void renderAll() {

                Minecraft mc = Minecraft.getInstance();

                if (mc.isPaused()) return;

                mc.mouseHandler.releaseMouse();

                LocalPlayer player = mc.player;
                if (player == null) return;
                
                JsonObject json = ResourceHelper.readJsonFromResources(mc.getResourceManager(), "textures/texture_config.json");

                int screenWidth = mc.getWindow().getGuiScaledWidth();
                int screenHeight = mc.getWindow().getGuiScaledHeight();
                int centerX = screenWidth / 2;
                int centerY = screenHeight / 2;

                if (backgroundDarkenOpacity > 0) renderBackground(json, screenWidth, screenHeight);

                Inventory inventory = player.getInventory();
                int[] visibleSlots = CommonRenderFunctions.getVisibleSlots(inventory, hideUnusedSlots, disabledSlots);
                if (visibleSlots.length == 0) return;

                SlotPosition[] slotPositions = CommonRenderFunctions.calculateSlotPositions(visibleSlots, centerX, centerY, startAngle, endAngle, counterclockwise, slotRadius);

                double mouseX = mc.mouseHandler.xpos() * screenWidth / mc.getWindow().getScreenWidth() - centerX;
                double mouseY = mc.mouseHandler.ypos() * screenHeight / mc.getWindow().getScreenHeight() - centerY;

                hoveredSlot = CommonRenderFunctions.getHoveredSlot(mouseX, mouseY, slotPositions, slotRadius, innerDeadzoneRadius, outerDeadzoneRadius);

                activeSlot = inventory.getSelectedSlot();

                for (SlotPosition pos : slotPositions) {
                    boolean isActive = (pos.slotIndex == activeSlot);
                    boolean isHovered = (pos.slotIndex == hoveredSlot);

                    String state = isActive ? "_active" : (isHovered ? "_hovered" : "");
                    int xOffset = Utils.getIntOrDefault(json, Constants.JSON_X_OFFSET + state, 0);
                    int yOffset = Utils.getIntOrDefault(json, Constants.JSON_Y_OFFSET + state, 0);
                    int x = pos.baseX + xOffset;
                    int y = pos.baseY + yOffset;

                    if (!hideSlotSprite) renderSlot(x, y, isActive, isHovered);
                    ItemStack stack = inventory.getItem(pos.slotIndex);
                    if (!stack.isEmpty()) renderItem(mc, json, stack, x, y, isActive, isHovered);
                    if (!hideSlotNumber) renderSlotNumber(mc, json, pos.slotIndex, x, y, isActive, isHovered);
                }
                mc.renderBuffers().bufferSource().endBatch();
            }

            @SuppressWarnings("null")
            void renderSlot(int x, int y, boolean active, boolean hovered) {
                ResourceLocation tex = active ? Constants.SLOT_ACTIVE_TEXTURE :
                    hovered ? Constants.SLOT_HOVERED_TEXTURE :
                    Constants.SLOT_TEXTURE;
                graphics.blit(RenderPipelines.GUI_TEXTURED, tex,
                    x - slotSize / 2, y - slotSize / 2,
                    0F, 0F, slotSize, slotSize, slotSize, slotSize);
            }

            @SuppressWarnings("null")
            void renderItem(Minecraft mc, JsonObject json, ItemStack stack, int x, int y, boolean active, boolean hovered) {
                String state = active ? "_active" : (hovered ? "_hovered" : "");
                int ix = x + Utils.getIntOrDefault(json, Constants.JSON_ITEM_X_OFFSET + state, 0);
                int iy = y + Utils.getIntOrDefault(json, Constants.JSON_ITEM_Y_OFFSET + state, 0);
                graphics.pose().pushMatrix();
                graphics.pose().translate(ix + 8, iy + 8);
                float scale = itemSize / 16f;
                graphics.pose().scale(scale, scale);
                graphics.pose().translate(-(ix + 8), -(iy + 8));
                graphics.renderItem(stack, ix, iy);
                graphics.renderItemDecorations(mc.font, stack, ix, iy);
                graphics.pose().popMatrix();
            }

            @SuppressWarnings("null")
            void renderSlotNumber(Minecraft mc, JsonObject json, int index, int x, int y, boolean active, boolean hovered) {
                String num = String.valueOf(index + 1);
                String state = active ? "_active" : (hovered ? "_hovered" : "");
                int xOffset = Utils.getIntOrDefault(json, Constants.JSON_SLOT_NUMBER_X_OFFSET + state, 0);
                int yOffset = Utils.getIntOrDefault(json, Constants.JSON_SLOT_NUMBER_Y_OFFSET + state, 0);
                int tx = x - mc.font.width(num) / 2 + xOffset;
                int ty = y + itemSize / 2 + yOffset;
                int col = active ? Utils.parseColor(json, Constants.JSON_SLOT_NUMBER_COLOR_ACTIVE, Constants.DEFAULT_SLOT_NUMBER_COLOR_ACTIVE) :
                    hovered ? Utils.parseColor(json, Constants.JSON_SLOT_NUMBER_COLOR_HOVERED, Constants.DEFAULT_SLOT_NUMBER_COLOR_HOVERED) :
                    Utils.parseColor(json, Constants.JSON_SLOT_NUMBER_COLOR, Constants.DEFAULT_SLOT_NUMBER_COLOR);
                graphics.drawString(mc.font, num, tx, ty, col);
            }

            void renderBackground(JsonObject json, int screenWidth, int screenHeight) {
                int baseColor = Utils.parseColor(json, Constants.JSON_BACKGROUND_OVERLAY_COLOR, 0x000000);
                int colorWithAlpha = (backgroundDarkenOpacity << 24) | (baseColor & 0xFFFFFF);
                graphics.fill(0, 0, screenWidth, screenHeight, colorWithAlpha);
            }
        }

        new RenderHelper().renderAll();
    }

    public void selectHoveredSlot() {
        if (hoveredSlot < 0 || hoveredSlot >= Constants.SLOT_COUNT || activeSlot == hoveredSlot) return;

        CommonRenderFunctions.selectSlot(hoveredSlot);
    }


}