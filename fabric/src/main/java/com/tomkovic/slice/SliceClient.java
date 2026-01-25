package com.tomkovic.slice;

import com.tomkovic.slice.handlers.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

public class SliceClient implements ClientModInitializer {

    public static Config CONFIG;
    public static RadialMenuRenderer renderer;

    @SuppressWarnings("deprecation")
    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(KeyBindings.OPEN_RADIAL_MENU);
        KeyBindingHelper.registerKeyBinding(KeyBindings.CLICK_TO_SELECT);

        AutoConfig.register(Config.class, Toml4jConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(Config.class).getConfig();

        ConfigHandler.registerListener();
        CONFIG.pushConfigToGlobal();

        // Register renderer
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (renderer != null) renderer.render(drawContext, tickDelta.getGameTimeDeltaTicks());
        });

        // Do the key handling
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
        	if (client.screen == null && !RadialMenuHandler.canHandleKeyBind)  {
                RadialMenuHandler.canHandleKeyBind = true;
                renderer = new RadialMenuRenderer();
            }

        	KeyBindings.handleOpenRadialMenu();
         	KeyBindings.handleClickToSelect();
        });

        // Disble key handling when a screen is active
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
       		if (RadialMenuHandler.isMenuOpen) RadialMenuHandler.closeMenu();

        	renderer = null;
            RadialMenuHandler.canHandleKeyBind = false;
        });
    }
}
