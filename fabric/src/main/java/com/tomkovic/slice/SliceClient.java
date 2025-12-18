package com.tomkovic.slice;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class SliceClient implements ClientModInitializer{

    public static SliceConfig CONFIG;

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(KeyBindings.OPEN_RADIAL_MENU);

        AutoConfig.register(SliceConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(SliceConfig.class).getConfig();
    }
}
