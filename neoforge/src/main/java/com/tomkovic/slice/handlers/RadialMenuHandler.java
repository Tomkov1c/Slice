package com.tomkovic.slice.handlers;

import com.mojang.blaze3d.platform.InputConstants;
import com.tomkovic.slice.CommonRenderFunctions;
import com.tomkovic.slice.Config;
import com.tomkovic.slice.KeyBindings;
import com.tomkovic.slice.RadialMenuRenderer;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class RadialMenuHandler {
    private boolean isMenuOpen = false;
    private boolean keyPreviouslyPressed = false;
    private boolean keyProcessed = false; 

    public static final RadialMenuRenderer renderer = new RadialMenuRenderer();

    private static boolean isToggleEnabled = Config.CONFIG.toggleKeybind.getDefault();


    public RadialMenuHandler() {

    }

    public static void updateFromConfig() { 
        isToggleEnabled = Config.CONFIG.toggleKeybind.getAsBoolean();
    }
    
    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;
        
        if (KeyBindings.isMouseButton()) return;
        
        handleMenuToggle(mc, KeyBindings.isOpenRadialMenuPressed(), false);
    }
    
    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseButton.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;
        
        if (KeyBindings.isMouseButton() && event.getButton() == KeyBindings.getMouseButton()) {
            event.setCanceled(true);
            
            boolean isPress = event.getAction() == InputConstants.PRESS;
            boolean isRelease = event.getAction() == InputConstants.RELEASE;
            
            handleMenuToggle(mc, isPress, isRelease);
            return;
        }
        
        if (isMenuOpen) handleMenuClick(mc, event);
    }
    
    private void handleMenuToggle(Minecraft mc, boolean isKeyDown, boolean isKeyUp) {
        if (isToggleEnabled) handleToggleMode(mc, isKeyDown, isKeyUp);
        else handleHoldMode(mc, isKeyDown, isKeyUp);
    }
    
    private void handleToggleMode(Minecraft mc, boolean isPress, boolean isRelease) {
        if (isPress && !keyPreviouslyPressed && !keyProcessed) {
            keyPreviouslyPressed = true;
            keyProcessed = true;
            
            if (isMenuOpen) closeMenu(mc);
            else openMenu(mc);

        } else if (isRelease || !isPress) {
            keyPreviouslyPressed = false;
            keyProcessed = false;
        }
    }
    
    private void handleHoldMode(Minecraft mc, boolean isPress, boolean isRelease) {
        if (isPress && !isMenuOpen) openMenu(mc);
        else if ((isRelease || !isPress) && isMenuOpen) closeMenu(mc);
    }
    
    private void handleMenuClick(Minecraft mc, InputEvent.MouseButton.Pre event) {
        event.setCanceled(true);
        
        if (RadialMenuRenderer.clickToSelect
            && event.getButton() == 0
            && event.getAction() == InputConstants.PRESS) {
            
            renderer.selectHoveredSlot();
            
            if (isToggleEnabled) {
                isMenuOpen = true;
                keyProcessed = true;
            } else closeMenu(mc);
        }
    }
    
    private void openMenu(Minecraft mc) {
        isMenuOpen = true;
        mc.mouseHandler.releaseMouse();
        renderer.onMenuOpen();
    }
    
    private void closeMenu(Minecraft mc) {
        isMenuOpen = false;
        mc.mouseHandler.grabMouse();
        renderer.onMenuClose();
    }
    
    @SubscribeEvent
    public void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (isMenuOpen) event.setCanceled(true);
    }
    
    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent.Post event) {
        if (isMenuOpen) renderer.render(event.getGuiGraphics(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
    }
}