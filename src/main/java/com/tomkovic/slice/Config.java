package com.tomkovic.slice;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue RADIAL_MENU_RADIUS = BUILDER
        .comment("Distance from center to slot")
        .defineInRange("radialMenuRadius", 80, 40, 200);

    public static final ModConfigSpec.IntValue ITEM_SIZE = BUILDER
        .comment("Size of each item slot icon")
        .defineInRange("itemSize", 16, 1, 64);

    public static final ModConfigSpec.IntValue SLOT_SIZE = BUILDER
        .comment("Size of the slot background texture")
        .defineInRange("slotSize", 32, 16, 64);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
