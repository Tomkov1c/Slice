package com.tomkovic.slice;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;


public class SliceClient {

    @SubscribeEvent
    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RadialMenuRenderer.updateFromConfig();
        });
    }

    @SubscribeEvent
    private static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.OPEN_RADIAL_MENU);
    }
}
