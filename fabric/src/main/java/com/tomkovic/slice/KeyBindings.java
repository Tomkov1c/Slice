package com.tomkovic.slice;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class KeyBindings {
    @SuppressWarnings("null")
    public static final KeyMapping.Category CATEGORY_OBJECT = new KeyMapping.Category(
        ResourceLocation.fromNamespaceAndPath("slice", "radial_menu")
    );
    
    @SuppressWarnings("null")
    public static final KeyMapping OPEN_RADIAL_MENU = new KeyMapping(
        "key.slice.open_radial_menu",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_R,
        CATEGORY_OBJECT
    );
    
    public static boolean isOpenRadialMenuPressed() {
        return OPEN_RADIAL_MENU.isDown();
    }
    
    public static boolean isMouseButton() {
        if (OPEN_RADIAL_MENU == null) return false;
        return OPEN_RADIAL_MENU.getDefaultKey().getType() == InputConstants.Type.MOUSE;
    }

    public static int getMouseButton() {
        if (isMouseButton()) {
            return OPEN_RADIAL_MENU.getDefaultKey().getValue();
        }
        return -1;
    }
}