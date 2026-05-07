package com.ansi.slice.handlers;

import com.ansi.slice.Config;
import com.ansi.slice.SliceClient;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.world.InteractionResult;

public class ConfigHandler {

    private static final ConfigHolder<Config> CONFIG_HOLDER = AutoConfig.getConfigHolder(Config.class);

    public static void registerListener() {
        CONFIG_HOLDER.registerSaveListener((config, spec) -> {
            SliceClient.CONFIG.pushConfigToGlobal();
            return InteractionResult.SUCCESS;
        });

        AutoConfig.getConfigHolder(Config.class).registerSaveListener((config, spec) -> {
            SliceClient.CONFIG.pushConfigToGlobal();
            return InteractionResult.SUCCESS;
        });
    }
}
