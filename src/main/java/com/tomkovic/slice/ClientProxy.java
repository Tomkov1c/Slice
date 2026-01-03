package com.tomkovic.slice;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        SliceClient.init(event);
        
        MinecraftForge.EVENT_BUS.register(new com.tomkovic.slice.handlers.RadialMenuHandler());
    }
}