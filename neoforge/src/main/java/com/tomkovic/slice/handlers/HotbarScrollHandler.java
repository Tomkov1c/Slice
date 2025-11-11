package com.tomkovic.slice.handlers;

import com.tomkovic.slice.Config;
import com.tomkovic.slice.Constants;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class HotbarScrollHandler {
    
    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (Config.CONFIG.disableScrollingOnHotbar.get()) {
            event.setCanceled(true);
        }
    }
}