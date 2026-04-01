package com.tomkovic.slice;

import com.tomkovic.slice.handlers.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
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

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.screen == null && !RadialMenuHandler.canHandleKeyBind) {
                RadialMenuHandler.canHandleKeyBind = true;
                renderer = new RadialMenuRenderer();
            }
            KeyBindings.handleOpenRadialMenu();
            KeyBindings.handleClickToSelect();
        });

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (RadialMenuHandler.isMenuOpen) RadialMenuHandler.closeMenu();
            renderer = null;
            RadialMenuHandler.canHandleKeyBind = false;
        });
    }
}