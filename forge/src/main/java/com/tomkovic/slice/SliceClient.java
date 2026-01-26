package com.tomkovic.slice;

import java.util.Objects;

import com.tomkovic.slice.commands.ReloadConfigCommand;
import com.tomkovic.slice.handlers.RadialMenuHandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// MOD bus events - registration and setup only
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SliceClient {

    public static RadialMenuRenderer renderer;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Config.pushConfigToGlobal();
        });
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(KeyBindings.OPEN_RADIAL_MENU);
            event.register(KeyBindings.CLICK_TO_SELECT);
        }

        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            ReloadConfigCommand.register(event.getDispatcher());
        }

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
            if (RadialMenuHandler.isMenuOpen && event.getScreen() != null) {
                RadialMenuHandler.closeMenu();
            }
            allowKeyBindHandling(false);
        }

        @SubscribeEvent
        public static void onScreenClose(ScreenEvent.Closing event) {
            if (renderer != null) {
                allowKeyBindHandling(true);
            }
        }

        @SubscribeEvent
        public static void registerGuiOverlays(AddGuiOverlayLayersEvent event) {
            event.getLayeredDraw().add(
                Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "radial_menu")),
                (guiGraphics, partialTick) -> {
                    SliceClient.renderer.render(guiGraphics, partialTick.getGameTimeDeltaTicks());
                }
            );
        }
    }

    // Private helper method
    private static void allowKeyBindHandling(boolean allow) {
        RadialMenuHandler.canHandleKeyBind = allow;
        KeyBindings.canHandleKeyBind = allow;
    }
}
