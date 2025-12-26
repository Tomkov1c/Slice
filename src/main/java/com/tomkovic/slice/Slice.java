package com.yourname.slice;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Slice.MODID, version = Slice.VERSION, name = Slice.NAME)
public class Slice {
    public static final String MODID = "slice";
    public static final String NAME = "Slice";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("Slice is loading!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Slice has loaded!");
    }
}