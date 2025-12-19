package com.tomkovic.slice;

import java.lang.invoke.MethodHandles;

import com.tomkovic.slice.handlers.ConfigHandler;

import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// import net.minecraftforge.fml.ModLoadingContext;
// import net.minecraftforge.client.ConfigScreenHandler;
// import net.minecraftforge.fml.common.Mod;
// import net.minecraftforge.fml.config.ModConfig;

@Mod(Constants.MOD_ID)
public class Slice {

    public Slice(FMLJavaModLoadingContext context) {

        context.registerConfig(ModConfig.Type.COMMON, Config.CONFIG_SPEC);

        BusGroup.DEFAULT.register(MethodHandles.lookup() , new ConfigHandler());

        // ModLoadingContext.get().registerExtensionPoint(
        //     ConfigScreenHandler.ConfigScreenFactory.class,
        //     () -> new ConfigScreenHandler.ConfigScreenFactory(
        //         (minecraft, parent) -> new ConfigScreen(parent)
        //     )
        // );

    }
}
