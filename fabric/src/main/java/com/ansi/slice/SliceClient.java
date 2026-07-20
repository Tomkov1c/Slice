package com.ansi.slice;

import com.ansi.slice.handlers.*;
import com.ansi.slice.handlers.ConfigHandler;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.resources.Identifier;

public class SliceClient implements ClientModInitializer {
    public static Config CONFIG;
    public static RadialMenuRenderer renderer;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(Config.class, Toml4jConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(Config.class).getConfig();
        ConfigHandler.registerListener();
        CONFIG.pushConfigToGlobal();

        HudElementRegistry.attachElementBefore(
            VanillaHudElements.CHAT,
            Identifier.fromNamespaceAndPath("slice", "radial_menu"),
            (graphics, tickCounter) -> {
                if (renderer != null) renderer.render(graphics, tickCounter);
            }
        );

        KeyMappingHelper.registerKeyMapping(KeyBindings.CLICK_TO_SELECT);
        KeyMappingHelper.registerKeyMapping(KeyBindings.OPEN_RADIAL_MENU);
        KeyMappingHelper.registerKeyMapping(KeyBindings.SWAP_TO_OFFHAND);


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.screen == null && !RadialMenuHandler.canHandleKeyBind) {
                RadialMenuHandler.canHandleKeyBind = true;
                renderer = new RadialMenuRenderer();
            }
            KeyBindings.handleOpenRadialMenu();
            KeyBindings.handleClickToSelect();
            KeyBindings.handleSwapToOffhand();
        });

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (RadialMenuHandler.isMenuOpen) RadialMenuHandler.closeMenu();
            renderer = null;
            RadialMenuHandler.canHandleKeyBind = false;
        });
    }
}