package com.tomkovic.slice;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {
    public static final String CATEGORY = "key.category.slice.radial_menu";
    
    public static final KeyBinding OPEN_RADIAL_MENU = new KeyBinding(
        "key.slice.open_radial_menu",
        Keyboard.KEY_R,
        CATEGORY
    );
    
    public static void register() {
        ClientRegistry.registerKeyBinding(OPEN_RADIAL_MENU);
    }
    
    public static boolean isOpenRadialMenuPressed() {
        return OPEN_RADIAL_MENU.isPressed() || OPEN_RADIAL_MENU.isKeyDown();
    }
    
    public static boolean isMouseButton() {
        return OPEN_RADIAL_MENU.getKeyCode() < 0;
    }
    
    public static int getMouseButton() {
        if (isMouseButton()) {
            return OPEN_RADIAL_MENU.getKeyCode() + 100;
        }
        return -1;
    }
}