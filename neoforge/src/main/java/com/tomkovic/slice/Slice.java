package com.tomkovic.slice;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.tomkovic.slice.handlers.ConfigHandler;
import com.tomkovic.slice.handlers.RadialMenuHandler;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Constants.MOD_ID)
public class Slice {
    public static final Logger LOGGER = LogUtils.getLogger();

    public Slice(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.CONFIG_SPEC);
        LOGGER.info("Roundabout Radial Menu Mod Initialized");

        ConfigHandler configHandler = new ConfigHandler(RadialMenuHandler.renderer);
        
        modEventBus.register(configHandler);
    }
}
