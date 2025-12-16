package com.tomkovic.slice;

import com.tomkovic.slice.handlers.ConfigHandler;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Constants.MOD_ID)
public class Slice {

    public Slice(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.CONFIG_SPEC);

        ConfigHandler configHandler = new ConfigHandler();
        
        modEventBus.register(configHandler);
    }
}
