package com.tomkovic.slice;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue RADIAL_MENU_RADIUS = BUILDER
        .translation("slice.configuration.radialMenuRadius")
        .defineInRange("radialMenuRadius", 80, 40, 200);
   
    public static final ModConfigSpec.IntValue ITEM_SIZE = BUILDER
        .translation("slice.configuration.itemSize")
        .defineInRange("itemSize", 16, 1, 64);
   
    public static final ModConfigSpec.IntValue SLOT_SIZE = BUILDER
        .translation("slice.configuration.slotSize")
        .defineInRange("slotSize", 32, 16, 64);
    
    public static final ModConfigSpec.BooleanValue COUNTERCLOCKWISE_ROTATION = BUILDER
        .translation("slice.configuration.counterclockwiseRotation")
        .define("counterclockwiseRotation", false);

    public static final ModConfigSpec.BooleanValue HIDE_UNUSED_SLOTS = BUILDER
        .translation("slice.configuration.hideUnusedSlots")
        .define("hideUnusedSlots", false);
    
    public static final ModConfigSpec SPEC = BUILDER.build();
   
}