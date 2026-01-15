package com.tomkovic.slice;

import java.lang.reflect.Field;
import com.google.gson.JsonObject;
import com.tomkovic.slice.classes.SlotPosition;
import com.tomkovic.slice.handlers.RadialMenuHandler;
import com.tomkovic.slice.helpers.RadialMenuHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class RadialMenuRenderer {

    private static int itemSize;
    private static int slotSize;
    private static int slotRadius;
    private static boolean counterclockwise;
    private static boolean hideUnusedSlots;
    private static boolean hideSlotNumber;
    private static boolean hideSlotSprite;
    private static boolean[] disabledSlots = new boolean[Constants.SLOT_COUNT];
    private static int startAngle;
    private static int endAngle;
    private static int backgroundDarkenOpacity;
    private static int innerDeadzoneRadius;
    private static int outerDeadzoneRadius;
    private static boolean clickToSelect;

    private double mouseStartX = 0;
    private double mouseStartY = 0;
    private static Field selectedField = null;

    private boolean isRendering = false;
    private boolean hasRenderedOnce = false;
    private boolean cursorEnteredSelectionArea = false;

    private SlotPosition[] cachedSlotPositions = null;
    private int[] cachedVisibleSlots = null;
    private int cachedCenterX = 0;
    private int cachedCenterY = 0;
    private JsonObject cachedJson = null;

    private int cachedScreenWidth = -1;
    private int cachedScreenHeight = -1;

    private LocalPlayer cachedPlayer = null;
    private Inventory cachedInventory = null;

    private int cachedSlotNumberColorNormal = -1;
    private int cachedSlotNumberColorHovered = -1;
    private int cachedSlotNumberColorActive = -1;

    public RadialMenuRenderer() {
        try {
            selectedField = Inventory.class.getDeclaredField("selected");
            selectedField.setAccessible(true);
        } catch (Exception e) {
            Constants.LOG.error("Failed to access Inventory.selected field", e);
        }
        refreshConfig();
    }
    
    public static void refreshConfig() {
        itemSize = GlobalConfig.itemSize;
        slotSize = GlobalConfig.slotSize;
        slotRadius = GlobalConfig.radialMenuRadius;
        counterclockwise = GlobalConfig.counterclockwiseRotation;
        hideUnusedSlots = GlobalConfig.hideUnusedSlots;
        hideSlotNumber = GlobalConfig.hideSlotNumber;
        hideSlotSprite = GlobalConfig.hideSlotSprite;

        startAngle = GlobalConfig.startAngle;
        endAngle = GlobalConfig.endAngle;
        
        backgroundDarkenOpacity = GlobalConfig.backgroundDarkenOpacity;
        
        innerDeadzoneRadius = GlobalConfig.innerDeadzone;
        outerDeadzoneRadius = GlobalConfig.outerDeadzone;

        disabledSlots[0] = GlobalConfig.disableSlot1;
        disabledSlots[1] = GlobalConfig.disableSlot2;
        disabledSlots[2] = GlobalConfig.disableSlot3;
        disabledSlots[3] = GlobalConfig.disableSlot4;
        disabledSlots[4] = GlobalConfig.disableSlot5;
        disabledSlots[5] = GlobalConfig.disableSlot6;
        disabledSlots[6] = GlobalConfig.disableSlot7;
        disabledSlots[7] = GlobalConfig.disableSlot8;
        disabledSlots[8] = GlobalConfig.disableSlot9;

        clickToSelect = GlobalConfig.clickToSelect;
    }

    public void renderMenu() {
        if (!isRendering) {
            isRendering = true;
            hasRenderedOnce = false;
            cursorEnteredSelectionArea = false;
            onMenuOpen();
        }
    }

    public void derenderMenu() {
        if (isRendering) {
            isRendering = false;
            hasRenderedOnce = false;
            cursorEnteredSelectionArea = false;
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
        cachedJson = null;

        cachedScreenWidth = -1;
        cachedScreenHeight = -1;
        cachedCenterX = -1;
        cachedCenterY = -1;

        cachedInventory = null;
        cachedSlotPositions = null;
        
        cachedSlotNumberColorNormal = -1;
        cachedSlotNumberColorHovered = -1;
        cachedSlotNumberColorActive = -1;
    }
    
    private void initializeCache(Minecraft mc, LocalPlayer player) {
        if (cachedJson == null) {
            cachedJson = RadialMenuHelper.readJsonFromResources(mc.getResourceManager(), "textures/texture_config.json");
        }

        if (cachedScreenWidth == -1 && cachedScreenHeight == -1) {
            cachedScreenWidth = mc.getWindow().getGuiScaledWidth();
            cachedScreenHeight = mc.getWindow().getGuiScaledHeight();
            cachedCenterX = cachedScreenWidth / 2;
            cachedCenterY = cachedScreenHeight / 2;
        }

        if (cachedInventory == null) {
            cachedInventory = player.getInventory();
        }

        if (cachedSlotPositions == null) {
            cachedVisibleSlots = RadialMenuHelper.getVisibleSlots(cachedInventory, hideUnusedSlots, disabledSlots);
            if (cachedVisibleSlots.length == 0) return;
            
            cachedSlotPositions = RadialMenuHelper.calculateSlotPositions(
                cachedVisibleSlots, cachedCenterX, cachedCenterY, 
                startAngle, endAngle, counterclockwise, slotRadius
            );
        }

        if (cachedSlotNumberColorNormal == -1) {
            cachedSlotNumberColorNormal = RadialMenuHelper.parseColor(cachedJson, Constants.JSON_SLOT_NUMBER_COLOR, Constants.DEFAULT_SLOT_NUMBER_COLOR);
            cachedSlotNumberColorHovered = RadialMenuHelper.parseColor(cachedJson, Constants.JSON_SLOT_NUMBER_COLOR_HOVERED, Constants.DEFAULT_SLOT_NUMBER_COLOR_HOVERED);
            cachedSlotNumberColorActive = RadialMenuHelper.parseColor(cachedJson, Constants.JSON_SLOT_NUMBER_COLOR_ACTIVE, Constants.DEFAULT_SLOT_NUMBER_COLOR_ACTIVE);
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

        boolean cursorInSelectionArea = RadialMenuHelper.isCursorInSelectionArea(mouseX, mouseY, slotRadius, innerDeadzoneRadius, outerDeadzoneRadius);

        if (cursorInSelectionArea)
            RadialMenuHandler.hoveredSlot = RadialMenuHelper.getHoveredSlot(
                mouseX, mouseY, cachedSlotPositions, slotRadius, 
                innerDeadzoneRadius, outerDeadzoneRadius
            );
        else
            RadialMenuHandler.hoveredSlot = -1;

        RadialMenuHandler.selectedSlot = cachedInventory.getSelectedSlot();

        if (backgroundDarkenOpacity > 0) renderBackground(graphics, cachedJson, cachedScreenWidth, cachedScreenHeight);

        renderVisibleSlots(graphics, mc);

        mc.renderBuffers().bufferSource().endBatch();
        
        hasRenderedOnce = true;
    }

    private void renderVisibleSlots(GuiGraphics graphics, Minecraft mc) {
        for (SlotPosition pos : cachedSlotPositions) {
            boolean isActive = (pos.slotIndex == RadialMenuHandler.selectedSlot);
            boolean isHovered = (pos.slotIndex == RadialMenuHandler.hoveredSlot);
            
            String state = isActive ? "_active" : (isHovered ? "_hovered" : "");
            int xOffset = RadialMenuHelper.getIntOrDefault(cachedJson, Constants.JSON_X_OFFSET + state, 0);
            int yOffset = RadialMenuHelper.getIntOrDefault(cachedJson, Constants.JSON_Y_OFFSET + state, 0);
            int x = pos.baseX + xOffset;
            int y = pos.baseY + yOffset;

            if (!hideSlotSprite) renderSlot(graphics, x, y, isActive, isHovered);
            
            ItemStack stack = cachedInventory.getItem(pos.slotIndex);
            if (!stack.isEmpty()) renderItem(graphics, mc, stack, x, y, isActive, isHovered);
            
            if (!hideSlotNumber) renderSlotNumber(graphics, mc, pos.slotIndex, x, y, isActive, isHovered);
        }
    }

    @SuppressWarnings("null")
    private void renderSlot(GuiGraphics graphics, int x, int y, boolean active, boolean hovered) {
        ResourceLocation tex = active ? Constants.SLOT_ACTIVE_TEXTURE :
            hovered ? Constants.SLOT_HOVERED_TEXTURE :
            Constants.SLOT_TEXTURE;

        graphics.blit(RenderPipelines.GUI_TEXTURED, tex,
            x - slotSize / 2, y - slotSize / 2,
            0F, 0F, slotSize, slotSize, slotSize, slotSize);
    }

    @SuppressWarnings("null")
    private void renderItem(GuiGraphics graphics, Minecraft mc, ItemStack stack, int x, int y, boolean active, boolean hovered) {
        String state = active ? "_active" : (hovered ? "_hovered" : "");
        int ix = x + RadialMenuHelper.getIntOrDefault(cachedJson, Constants.JSON_ITEM_X_OFFSET + state, 0);
        int iy = y + RadialMenuHelper.getIntOrDefault(cachedJson, Constants.JSON_ITEM_Y_OFFSET + state, 0);
        
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
    private void renderSlotNumber(GuiGraphics graphics, Minecraft mc, int index, int x, int y, boolean active, boolean hovered) {
        String num = String.valueOf(index + 1);
        String state = active ? "_active" : (hovered ? "_hovered" : "");
        int xOffset = RadialMenuHelper.getIntOrDefault(cachedJson, Constants.JSON_SLOT_NUMBER_X_OFFSET + state, 0);
        int yOffset = RadialMenuHelper.getIntOrDefault(cachedJson, Constants.JSON_SLOT_NUMBER_Y_OFFSET + state, 0);
        int tx = x - mc.font.width(num) / 2 + xOffset;
        int ty = y + itemSize / 2 + yOffset;
        
        int col = active ? cachedSlotNumberColorActive :
            hovered ? cachedSlotNumberColorHovered :
            cachedSlotNumberColorNormal;
        
        graphics.drawString(mc.font, num, tx, ty, col);
    }

    private void renderBackground(GuiGraphics graphics, JsonObject json, int screenWidth, int screenHeight) {
        int baseColor = RadialMenuHelper.parseColor(json, Constants.JSON_BACKGROUND_OVERLAY_COLOR, 0x000000);
        int colorWithAlpha = (backgroundDarkenOpacity << 24) | (baseColor & 0xFFFFFF);
        graphics.fill(0, 0, screenWidth, screenHeight, colorWithAlpha);
    }
    
}