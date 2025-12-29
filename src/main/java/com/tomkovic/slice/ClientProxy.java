package com.tomkovic.slice;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        SliceClient.init(event);
    }
}