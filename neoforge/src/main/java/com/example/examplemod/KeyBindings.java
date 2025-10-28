package com.example.examplemod;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public class KeyBindings {
    public static final KeyMapping.Category CATEGORY_OBJECT = new KeyMapping.Category(ResourceLocation.fromNamespaceAndPath("slice", "radial_menu"));

    public static final KeyMapping OPEN_RADIAL_MENU = new KeyMapping(
        "key.slice.open_radial_menu",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_R,
        CATEGORY_OBJECT
    );
}