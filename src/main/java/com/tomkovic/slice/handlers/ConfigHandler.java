package com.tomkovic.slice.handlers;


import com.tomkovic.slice.Config;
import com.tomkovic.slice.RadialMenuRenderer;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;

public class ConfigHandler {
    public ConfigHandler(RadialMenuRenderer renderer) {
        
    }

    @SubscribeEvent
    public void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == Config.CONFIG_SPEC) {
            RadialMenuRenderer.updateFromConfig();
        }
    }

    @SubscribeEvent
    public void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == Config.CONFIG_SPEC) {
            RadialMenuRenderer.updateFromConfig();
        }
    }

}
