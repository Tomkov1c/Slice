package com.tomkovic.slice.handlers;

import com.mojang.blaze3d.platform.InputConstants;
import com.tomkovic.slice.Config;
import com.tomkovic.slice.Constants;
import com.tomkovic.slice.KeyBindings;
import com.tomkovic.slice.RadialMenuRenderer;
import com.tomkovic.slice.RadialMenuState;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollingEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID,  bus = EventBusSubscriber.Bus.FORGE, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class RadialMenuHandler {

    public static final RadialMenuRenderer renderer = new RadialMenuRenderer();

    private static boolean isToggleEnabled = Config.CONFIG.toggleKeybind.getDefault();
    private static boolean clickToSelect = Config.CONFIG.clickToSelect.getDefault();
    private static boolean closeOnSelect = Config.CONFIG.closeOnSelect.getDefault();
    private static boolean disableScrollingOnHotbar = Config.CONFIG.disableScrollingOnHotbar.getDefault();

    public static void updateFromConfig() { 
        isToggleEnabled = Config.CONFIG.toggleKeybind.get();
        clickToSelect = Config.CONFIG.clickToSelect.get();
        closeOnSelect = Config.CONFIG.closeOnSelect.get();
        disableScrollingOnHotbar = Config.CONFIG.disableScrollingOnHotbar.get();
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;
        
        if (KeyBindings.isMouseButton()) return;
        
        RadialMenuState.handleMenuToggle(mc, KeyBindings.isOpenRadialMenuPressed(), false, isToggleEnabled, () -> renderer.onMenuOpen(), () -> renderer.onMenuClose());
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) { 
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;
        
        if (KeyBindings.isMouseButton() && event.getButton() == KeyBindings.getMouseButton()) {
            //event.setResult(Event.De);
            
            boolean isPress = event.getAction() == InputConstants.PRESS;
            boolean isRelease = event.getAction() == InputConstants.RELEASE;
            
            RadialMenuState.handleMenuToggle(mc, isPress, isRelease, isToggleEnabled, () -> renderer.onMenuOpen(), () -> renderer.onMenuClose());
            return;
        }
        
        if (RadialMenuState.isMenuOpen) handleMenuClick(mc, event);
    }

    @SubscribeEvent
    public static void onMouseScroll(MouseScrollingEvent event) { 
        if (RadialMenuState.isMenuOpen || disableScrollingOnHotbar) {
            //event.setCanceled(true);
        }   
    }

    @SubscribeEvent
    public static void registerGuiOverlays(AddGuiOverlayLayersEvent event) {
        event.getLayeredDraw().add(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "radial_menu"),
            (guiGraphics, partialTick) -> {
                if (RadialMenuState.isMenuOpen) {
                    RadialMenuHandler.renderer.render(guiGraphics, partialTick.getGameTimeDeltaPartialTick(false));
                }
            }
        );
    }

    private static void handleMenuClick(Minecraft mc, InputEvent.MouseButton.Pre event) {
        //event.setCanceled();

        if (clickToSelect && event.getButton() == 0 && event.getAction() == InputConstants.PRESS) {
            renderer.selectHoveredSlot();

            if (closeOnSelect) {
                RadialMenuState.closeMenu(mc, () -> renderer.onMenuClose());
            }
        }
    }
}
