package com.tomkovic.slice.handlers;

import com.tomkovic.slice.Config;
import com.tomkovic.slice.RadialMenuRenderer;

import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

public class ConfigHandler {
        
    @SubscribeEvent
    public void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == Config.CONFIG_SPEC) {
            RadialMenuRenderer.updateFromConfig();
            RadialMenuHandler.updateFromConfig();
        }
    }

    @SubscribeEvent
    public void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == Config.CONFIG_SPEC) {
            RadialMenuRenderer.updateFromConfig();
            RadialMenuHandler.updateFromConfig();
        }
    }

}
