package com.tomkovic.slice;

import com.tomkovic.slice.handlers.ConfigHandler;
import com.tomkovic.slice.handlers.RadialMenuHandler;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;


@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class SliceClient {

    public static RadialMenuRenderer renderer;

    public SliceClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        allowKeyBindHandling(true);
        ConfigHandler.updateConfigs();

        renderer = new RadialMenuRenderer();
    }

    @SubscribeEvent
    public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingOut event) {
        allowKeyBindHandling(false);

        renderer = null;
    }

    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        if (RadialMenuHandler.isMenuOpen && event.getScreen() != null) RadialMenuHandler.closeMenu();

        allowKeyBindHandling(false);
    }

    @SubscribeEvent
    public static void onScreenClose(ScreenEvent.Closing event) {              
        if (renderer != null) allowKeyBindHandling(true);
    }


    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (RadialMenuHandler.isMenuOpen && renderer != null)
            SliceClient.renderer.render(event.getGuiGraphics(), event.getPartialTick().getGameTimeDeltaTicks());
    }


    // Private
    private static void allowKeyBindHandling(boolean allow) {
        RadialMenuHandler.canHandleKeyBind = allow;
        KeyBindings.canHandleKeyBind = allow;
    }
}
