package com.tomkovic.slice;

import com.tomkovic.slice.handlers.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class SliceClient implements ClientModInitializer {

    public static Config CONFIG;
    public static RadialMenuRenderer renderer = new RadialMenuRenderer();

    @SuppressWarnings("deprecation")
    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(KeyBindings.OPEN_RADIAL_MENU);
        KeyBindingHelper.registerKeyBinding(KeyBindings.CLICK_TO_SELECT);

        AutoConfig.register(Config.class, Toml4jConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(Config.class).getConfig();

        ConfigHandler.registerListener();
        CONFIG.pushConfigToGlobal();

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            renderer.render(drawContext, tickDelta.getGameTimeDeltaTicks());
        });

        RadialMenuHandler.canHandleKeyBind = true;

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
        	KeyBindings.handleOpenRadialMenu();
         	KeyBindings.handleClickToSelect();
        });
    }
}
