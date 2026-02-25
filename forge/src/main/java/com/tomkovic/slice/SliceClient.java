package com.tomkovic.slice;

import com.tomkovic.slice.commands.ReloadConfigCommand;
import com.tomkovic.slice.handlers.RadialMenuHandler;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SliceClient {
    public static RadialMenuRenderer renderer;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Config.pushConfigToGlobal();
            Minecraft mc = Minecraft.getInstance();
            mc.gui.layers.add(
                (guiGraphics, deltaTracker) -> {
                    if (SliceClient.renderer != null) SliceClient.renderer.render(guiGraphics, deltaTracker.getGameTimeDeltaTicks());
                }
            );
        });
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.OPEN_RADIAL_MENU);
        event.register(KeyBindings.CLICK_TO_SELECT);
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) { ReloadConfigCommand.register(event.getDispatcher()); }

        @SubscribeEvent
        public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
            allowKeyBindHandling(true);
            renderer = new RadialMenuRenderer();
        }

        @SubscribeEvent
        public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
            allowKeyBindHandling(false);
            renderer = null;
        }

        @SubscribeEvent
        public static void onScreenOpen(ScreenEvent.Opening event) {
            if (RadialMenuHandler.isMenuOpen && event.getScreen() != null)
                RadialMenuHandler.closeMenu();
            allowKeyBindHandling(false);
        }

        @SubscribeEvent
        public static void onScreenClose(ScreenEvent.Closing event) {
            if (renderer != null) allowKeyBindHandling(true);
        }
    }

    private static void allowKeyBindHandling(boolean allow) {
        RadialMenuHandler.canHandleKeyBind = allow;
        KeyBindings.canHandleKeyBind = allow;
    }
}
