package com.tomkovic.slice.handlers;

import com.tomkovic.slice.GlobalConfig;
import com.tomkovic.slice.Constants;
import com.tomkovic.slice.platform.Services;

import net.minecraft.client.Minecraft;

public class RadialMenuHandler {

    public static Minecraft mc() { return Minecraft.getInstance(); }

    public static boolean isMenuOpen;

    /* Config */
    private static boolean isToggleEnabled;
    private static boolean isClickToSelectEnabled;
    private static boolean isCloseOnSelectEnabled;

    private static boolean isRecenterCursorEnabled;
    /* */

    public static int hoveredSlot = 0;
    public static int selectedSlot = 0;

    public static boolean canHandleKeyBind = false;


    public static void refreshConfig() {
        isToggleEnabled = GlobalConfig.toggleKeybind;
        isClickToSelectEnabled = GlobalConfig.clickToSelect;
        isCloseOnSelectEnabled = GlobalConfig.closeOnSelect;
        isRecenterCursorEnabled = false;
    }

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

    public static void selectSlot(int index) {
        Services.PLATFORM.setSelectedSlot(index);

        selectedSlot = index;
    }

    public static void handleOpenMenuKeyBehaviour() {
        if(!canHandleKeyBind || mc().isPaused()) return;

        if (isToggleEnabled) {
            handleToggleMode();
        }
        else {
            handleHoldMode();
        }
    }
    
    private static void handleToggleMode() {
        /*  On Open    */
        if (BindingHandler.openMenuKeyState.isPressed() && !isMenuOpen) {
            openMenu();
        }
        
        /*  On Close    */
        else if (BindingHandler.openMenuKeyState.isPressed() && isMenuOpen) {
            closeMenu();
        }
    }
    
    private static void handleHoldMode() {
        /*  On Open    */
        if (BindingHandler.openMenuKeyState.isPressed() && !isMenuOpen) {
            openMenu();
        }

        /*  On Close    */
        else if (BindingHandler.openMenuKeyState.isReleased() && isMenuOpen) {
            closeMenu();

            if (selectedSlot != hoveredSlot) { handleSlotSelecting(hoveredSlot); }
        }
    }

    private static void handleSlotSelecting(int index) {

        selectSlot(index);

        if(isCloseOnSelectEnabled && isMenuOpen) { closeMenu(); }

        if(isRecenterCursorEnabled && isMenuOpen) { centerCursor(); }
    }
    
}
