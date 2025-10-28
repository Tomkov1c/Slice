package com.example.examplemod;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.example.examplemod.handlers.ConfigHandler;
import com.example.examplemod.handlers.RadialMenuHandler;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
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
