package com.tomkovic.slice;

import java.lang.reflect.Field;
import com.tomkovic.slice.classes.SlotPosition;
import com.tomkovic.slice.classes.TexturePackCustomValues;
import com.tomkovic.slice.handlers.RadialMenuHandler;
import com.tomkovic.slice.helpers.JsonHelper;
import com.tomkovic.slice.helpers.RadialMenuHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class RadialMenuRenderer {
    private boolean isRendering = false;
    private boolean hasRenderedOnce = false;

    private double mouseStartX = 0;
    private double mouseStartY = 0;
    private static Field selectedField = null;

    TexturePackCustomValues jsonConfig = new TexturePackCustomValues();

    // Cache
    private int cachedCenterX = 0;
    private int cachedCenterY = 0;
    private int[] cachedVisibleSlots = null;
    private SlotPosition[] cachedSlotPositions = null;

    private int cachedScreenWidth = -1;
    private int cachedScreenHeight = -1;

    private LocalPlayer cachedPlayer = null;
    private Inventory cachedInventory = null;
    //

    public RadialMenuRenderer() {
        try {
            selectedField = Inventory.class.getDeclaredField("selected");
            selectedField.setAccessible(true);
        } catch (Exception e) {
            Constants.LOG.error("Failed to access Inventory.selected field", e);
        }
    }

    public void renderMenu() {
        if (!isRendering) {
            isRendering = true;
            hasRenderedOnce = false;
            onMenuOpen();
        }
    }

    public void derenderMenu() {
        if (isRendering) {
            isRendering = false;
            hasRenderedOnce = false;
            onMenuClose();
            clearCache();
        }
    }

    public void onMenuOpen() {
        RadialMenuHandler.hoveredSlot = -1;

        if (RadialMenuHandler.mc().mouseHandler != null) {
            mouseStartX = RadialMenuHandler.mc().mouseHandler.xpos();
            mouseStartY = RadialMenuHandler.mc().mouseHandler.ypos();
        }
    }

    public void onMenuClose() {
        clearCache();

        mouseStartX = -1;
        mouseStartY = -1;
    }

    private void clearCache() {
        cachedPlayer = null;

        cachedScreenWidth = -1;
        cachedScreenHeight = -1;
        cachedCenterX = -1;
        cachedCenterY = -1;

        cachedInventory = null;
        cachedSlotPositions = null;

    }
    
    private void initializeCache(Minecraft mc, LocalPlayer player) {

        jsonConfig.parseFromResource(Constants.TEXTTURE_CONFIG_JSON_NAMESPACE_PATH);

        if (cachedScreenWidth == -1 && cachedScreenHeight == -1) {
            cachedScreenWidth = mc.getWindow().getGuiScaledWidth();
            cachedScreenHeight = mc.getWindow().getGuiScaledHeight();
            cachedCenterX = cachedScreenWidth / 2;
            cachedCenterY = cachedScreenHeight / 2;
        }

        if (cachedInventory == null) cachedInventory = player.getInventory();

        if (cachedSlotPositions == null) {
            cachedVisibleSlots = RadialMenuHelper.getVisibleSlots(cachedInventory, GlobalConfig.HIDE_UNUSED_SLOTS);
            
            if (cachedVisibleSlots.length == 0) return;
            
            cachedSlotPositions = RadialMenuHelper.calculateSlotPositions(
                cachedVisibleSlots, cachedCenterX, cachedCenterY, 
                GlobalConfig.START_ANGLE, GlobalConfig.END_ANGLE, GlobalConfig.REVERSE_ROTATION, GlobalConfig.MENU_RADIUS
            );
        }
    }

    public void render(GuiGraphics graphics, float partialTick) {
        if (!isRendering) return;

        Minecraft mc = RadialMenuHandler.mc();
        LocalPlayer player = mc.player;
        if (player == null) return;

        if (!hasRenderedOnce) {
            initializeCache(mc, player);
            if (cachedVisibleSlots.length == 0) return;
        }

        double mouseX = mc.mouseHandler.xpos() * cachedScreenWidth / mc.getWindow().getScreenWidth() - cachedCenterX;
        double mouseY = mc.mouseHandler.ypos() * cachedScreenHeight / mc.getWindow().getScreenHeight() - cachedCenterY;

        boolean cursorInSelectionArea = RadialMenuHelper.isCursorInSelectionArea(mouseX, mouseY, GlobalConfig.MENU_RADIUS, GlobalConfig.INNER_DEADZONE, GlobalConfig.OUTER_DEADZONE);

        if (cursorInSelectionArea)
            RadialMenuHandler.hoveredSlot = RadialMenuHelper.getHoveredSlot(
                mouseX, mouseY, cachedSlotPositions, GlobalConfig.MENU_RADIUS, 
                GlobalConfig.INNER_DEADZONE, GlobalConfig.OUTER_DEADZONE
            );
        else
            RadialMenuHandler.hoveredSlot = -1;

        RadialMenuHandler.selectedSlot = cachedInventory.getSelectedSlot();

        if (GlobalConfig.BACKGROUND_OPACITY > 0) renderBackground(graphics, cachedScreenWidth, cachedScreenHeight);

        renderVisibleSlots(graphics, mc);

        mc.renderBuffers().bufferSource().endBatch();
        
        hasRenderedOnce = true;
    }

    private void renderVisibleSlots(GuiGraphics graphics, Minecraft mc) {
        for (SlotPosition pos : cachedSlotPositions) {
            boolean isActive = (pos.slotIndex == RadialMenuHandler.selectedSlot);
            boolean isHovered = (pos.slotIndex == RadialMenuHandler.hoveredSlot);
            
            int xOffset = isActive ? jsonConfig.xOffsetActive : (isHovered ? jsonConfig.xOffsetHovered : jsonConfig.xOffset);
            int yOffset = isActive ? jsonConfig.yOffsetActive : (isHovered ? jsonConfig.yOffsetHovered : jsonConfig.yOffset);

            int x = pos.baseX + xOffset;
            int y = pos.baseY + yOffset;

            if (!GlobalConfig.HIDE_SLOT_SPRITE) renderSlot(graphics, x, y, isActive, isHovered);
            
            ItemStack stack = cachedInventory.getItem(pos.slotIndex);
            if (!stack.isEmpty()) renderItem(graphics, mc, stack, x, y, isActive, isHovered);
            
            if (!GlobalConfig.HIDE_SLOT_NUMBER) renderSlotNumber(graphics, mc, pos.slotIndex, x, y, isActive, isHovered);
        }
    }

    @SuppressWarnings("null")
    private void renderSlot(GuiGraphics graphics, int x, int y, boolean active, boolean hovered) {
        ResourceLocation tex = active ? Constants.SLOT_ACTIVE_TEXTURE :
            hovered ? Constants.SLOT_HOVERED_TEXTURE :
            Constants.SLOT_TEXTURE;

        graphics.blit(RenderPipelines.GUI_TEXTURED, tex,
            x - GlobalConfig.SLOT_SIZE / 2, y - GlobalConfig.SLOT_SIZE / 2,
            0F, 0F, GlobalConfig.SLOT_SIZE, GlobalConfig.SLOT_SIZE, GlobalConfig.SLOT_SIZE, GlobalConfig.SLOT_SIZE);
    }

    @SuppressWarnings("null")
    private void renderItem(GuiGraphics graphics, Minecraft mc, ItemStack stack, int x, int y, boolean active, boolean hovered) {

        int x_offset = active ? jsonConfig.itemXOffsetActive : (hovered ? jsonConfig.itemXOffsetHovered : jsonConfig.itemXOffset);
        int y_offset = active ? jsonConfig.itemYOffsetActive : (hovered ? jsonConfig.itemXOffsetHovered : jsonConfig.itemXOffset);

        int ix = x + x_offset;
        int iy = y + y_offset;
        
        graphics.pose().pushMatrix();
        graphics.pose().translate(ix + 8, iy + 8);
        float scale = GlobalConfig.ITEM_SIZE / 16f;
        graphics.pose().scale(scale, scale);
        graphics.pose().translate(-(ix + 8), -(iy + 8));
        graphics.renderItem(stack, ix, iy);
        graphics.renderItemDecorations(mc.font, stack, ix, iy);
        graphics.pose().popMatrix();
    }

    @SuppressWarnings("null")
    private void renderSlotNumber(GuiGraphics graphics, Minecraft mc, int index, int x, int y, boolean active, boolean hovered) {

        String num = String.valueOf(index + 1);

        int xOffset = active ? jsonConfig.slotNumberXOffsetActive : (hovered ? jsonConfig.slotNumberXOffsetHovered : jsonConfig.slotNumberXOffset);
        int yOffset = active ? jsonConfig.slotNumberYOffsetActive : (hovered ? jsonConfig.slotNumberYOffsetHovered : jsonConfig.slotNumberYOffset);

        int tx = x - mc.font.width(num) / 2 + xOffset;
        int ty = y + GlobalConfig.ITEM_SIZE / 2 + yOffset;
        
        int col = active ? JsonHelper.parseColor(jsonConfig.slotNumberColorActive, 0) :
            hovered ? JsonHelper.parseColor(jsonConfig.slotNumberColorHovered, 0) :
            JsonHelper.parseColor(jsonConfig.slotNumberColor, 0);
        
        graphics.drawString(mc.font, num, tx, ty, col);
    }

    private void renderBackground(GuiGraphics graphics, int screenWidth, int screenHeight) {
        int baseColor = JsonHelper.parseColor(jsonConfig.backgroundOverlayColor, 0);

        int colorWithAlpha = (GlobalConfig.BACKGROUND_OPACITY << 24) | (baseColor & 0xFFFFFF);
        graphics.fill(0, 0, screenWidth, screenHeight, colorWithAlpha);
    }
    
}

// 237