package com.tomkovic.slice;

import java.lang.invoke.MethodHandles;

import com.tomkovic.slice.handlers.ConfigHandler;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.MinecraftForge;

@Mod(Constants.MOD_ID)
public class Slice {
    public Slice(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, Config.CONFIG_SPEC);
        MinecraftForge.EVENT_BUS.register(new ConfigHandler());
    }
}
