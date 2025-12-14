package com.tomkovic.slice.handlers;

import com.mojang.blaze3d.platform.InputConstants;
import com.tomkovic.slice.Config;
import com.tomkovic.slice.KeyBindings;
import com.tomkovic.slice.RadialMenuRenderer;
import com.tomkovic.slice.RadialMenuState;

import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class RadialMenuHandler {

    public static final RadialMenuRenderer renderer = new RadialMenuRenderer();

    private static boolean isToggleEnabled = Config.CONFIG.toggleKeybind.getDefault();


    public static void updateFromConfig() { 
        isToggleEnabled = Config.CONFIG.toggleKeybind.getAsBoolean();
    }
    
    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;
        
        if (KeyBindings.isMouseButton()) return;
        
        RadialMenuState.handleMenuToggle(mc, KeyBindings.isOpenRadialMenuPressed(), false, isToggleEnabled, () -> renderer.onMenuOpen(), () -> renderer.onMenuClose());
    }
    
    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseButton.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;
        
        if (KeyBindings.isMouseButton() && event.getButton() == KeyBindings.getMouseButton()) {
            event.setCanceled(true);
            
            boolean isPress = event.getAction() == InputConstants.PRESS;
            boolean isRelease = event.getAction() == InputConstants.RELEASE;
            
            RadialMenuState.handleMenuToggle(mc, isPress, isRelease, isToggleEnabled, () -> renderer.onMenuOpen(), () -> renderer.onMenuClose());
            return;
        }
        
        if (RadialMenuState.isMenuOpen) handleMenuClick(mc, event);
    }
    
    @SubscribeEvent
    public void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (RadialMenuState.isMenuOpen) event.setCanceled(true);
    }
    
    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent.Post event) {
        if (RadialMenuState.isMenuOpen) renderer.render(event.getGuiGraphics(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
    }

    

    private void handleMenuClick(Minecraft mc, InputEvent.MouseButton.Pre event) {
        event.setCanceled(true);

        if (RadialMenuRenderer.clickToSelect
            && event.getButton() == 0
            && event.getAction() == InputConstants.PRESS) {

            renderer.selectHoveredSlot();

            RadialMenuState.closeMenu(mc, () -> renderer.onMenuClose());
        }
    }

}