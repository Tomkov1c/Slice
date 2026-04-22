package com.ansi.slice;

import java.util.Objects;

import com.ansi.slice.classes.SlotPosition;
import com.ansi.slice.classes.TexturePackCustomValues;
import com.ansi.slice.handlers.RadialMenuHandler;
import com.ansi.slice.helpers.RadialMenuHelper;
import com.ansi.slice.helpers.RadialMenuRendererHelper;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class RadialMenuRenderer {

    public boolean isRendering = false;
    public boolean hasRenderedOnce = false;

    @SuppressWarnings("unused")
    private double cursorX = -1;

    @SuppressWarnings("unused")
    private double cursorY = -1;

    public RadialMenuRendererHelper helper = new RadialMenuRendererHelper();
    TexturePackCustomValues jsonConfig = new TexturePackCustomValues();

    public void onMenuOpen() {
        RadialMenuHandler.hoveredSlot = -1;

        if (Constants.MINECRAFT.mouseHandler != null) {
            cursorX = Constants.MINECRAFT.mouseHandler.xpos();
            cursorY = Constants.MINECRAFT.mouseHandler.ypos();
        }
    }

    public void onMenuClose() {
        helper.clearCache();
        cursorX = -1;
        cursorY = -1;
    }

    public void render(GuiGraphicsExtractor graphics, float partialTick) {
        if (!isRendering) return;

        if (!hasRenderedOnce) {
            helper.initializeCache(jsonConfig);
            if (helper.cachedVisibleSlots.length == 0) return;
        }

        double mouseX = RadialMenuRendererHelper.projectMouseX(
            Constants.MINECRAFT.mouseHandler.xpos(), helper.cachedScreenWidth,
            Constants.MINECRAFT.getWindow().getScreenWidth(), helper.cachedCenterX);

        double mouseY = RadialMenuRendererHelper.projectMouseY(
            Constants.MINECRAFT.mouseHandler.ypos(), helper.cachedScreenHeight,
            Constants.MINECRAFT.getWindow().getScreenHeight(), helper.cachedCenterY);

        boolean cursorInSelectionArea = RadialMenuHelper.isCursorInSelectionArea(mouseX, mouseY);

        if (cursorInSelectionArea)
            RadialMenuHandler.hoveredSlot = RadialMenuHelper.getHoveredSlot(mouseX, mouseY, helper.cachedSlotPositions);
        else
            RadialMenuHandler.hoveredSlot = -1;

        RadialMenuHandler.selectedSlot = helper.cachedInventory.getSelectedSlot();

        if (GlobalConfig.BACKGROUND_OPACITY > 0) renderBackground(graphics, helper.cachedScreenWidth, helper.cachedScreenHeight);

        renderVisibleSlots(graphics);

        Constants.MINECRAFT.renderBuffers().bufferSource().endBatch();

        hasRenderedOnce = true;
    }

    private void renderVisibleSlots(GuiGraphicsExtractor graphics) {
        for (SlotPosition pos : helper.cachedSlotPositions) {
            boolean isActive = RadialMenuRendererHelper.isSlotActive(pos);
            boolean isHovered = RadialMenuRendererHelper.isSlotHovered(pos);

            int x = RadialMenuRendererHelper.resolveSlotX(pos, jsonConfig, isActive, isHovered);
            int y = RadialMenuRendererHelper.resolveSlotY(pos, jsonConfig, isActive, isHovered);

            if (!GlobalConfig.HIDE_SLOT_SPRITE) renderSlot(graphics, x, y, isActive, isHovered);

            ItemStack stack = helper.cachedInventory.getItem(pos.slotIndex);
            if (!stack.isEmpty()) renderItem(graphics, stack, x, y, isActive, isHovered);

            if (!GlobalConfig.HIDE_SLOT_NUMBER) renderSlotNumber(graphics, pos.slotIndex, x, y, isActive, isHovered);
        }
    }

    private void renderSlot(GuiGraphicsExtractor graphics, int x, int y, boolean active, boolean hovered) {
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

    private void renderItem(GuiGraphicsExtractor graphics, ItemStack stack, int x, int y, boolean active, boolean hovered) {
        int ix = RadialMenuRendererHelper.resolveItemX(x, jsonConfig, active, hovered);
        int iy = RadialMenuRendererHelper.resolveItemY(y, jsonConfig, active, hovered);

        float scale = RadialMenuRendererHelper.resolveItemScale();

        graphics.pose().pushMatrix();
        graphics.pose().translate(ix + 8, iy + 8);
        graphics.pose().scale(scale, scale);
        graphics.pose().translate(-(ix + 8), -(iy + 8));

        graphics.item(stack, ix, iy);

        if (Constants.MINECRAFT.font != null)
            graphics.itemDecorations(Constants.MINECRAFT.font, stack, ix, iy);

        graphics.pose().popMatrix();
    }

    private void renderSlotNumber(GuiGraphicsExtractor graphics, int index, int x, int y, boolean active, boolean hovered) {
        if (Constants.MINECRAFT.font == null) return;

        String num = String.valueOf(index + 1);

        int tx = RadialMenuRendererHelper.resolveSlotNumberX(x, Constants.MINECRAFT.font.width(num), jsonConfig, active, hovered);
        int ty = RadialMenuRendererHelper.resolveSlotNumberY(y, jsonConfig, active, hovered);
        int col = RadialMenuRendererHelper.resolveSlotNumberColor(jsonConfig, active, hovered);

        graphics.text(Constants.MINECRAFT.font, num, tx, ty, col);
    }

    private void renderBackground(GuiGraphicsExtractor graphics, int screenWidth, int screenHeight) {
        int colorWithAlpha = RadialMenuRendererHelper.resolveBackgroundColor(jsonConfig);
        graphics.fill(0, 0, screenWidth, screenHeight, colorWithAlpha);
    }
}