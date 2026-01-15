package com.tomkovic.slice;

import com.tomkovic.slice.handlers.ConfigHandler;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Constants.MOD_ID)
public class Slice {
    ConfigHandler configHandler = new ConfigHandler();

    public Slice(IEventBus modEventBus, ModContainer modContainer) {

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.CONFIG_SPEC);
        modEventBus.register(configHandler);

        modEventBus.addListener(KeyBindings::onRegisterKeyMappings);
        NeoForge.EVENT_BUS.register(KeyBindings.class);
    }
}
