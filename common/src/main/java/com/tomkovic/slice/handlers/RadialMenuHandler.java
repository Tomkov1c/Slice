package com.tomkovic.slice.handlers;

import com.tomkovic.slice.Constants;
import com.tomkovic.slice.GlobalConfig;
import com.tomkovic.slice.platform.Services;

import net.minecraft.client.Minecraft;

public class RadialMenuHandler {

    public static Minecraft mc() { return Minecraft.getInstance(); }

    public static boolean isMenuOpen = false;

    public static int hoveredSlot = 0;
    public static int selectedSlot = 0;

    public static boolean canHandleKeyBind = false;

    public static void openMenu() {
        isMenuOpen = true;
        mc().mouseHandler.releaseMouse();
        
        Services.PLATFORM.renderMenu();
    }
    
    public static void closeMenu() {
        isMenuOpen = false;
        mc().mouseHandler.grabMouse();

        Services.PLATFORM.derenderMenu();
    }

    public static void centerCursor() {
        
    }

    public static void handleOpenMenuKeyBehaviour() {
        if(!canHandleKeyBind) return;

        if (GlobalConfig.TOGGLE_KEYBIND)
            handleToggleMode();
        else
            handleHoldMode();
    }
    
    private static void handleToggleMode() {
        if (BindingHandler.openMenuKeyState.isPressed() && !isMenuOpen) {
            openMenu();
        }
        
        else if (BindingHandler.openMenuKeyState.isPressed() && isMenuOpen) {

            if (!GlobalConfig.CLICK_TO_SELECT) {
                handleSlotSelecting(hoveredSlot);
            }

            closeMenu();
        }
    }
    
    private static void handleHoldMode() {
        if (BindingHandler.openMenuKeyState.isPressed() && !isMenuOpen) {
            openMenu();
        }

        else if (BindingHandler.openMenuKeyState.isReleased() && isMenuOpen) {
            if (selectedSlot != hoveredSlot && !GlobalConfig.CLICK_TO_SELECT) { handleSlotSelecting(hoveredSlot); }

            closeMenu();
        }
    }

    public static void handleClickToSelect() {
        handleSlotSelecting(hoveredSlot);
    }

    private static void handleSlotSelecting(int index) {
        selectSlot(index);

        if(GlobalConfig.CLOSE_ON_SELECT && isMenuOpen) { closeMenu(); }
        if(GlobalConfig.RECENTER_ON_SELECT && isMenuOpen) { centerCursor(); }
    }

    private static void selectSlot(int index) {
        if (index < 0 && index > Constants.SLOT_COUNT) return;

        Services.PLATFORM.setSelectedSlot(index);
        selectedSlot = index;
    }
    
}
