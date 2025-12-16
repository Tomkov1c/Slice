package com.tomkovic.slice;

import com.tomkovic.slice.handlers.ConfigHandler;
import com.tomkovic.slice.handlers.RadialMenuHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Constants.MOD_ID)
public class Slice {

    public Slice() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CONFIG_SPEC);

        MinecraftForge.EVENT_BUS.register(new ConfigHandler());

        RadialMenuHandler handler = new RadialMenuHandler();

        BusGroup.DEFAULT.register(null, handler);
    }
}
