package com.tomkovic.slice.handlers;

import com.tomkovic.slice.Config;
import com.tomkovic.slice.RadialMenuRenderer;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.world.InteractionResult;

public class ConfigHandler {

    private static final ConfigHolder<Config> CONFIG_HOLDER = AutoConfig.getConfigHolder(Config.class);

    public static void onConfigReload() {
        RadialMenuRenderer.updateFromConfig();
        RadialMenuHandler.updateFromConfig();
    }

    public static void registerListener() {
        CONFIG_HOLDER.registerSaveListener((config, spec) -> {
            onConfigReload();
            return InteractionResult.SUCCESS;
        });

        AutoConfig.getConfigHolder(Config.class).registerSaveListener((config, spec) -> {
            onConfigReload();
            return InteractionResult.SUCCESS;
        });
    }
}
