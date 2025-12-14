package com.tomkovic.slice;

import net.minecraft.client.Minecraft;

public class RadialMenuState {
    public static boolean isMenuOpen = false;
    public static boolean keyPreviouslyPressed = false;
    public static boolean keyProcessed = false;
    
    public boolean isMenuOpen() {
        return isMenuOpen;
    }
    
    public static void handleMenuToggle(Minecraft mc, boolean isKeyDown, boolean isKeyUp, boolean isToggleEnabled, Runnable onMenuOpen, Runnable onMenuClose) {
        if (isToggleEnabled) {
            handleToggleMode(mc, isKeyDown, isKeyUp, onMenuOpen, onMenuClose);
        } else {
            handleHoldMode(mc, isKeyDown, isKeyUp, onMenuOpen, onMenuClose);
        }
    }
    
    private static void handleToggleMode(Minecraft mc, boolean isPress, boolean isRelease, Runnable onMenuOpen, Runnable onMenuClose) {
        if (isPress && !keyPreviouslyPressed && !keyProcessed) {
            keyPreviouslyPressed = true;
            keyProcessed = true;
            
            if (isMenuOpen) {
                closeMenu(mc, onMenuClose);
            } else {
                openMenu(mc, onMenuOpen);
            }
        } else if (isRelease || !isPress) {
            keyPreviouslyPressed = false;
            keyProcessed = false;
        }
    }
    
    private static void handleHoldMode(Minecraft mc, boolean isPress, boolean isRelease, Runnable onMenuOpen, Runnable onMenuClose) {
        if (isPress && !isMenuOpen) {
            openMenu(mc, onMenuOpen);
        } else if ((isRelease || !isPress) && isMenuOpen) {
            closeMenu(mc, onMenuClose);
        }
    }
    
    
    public static void openMenu(Minecraft mc, Runnable onMenuOpen) {
        isMenuOpen = true;
        mc.mouseHandler.releaseMouse();
        onMenuOpen.run();
    }
    
    public static void closeMenu(Minecraft mc, Runnable onMenuClose) {
        isMenuOpen = false;
        mc.mouseHandler.grabMouse();
        onMenuClose.run();
    }
    
}