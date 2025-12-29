package com.tomkovic.slice;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class SliceClient {
    public static void init(FMLInitializationEvent event) {
        KeyBindings.register();
        RadialMenuRenderer.updateFromConfig();
    }
}