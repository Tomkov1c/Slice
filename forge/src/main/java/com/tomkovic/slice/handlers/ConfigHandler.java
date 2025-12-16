package com.tomkovic.slice.handlers;

import com.tomkovic.slice.Config;
import com.tomkovic.slice.Constants;
import com.tomkovic.slice.RadialMenuRenderer;

import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID,  bus = EventBusSubscriber.Bus.MOD)
public class ConfigHandler {
        
    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == Config.CONFIG_SPEC)getCurrentConfig();
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == Config.CONFIG_SPEC) getCurrentConfig();
    }



    public static void getCurrentConfig() {
        RadialMenuRenderer.updateFromConfig();
        RadialMenuHandler.updateFromConfig();
    }

}
