package com.tomkovic.slice;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

public class KeyBindings {
    @NotNull
    public static final KeyMapping.Category CATEGORY_OBJECT = new KeyMapping.Category(
        Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath("slice", "radial_menu"))
    );

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

}
