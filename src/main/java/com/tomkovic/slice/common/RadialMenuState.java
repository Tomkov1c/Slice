package com.tomkovic.slice;

import net.minecraft.client.Minecraft;

public class RadialMenuState {
    public static boolean isMenuOpen = false;
    public static boolean keyPreviouslyPressed = false;
    public static boolean keyProcessed = false;

    private static Minecraft mc = Minecraft.getInstance();
    
    public boolean isMenuOpen() {
        return isMenuOpen;
    }
    
    public static void handleMenuToggle(boolean isKeyDown, boolean isKeyUp, boolean isToggleEnabled, Runnable onMenuOpen, Runnable onMenuClose) {
        if (isToggleEnabled) {
            handleToggleMode(isKeyDown, isKeyUp, onMenuOpen, onMenuClose);
        } else {
            handleHoldMode(isKeyDown, isKeyUp, onMenuOpen, onMenuClose);
        }
    }
    
    private static void handleToggleMode(boolean isPress, boolean isRelease, Runnable onMenuOpen, Runnable onMenuClose) {
        if (isPress && !keyPreviouslyPressed && !keyProcessed) {
            keyPreviouslyPressed = true;
            keyProcessed = true;
            
            if (isMenuOpen) {
                closeMenu(onMenuClose);
            } else {
                openMenu(onMenuOpen);
            }
        } else if (isRelease || !isPress) {
            keyPreviouslyPressed = false;
            keyProcessed = false;
        }
    }
    
    private static void handleHoldMode(boolean isPress, boolean isRelease, Runnable onMenuOpen, Runnable onMenuClose) {
        if (isPress && !isMenuOpen) {
            openMenu(onMenuOpen);
        } else if ((isRelease || !isPress) && isMenuOpen) {
            closeMenu(onMenuClose);
        }
    }
    
    
    public static void openMenu(Runnable onMenuOpen) {
        isMenuOpen = true;
        mc.mouseHandler.releaseMouse();
        onMenuOpen.run();
    }
    
    public static void closeMenu(Runnable onMenuClose) {
        isMenuOpen = false;
        mc.mouseHandler.grabMouse();
        onMenuClose.run();
    }
    
}