package com.tomkovic.slice.handlers;

import com.mojang.blaze3d.platform.InputConstants;
import com.tomkovic.slice.Config;
import com.tomkovic.slice.Constants;
import com.tomkovic.slice.KeyBindings;
import com.tomkovic.slice.RadialMenuRenderer;
import com.tomkovic.slice.RadialMenuState;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollingEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
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

    // Just need these to not crash the game

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {

    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseButton event) { 

    }

    @SubscribeEvent
    public void onMouseScroll(MouseScrollingEvent event) { 

     }

    @SubscribeEvent
    public void onRenderGui(ScreenEvent.Render.Pre event) { 

    }



    private static void handleMenuClick(Minecraft mc, InputEvent.MouseButton event) {
        //event.setCanceled(true);

        if (clickToSelect && event.getButton() == 0 && event.getAction() == InputConstants.PRESS) {
            renderer.selectHoveredSlot();

            if (closeOnSelect) {
                RadialMenuState.closeMenu(mc, () -> renderer.onMenuClose());
            }
        }
    }
}
