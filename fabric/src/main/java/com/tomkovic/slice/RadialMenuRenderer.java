package com.tomkovic.slice;

import java.util.Objects;

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

    Minecraft mc = RadialMenuHandler.mc();

    public volatile boolean isRendering = false;
    public boolean hasRenderedOnce = false;

    @SuppressWarnings("unused")
	private double cursorX = -1;
	@SuppressWarnings("unused")
    private double cursorY = -1;

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

    public void onMenuOpen() {
        RadialMenuHandler.hoveredSlot = -1;

        if (RadialMenuHandler.mc().mouseHandler != null) {
            cursorX = RadialMenuHandler.mc().mouseHandler.xpos();
            cursorY = RadialMenuHandler.mc().mouseHandler.ypos();
        }
    }

    public void onMenuClose() {
        clearCache();

        cursorX = -1;
        cursorY = -1;
    }

    public void clearCache() {
        cachedScreenWidth = -1;
        cachedScreenHeight = -1;
        cachedCenterX = -1;
        cachedCenterY = -1;

        cachedPlayer = null;
        cachedInventory = null;
        cachedSlotPositions = null;

    }

    private void initializeCache() {

        jsonConfig.parseFromResource(Constants.TEXTURE_CONFIG_JSON_NAMESPACE_PATH);

        if (cachedScreenWidth == -1 && cachedScreenHeight == -1) {
            cachedScreenWidth = mc.getWindow().getGuiScaledWidth();
            cachedScreenHeight = mc.getWindow().getGuiScaledHeight();
            cachedCenterX = cachedScreenWidth / 2;
            cachedCenterY = cachedScreenHeight / 2;
        }

        if (mc.player != null) cachedPlayer = mc.player;

        if (cachedInventory == null && cachedPlayer != null) cachedInventory = cachedPlayer.getInventory();

        if (cachedSlotPositions == null) {
            cachedVisibleSlots = RadialMenuHelper.getVisibleSlots(cachedInventory);

            if (cachedVisibleSlots.length == 0) return;

            cachedSlotPositions = RadialMenuHelper.calculateSlotPositions(cachedVisibleSlots, cachedCenterX, cachedCenterY);
        }
    }

    public void render(GuiGraphics graphics, float partialTick) {
        if (!isRendering) return;

        if (!hasRenderedOnce) {
            initializeCache();
            if (cachedVisibleSlots.length == 0) return;
        }

        double mouseX = mc.mouseHandler.xpos() * cachedScreenWidth / mc.getWindow().getScreenWidth() - cachedCenterX;
        double mouseY = mc.mouseHandler.ypos() * cachedScreenHeight / mc.getWindow().getScreenHeight() - cachedCenterY;

        boolean cursorInSelectionArea = RadialMenuHelper.isCursorInSelectionArea(mouseX, mouseY);

        if (cursorInSelectionArea)
            RadialMenuHandler.hoveredSlot = RadialMenuHelper.getHoveredSlot( mouseX, mouseY, cachedSlotPositions );
        else
            RadialMenuHandler.hoveredSlot = -1;

        RadialMenuHandler.selectedSlot = cachedInventory.getSelectedSlot();

        if (GlobalConfig.BACKGROUND_OPACITY > 0) renderBackground(graphics, cachedScreenWidth, cachedScreenHeight);

        renderVisibleSlots(graphics);

        mc.renderBuffers().bufferSource().endBatch();

        hasRenderedOnce = true;
    }

    private void renderVisibleSlots(GuiGraphics graphics) {
        if (GlobalConfig.HIDE_SLOT_SPRITE && GlobalConfig.HIDE_SLOT_NUMBER) return;

        for (SlotPosition pos : cachedSlotPositions) {
            boolean isActive = (pos.slotIndex == RadialMenuHandler.selectedSlot);
            boolean isHovered = (pos.slotIndex == RadialMenuHandler.hoveredSlot);

            int xOffset = isActive ? jsonConfig.xOffsetActive : (isHovered ? jsonConfig.xOffsetHovered : jsonConfig.xOffset);
            int yOffset = isActive ? jsonConfig.yOffsetActive : (isHovered ? jsonConfig.yOffsetHovered : jsonConfig.yOffset);

            int x = pos.baseX + xOffset;
            int y = pos.baseY + yOffset;

            if (!GlobalConfig.HIDE_SLOT_SPRITE) renderSlot(graphics, x, y, isActive, isHovered);

            ItemStack stack = cachedInventory.getItem(pos.slotIndex);
            if (!stack.isEmpty()) renderItem(graphics, cachedInventory.getItem(pos.slotIndex), x, y, isActive, isHovered);

            if (!GlobalConfig.HIDE_SLOT_NUMBER) renderSlotNumber(graphics, pos.slotIndex, x, y, isActive, isHovered);
        }
    }

    private void renderSlot(GuiGraphics graphics, int x, int y, boolean active, boolean hovered) {
        ResourceLocation tex = active ? Constants.SLOT_ACTIVE_TEXTURE :
            hovered ? Constants.SLOT_HOVERED_TEXTURE :
            Constants.SLOT_TEXTURE;

        graphics.blit(Objects.requireNonNull(RenderPipelines.GUI_TEXTURED), Objects.requireNonNull(tex),
            x - GlobalConfig.SLOT_SIZE / 2, y - GlobalConfig.SLOT_SIZE / 2,
            0F, 0F, GlobalConfig.SLOT_SIZE, GlobalConfig.SLOT_SIZE, GlobalConfig.SLOT_SIZE, GlobalConfig.SLOT_SIZE);
    }

    private void renderItem(GuiGraphics graphics, ItemStack stack, int x, int y, boolean active, boolean hovered) {

        int xOffset = active ? jsonConfig.itemXOffsetActive : (hovered ? jsonConfig.itemXOffsetHovered : jsonConfig.itemXOffset);
        int yOffset = active ? jsonConfig.itemYOffsetActive : (hovered ? jsonConfig.itemXOffsetHovered : jsonConfig.itemXOffset);

        int ix = x + xOffset;
        int iy = y + yOffset;

        graphics.pose().pushMatrix();
        graphics.pose().translate(ix + 8, iy + 8);
        float scale = GlobalConfig.ITEM_SIZE / 16f;
        graphics.pose().scale(scale, scale);
        graphics.pose().translate(-(ix + 8), -(iy + 8));

        if (stack == null) return;

        graphics.renderItem(stack, ix, iy);

        if (mc.font == null) return;

        graphics.renderItemDecorations(mc.font, stack, ix, iy);
        graphics.pose().popMatrix();
    }

    private void renderSlotNumber(GuiGraphics graphics, int index, int x, int y, boolean active, boolean hovered) {

        String num = String.valueOf(index + 1);

        int xOffset = active ? jsonConfig.slotNumberXOffsetActive : (hovered ? jsonConfig.slotNumberXOffsetHovered : jsonConfig.slotNumberXOffset);
        int yOffset = active ? jsonConfig.slotNumberYOffsetActive : (hovered ? jsonConfig.slotNumberYOffsetHovered : jsonConfig.slotNumberYOffset);

        if (num == null) return;

        int tx = x - mc.font.width(num) / 2 + xOffset;
        int ty = y + GlobalConfig.ITEM_SIZE / 2 + yOffset;

        int col = active ? JsonHelper.parseColor(jsonConfig.slotNumberColorActive, 0) :
            hovered ? JsonHelper.parseColor(jsonConfig.slotNumberColorHovered, 0) :
            JsonHelper.parseColor(jsonConfig.slotNumberColor, 0);

        if (mc.font == null) return;

        graphics.drawString(mc.font, num, tx, ty, col);
    }

    private void renderBackground(GuiGraphics graphics, int screenWidth, int screenHeight) {
        int baseColor = JsonHelper.parseColor(jsonConfig.backgroundOverlayColor, 0);

        int colorWithAlpha = (GlobalConfig.BACKGROUND_OPACITY << 24) | (baseColor & 0xFFFFFF);
        graphics.fill(0, 0, screenWidth, screenHeight, colorWithAlpha);
    }

}
