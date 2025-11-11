package com.tomkovic.slice;

import net.minecraft.world.entity.ai.behavior.Behavior;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;

import org.apache.commons.lang3.tuple.Pair;

public class Config {
    public static final Config CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public final BooleanValue disableScrollingOnHotbar;

    public final ModConfigSpec.IntValue radialMenuRadius;
    public final ModConfigSpec.IntValue itemSize;
    public final ModConfigSpec.IntValue slotSize;
    public final ModConfigSpec.BooleanValue counterclockwiseRotation;
    public final ModConfigSpec.BooleanValue hideUnusedSlots;
    public final ModConfigSpec.BooleanValue hideSlotNumber;
    public final ModConfigSpec.BooleanValue hideSlotSprite;

    public final ModConfigSpec.BooleanValue toggleKeybind;

    private Config(ModConfigSpec.Builder builder) {

        {
                var display = builder
                        .translation("slice.configuration.category.display")
                        .push("display");

                {
                        var size = builder
                                .translation("slice.configuration.category.size")
                                .push("size");

                                this.radialMenuRadius = size
                                        .translation("slice.configuration.radialMenuRadius")
                                        .defineInRange("radialMenuRadius", 75, 40, 200);

                                this.itemSize = size
                                        .translation("slice.configuration.itemSize")
                                        .defineInRange("itemSize", 16, 1, 64);
                                
                                this.slotSize = size
                                        .translation("slice.configuration.slotSize")
                                        .defineInRange("slotSize", 32, 16, 64);

                        size.pop();
                }

                {
                        var visibility = builder
                                .translation("slice.configuration.category.visibility")
                                .push("visibility");
                        
                                this.hideUnusedSlots = visibility
                                        .translation("slice.configuration.visibility.hideUnusedSlots")
                                        .define("hideUnusedSlots", false);
                                
                                this.hideSlotNumber = visibility
                                        .translation("slice.configuration.visibility.hideSlotNumber")
                                        .define("hideSlotNumber", false);
                                
                                this.hideSlotSprite = visibility
                                        .translation("slice.configuration.visibility.hideSlotSprite")
                                        .define("hideSlotSprite", false);
                        
                        visibility.pop();
                }

                this.counterclockwiseRotation = display
                        .translation("slice.configuration.counterclockwiseRotation")
                        .define("counterclockwiseRotation", false);   

                display.pop();
        }     

        {
                var behaviour = builder
                        .translation("slice.configuration.category.behaviour")
                        .push("behaviour");

                        this.toggleKeybind = behaviour
                                .translation("slice.configuration.behaviour.toggleKeybind")
                                .define("toggleKeybind", false);  

                behaviour.pop();
        }


        {
                var misc = builder
                        .translation("slice.configuration.category.misc")
                        .push("misc");

                        this.disableScrollingOnHotbar = misc
                                .translation("slice.configuration.disableScrollingOnHotbar")
                                .define("disableScrollingOnHotbar", false);    

                misc.pop();
        }
    }

    static {
        Pair<Config, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Config::new);
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
}