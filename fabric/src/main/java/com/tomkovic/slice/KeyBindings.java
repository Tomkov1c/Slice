package com.tomkovic.slice;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import com.tomkovic.slice.handlers.RadialMenuHandler;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

public class KeyBindings {

    @NotNull
    public static final KeyMapping.Category CATEGORY_OBJECT = new KeyMapping.Category(
        Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath("slice", "radial_menu"))
    );

    public static boolean OPEN_RADIAL_MENU_Privious_State = false;
    public static boolean CLICK_TO_SELECT_Privious_State = false;

    @NotNull
    public static final KeyMapping OPEN_RADIAL_MENU = new KeyMapping(
        "key.slice.open_radial_menu",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_R,
        CATEGORY_OBJECT
    );

    @NotNull
    public static final KeyMapping CLICK_TO_SELECT = new KeyMapping(
        "key.slice.click_to_select",
        InputConstants.Type.MOUSE,
        GLFW.GLFW_MOUSE_BUTTON_1,
        CATEGORY_OBJECT
    );

    public static void handleOpenRadialMenu() {
	   	boolean isKeyDown = OPEN_RADIAL_MENU.isDown();

	    if (OPEN_RADIAL_MENU_Privious_State == isKeyDown) return;

	    if (isKeyDown) {
	        RadialMenuHandler.handleOpenMenuKeyBehaviour(true);
	    } else if (!isKeyDown) {
	        RadialMenuHandler.handleOpenMenuKeyBehaviour(false);
	    }
	    OPEN_RADIAL_MENU_Privious_State = isKeyDown;
    }

    public static void handleClickToSelect() {
   		if (!RadialMenuHandler.isMenuOpen) return;

    	boolean isKeyDown = CLICK_TO_SELECT.isDown();

	    if (CLICK_TO_SELECT_Privious_State == isKeyDown) return;
	    if (isKeyDown) RadialMenuHandler.handleClickToSelect();

	    CLICK_TO_SELECT_Privious_State = isKeyDown;
    }

}
