package com.ansi.slice.helpers;

import com.ansi.slice.Constants;
import com.ansi.slice.GlobalConfig;
import com.ansi.slice.classes.SlotPosition;
import com.ansi.slice.classes.TexturePackCustomValues;
import com.ansi.slice.handlers.RadialMenuHandler;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;

public class RadialMenuRendererHelper {

    // Cache fields

    public int cachedCenterX = 0;
    public int cachedCenterY = 0;
    public int[] cachedVisibleSlots = null;
    public SlotPosition[] cachedSlotPositions = null;

    public int cachedScreenWidth = -1;
    public int cachedScreenHeight = -1;

    public LocalPlayer cachedPlayer = null;
    public Inventory cachedInventory = null;

    // Cache management

    public void clearCache() {
        cachedScreenWidth = -1;
        cachedScreenHeight = -1;
        cachedCenterX = -1;
        cachedCenterY = -1;
        cachedPlayer = null;
        cachedInventory = null;
        cachedSlotPositions = null;
    }

    public void initializeCache(TexturePackCustomValues jsonConfig) {
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

    // Offset resolution

    public static int resolveSlotXOffset(TexturePackCustomValues cfg, boolean active, boolean hovered) {
        return active ? cfg.xOffsetActive : (hovered ? cfg.xOffsetHovered : cfg.xOffset);
    }

    public static int resolveSlotYOffset(TexturePackCustomValues cfg, boolean active, boolean hovered) {
        return active ? cfg.yOffsetActive : (hovered ? cfg.yOffsetHovered : cfg.yOffset);
    }

    public static int resolveItemXOffset(TexturePackCustomValues cfg, boolean active, boolean hovered) {
        return active ? cfg.itemXOffsetActive : (hovered ? cfg.itemXOffsetHovered : cfg.itemXOffset);
    }

    public static int resolveItemYOffset(TexturePackCustomValues cfg, boolean active, boolean hovered) {
        return active ? cfg.itemYOffsetActive : (hovered ? cfg.itemYOffsetHovered : cfg.itemYOffset);
    }

    public static int resolveSlotNumberXOffset(TexturePackCustomValues cfg, boolean active, boolean hovered) {
        return active ? cfg.slotNumberXOffsetActive : (hovered ? cfg.slotNumberXOffsetHovered : cfg.slotNumberXOffset);
    }

    public static int resolveSlotNumberYOffset(TexturePackCustomValues cfg, boolean active, boolean hovered) {
        return active ? cfg.slotNumberYOffsetActive : (hovered ? cfg.slotNumberYOffsetHovered : cfg.slotNumberYOffset);
    }

    public static int resolveSlotNumberColor(TexturePackCustomValues cfg, boolean active, boolean hovered) {
        return active ? JsonHelper.parseColor(cfg.slotNumberColorActive, 0) :
               hovered ? JsonHelper.parseColor(cfg.slotNumberColorHovered, 0) :
               JsonHelper.parseColor(cfg.slotNumberColor, 0);
    }

    // Position calculation

    public static int resolveSlotX(SlotPosition pos, TexturePackCustomValues cfg, boolean active, boolean hovered) {
        return pos.baseX + resolveSlotXOffset(cfg, active, hovered);
    }

    public static int resolveSlotY(SlotPosition pos, TexturePackCustomValues cfg, boolean active, boolean hovered) {
        return pos.baseY + resolveSlotYOffset(cfg, active, hovered);
    }

    public static int resolveItemX(int slotX, TexturePackCustomValues cfg, boolean active, boolean hovered) {
        return slotX + resolveItemXOffset(cfg, active, hovered);
    }

    public static int resolveItemY(int slotY, TexturePackCustomValues cfg, boolean active, boolean hovered) {
        return slotY + resolveItemYOffset(cfg, active, hovered);
    }

    public static int resolveSlotNumberX(int slotX, int textWidth, TexturePackCustomValues cfg, boolean active, boolean hovered) {
        return slotX - textWidth / 2 + resolveSlotNumberXOffset(cfg, active, hovered);
    }

    public static int resolveSlotNumberY(int slotY, TexturePackCustomValues cfg, boolean active, boolean hovered) {
        return slotY + GlobalConfig.ITEM_SIZE / 2 + resolveSlotNumberYOffset(cfg, active, hovered) + ((GlobalConfig.SLOT_SIZE - 16) / 2);
    }

    // Item scale

    public static float resolveItemScale() {
        return GlobalConfig.ITEM_SIZE / 16f;
    }

    // Background color

    public static int resolveBackgroundColor(TexturePackCustomValues cfg) {
        int baseColor = JsonHelper.parseColor(cfg.backgroundOverlayColor, 0);
        return (GlobalConfig.BACKGROUND_OPACITY << 24) | (baseColor & 0xFFFFFF);
    }

    // Mouse projection

    public static double projectMouseX(double rawX, int cachedScreenWidth, int screenWidth, int centerX) {
        return rawX * cachedScreenWidth / screenWidth - centerX;
    }

    public static double projectMouseY(double rawY, int cachedScreenHeight, int screenHeight, int centerY) {
        return rawY * cachedScreenHeight / screenHeight - centerY;
    }

    // Slot state

    public static boolean isSlotActive(SlotPosition pos) {
        return pos.slotIndex == RadialMenuHandler.selectedSlot;
    }

    public static boolean isSlotHovered(SlotPosition pos) {
        return pos.slotIndex == RadialMenuHandler.hoveredSlot;
    }
}