package com.ansi.slice;

import java.util.Objects;

import com.ansi.slice.classes.SlotPosition;
import com.ansi.slice.classes.TexturePackCustomValues;
import com.ansi.slice.handlers.RadialMenuHandler;
import com.ansi.slice.helpers.RadialMenuHelper;
import com.ansi.slice.helpers.RadialMenuRendererHelper;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class RadialMenuRenderer {

    public boolean isRendering = false;
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

    public void onMenuOpen() {
        RadialMenuHandler.hoveredSlot = -1;

        if (Constants.MINECRAFT.mouseHandler != null) {
            cursorX = Constants.MINECRAFT.mouseHandler.xpos();
            cursorY = Constants.MINECRAFT.mouseHandler.ypos();
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
            cachedScreenWidth = Constants.MINECRAFT.getWindow().getGuiScaledWidth();
            cachedScreenHeight = Constants.MINECRAFT.getWindow().getGuiScaledHeight();
            cachedCenterX = cachedScreenWidth / 2;
            cachedCenterY = cachedScreenHeight / 2;
        }

        if (Constants.MINECRAFT.player != null) cachedPlayer = Constants.MINECRAFT.player;

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

        double mouseX = RadialMenuRendererHelper.projectMouseX(
            Constants.MINECRAFT.mouseHandler.xpos(), cachedScreenWidth,
            Constants.MINECRAFT.getWindow().getScreenWidth(), cachedCenterX);

        double mouseY = RadialMenuRendererHelper.projectMouseY(
            Constants.MINECRAFT.mouseHandler.ypos(), cachedScreenHeight,
            Constants.MINECRAFT.getWindow().getScreenHeight(), cachedCenterY);

        boolean cursorInSelectionArea = RadialMenuHelper.isCursorInSelectionArea(mouseX, mouseY);

        if (cursorInSelectionArea)
            RadialMenuHandler.hoveredSlot = RadialMenuHelper.getHoveredSlot(mouseX, mouseY, cachedSlotPositions);
        else
            RadialMenuHandler.hoveredSlot = -1;

        RadialMenuHandler.selectedSlot = cachedInventory.getSelectedSlot();

        if (GlobalConfig.BACKGROUND_OPACITY > 0) renderBackground(graphics, cachedScreenWidth, cachedScreenHeight);

        renderVisibleSlots(graphics);

        Constants.MINECRAFT.renderBuffers().bufferSource().endBatch();

        hasRenderedOnce = true;
    }

    private void renderVisibleSlots(GuiGraphics graphics) {
        for (SlotPosition pos : cachedSlotPositions) {
            boolean isActive = RadialMenuRendererHelper.isSlotActive(pos);
            boolean isHovered = RadialMenuRendererHelper.isSlotHovered(pos);

            int x = RadialMenuRendererHelper.resolveSlotX(pos, jsonConfig, isActive, isHovered);
            int y = RadialMenuRendererHelper.resolveSlotY(pos, jsonConfig, isActive, isHovered);

            if (!GlobalConfig.HIDE_SLOT_SPRITE) renderSlot(graphics, x, y, isActive, isHovered);

            ItemStack stack = cachedInventory.getItem(pos.slotIndex);
            if (!stack.isEmpty()) renderItem(graphics, stack, x, y, isActive, isHovered);

            if (!GlobalConfig.HIDE_SLOT_NUMBER) renderSlotNumber(graphics, pos.slotIndex, x, y, isActive, isHovered);
        }
    }

    private void renderSlot(GuiGraphics graphics, int x, int y, boolean active, boolean hovered) {
        Identifier tex = active ? Constants.SLOT_ACTIVE_TEXTURE :
            hovered ? Constants.SLOT_HOVERED_TEXTURE :
            Constants.SLOT_TEXTURE;

        graphics.blit(
            Objects.requireNonNull(RenderPipelines.GUI_TEXTURED),
            Objects.requireNonNull(tex),
            x - GlobalConfig.SLOT_SIZE / 2, y - GlobalConfig.SLOT_SIZE / 2,
            0F, 0F, GlobalConfig.SLOT_SIZE, GlobalConfig.SLOT_SIZE,
            GlobalConfig.SLOT_SIZE, GlobalConfig.SLOT_SIZE);
    }

    private void renderItem(GuiGraphics graphics, ItemStack stack, int x, int y, boolean active, boolean hovered) {
        int ix = RadialMenuRendererHelper.resolveItemX(x, jsonConfig, active, hovered);
        int iy = RadialMenuRendererHelper.resolveItemY(y, jsonConfig, active, hovered);

        float scale = RadialMenuRendererHelper.resolveItemScale();

        graphics.pose().pushMatrix();
        graphics.pose().translate(ix + 8, iy + 8);
        graphics.pose().scale(scale, scale);
        graphics.pose().translate(-(ix + 8), -(iy + 8));

        if (stack == null) return;

        graphics.renderItem(stack, ix, iy);

        if (Constants.MINECRAFT.font == null) return;

        graphics.renderItemDecorations(Constants.MINECRAFT.font, stack, ix, iy);
        graphics.pose().popMatrix();
    }

    private void renderSlotNumber(GuiGraphics graphics, int index, int x, int y, boolean active, boolean hovered) {
        if (Constants.MINECRAFT.font == null) return;

        String num = String.valueOf(index + 1);

        int tx = RadialMenuRendererHelper.resolveSlotNumberX(x, Constants.MINECRAFT.font.width(num), jsonConfig, active, hovered);
        int ty = RadialMenuRendererHelper.resolveSlotNumberY(y, jsonConfig, active, hovered);
        int col = RadialMenuRendererHelper.resolveSlotNumberColor(jsonConfig, active, hovered);

        graphics.drawString(Constants.MINECRAFT.font, num, tx, ty, col);
    }


    private void renderBackground(GuiGraphics graphics, int screenWidth, int screenHeight) {
        int colorWithAlpha = RadialMenuRendererHelper.resolveBackgroundColor(jsonConfig);
        graphics.fill(0, 0, screenWidth, screenHeight, colorWithAlpha);
    }
}