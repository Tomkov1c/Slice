package com.tomkovic.slice;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    public static final Config CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public final ModConfigSpec.IntValue radialMenuRadius;
    public final ModConfigSpec.IntValue itemSize;
    public final ModConfigSpec.IntValue slotSize;
    public final ModConfigSpec.BooleanValue counterclockwiseRotation;
    public final ModConfigSpec.BooleanValue hideUnusedSlots;

    private Config(ModConfigSpec.Builder builder) {
        this.radialMenuRadius = builder
            .translation("slice.configuration.radialMenuRadius")
            .defineInRange("radialMenuRadius", 75, 40, 200);

        this.itemSize = builder
            .translation("slice.configuration.itemSize")
            .defineInRange("itemSize", 16, 1, 64);

        this.slotSize = builder
            .translation("slice.configuration.slotSize")
            .defineInRange("slotSize", 32, 16, 64);

        this.counterclockwiseRotation = builder
            .comment("If true, the radial menu rotates counterclockwise instead of clockwise.")
            .translation("slice.configuration.counterclockwiseRotation")
            .define("counterclockwiseRotation", false);

        this.hideUnusedSlots = builder
            .translation("slice.configuration.hideUnusedSlots")
            .define("hideUnusedSlots", false);
    }

    static {
        Pair<Config, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Config::new);
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
}
