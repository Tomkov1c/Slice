package com.tomkovic.slice.handlers;

import com.tomkovic.slice.Config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;

public class ConfigHandler {

    @SubscribeEvent
    public void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == Config.CONFIG_SPEC) Config.pushConfigToGlobal();
    }

    @SubscribeEvent
    public void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == Config.CONFIG_SPEC) Config.pushConfigToGlobal();
    }

}
