package com.tomkovic.slice;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;

import org.apache.commons.lang3.tuple.Pair;

public class Config {
    public static final Config CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    // Display
    
        // Display / size
        public final ModConfigSpec.IntValue radialMenuRadius;
        public final ModConfigSpec.IntValue itemSize;
        public final ModConfigSpec.IntValue slotSize;

        // Display / Visibility
        public final ModConfigSpec.BooleanValue backgroundBlur;
        public final ModConfigSpec.BooleanValue hideUnusedSlots;
        public final ModConfigSpec.BooleanValue hideSlotNumber;
        public final ModConfigSpec.BooleanValue hideSlotSprite;

    public final ModConfigSpec.BooleanValue animationsEnabled;
    public final ModConfigSpec.BooleanValue soundeffectsEnabled;
    public final ModConfigSpec.IntValue maxAngle;
    public final ModConfigSpec.BooleanValue counterclockwiseRotation;

    // Behaviour
    public final ModConfigSpec.BooleanValue toggleKeybind;
    public final ModConfigSpec.BooleanValue quickSwitch;
    public final ModConfigSpec.BooleanValue clickToSelect;
    public final ModConfigSpec.BooleanValue showTooltip;

    // Misc
    public final BooleanValue disableScrollingOnHotbar;

    private Config(ModConfigSpec.Builder builder) {

        var display = builder
                .translation("slice.configuration.category.display")
                .push("display");

                var size = builder
                        .translation("slice.configuration.category.display.size")
                        .push("size");

                        this.radialMenuRadius = size
                                .translation("slice.configuration.display.size.radialMenuRadius")
                                .defineInRange("radialMenuRadius", 75, 40, 200);

                        this.itemSize = size
                                .translation("slice.configuration.display.size.itemSize")
                                .defineInRange("itemSize", 16, 1, 64);
                        
                        this.slotSize = size
                                .translation("slice.configuration.display.size.slotSize")
                                .defineInRange("slotSize", 32, 16, 64);

                size.pop();

                var visibility = builder
                        .translation("slice.configuration.category.display.visibility")
                        .push("visibility");
                
                        this.backgroundBlur = visibility
                                .translation("slice.configuration.display.visibility.backgroundBlur")
                                .define("backgroundBlur", true);
                
                        this.hideUnusedSlots = visibility
                                .translation("slice.configuration.display.visibility.hideUnusedSlots")
                                .define("hideUnusedSlots", false);
                        
                        this.hideSlotNumber = visibility
                                .translation("slice.configuration.display.visibility.hideSlotNumber")
                                .define("hideSlotNumber", false);
                        
                        this.hideSlotSprite = visibility
                                .translation("slice.configuration.display.visibility.hideSlotSprite")
                                .define("hideSlotSprite", false);
                
                visibility.pop();

                this.animationsEnabled = display
                        .translation("slice.configuration.display.enableAnimations")
                        .define("enableAnimations", true);

                this.soundeffectsEnabled = display
                        .translation("slice.configuration.display.soundEffects")
                        .define("soundEffects", true);

                this.maxAngle = display
                        .translation("slice.configuration.display.maxAngle")
                        .defineInRange("maxAngle", 360, 0, 360);

                this.counterclockwiseRotation = display
                        .translation("slice.configuration.display.counterclockwiseRotation")
                        .define("counterclockwiseRotation", false);   

        display.pop();



        var behaviour = builder
                .translation("slice.configuration.category.behaviour")
                .push("behaviour");

                this.toggleKeybind = behaviour
                        .translation("slice.configuration.behaviour.toggleKeybind")
                        .define("toggleKeybind", false);

                this.quickSwitch = behaviour
                        .translation("slice.configuration.behaviour.quickSwitch")
                        .define("quickSwitch", true);

                this.clickToSelect = behaviour
                        .translation("slice.configuration.behaviour.clickToSelect")
                        .define("clickToSelect", false);

                this.showTooltip = behaviour
                        .translation("slice.configuration.behaviour.showTooltip")
                        .define("showTooltip", true);

        behaviour.pop();



        var misc = builder
                .translation("slice.configuration.category.misc")
                .push("misc");

                this.disableScrollingOnHotbar = misc
                        .translation("slice.configuration.misc.disableScrollingOnHotbar")
                        .define("disableScrollingOnHotbar", false);    

        misc.pop();
    }

    static {
        Pair<Config, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Config::new);
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
}