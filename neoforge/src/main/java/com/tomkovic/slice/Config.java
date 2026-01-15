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
        public final ModConfigSpec.IntValue backgroundDarkenOpacity;
        public final ModConfigSpec.BooleanValue hideUnusedSlots;
        public final ModConfigSpec.BooleanValue hideSlotNumber;
        public final ModConfigSpec.BooleanValue hideSlotSprite;

                // Display / Visibility / Disable Slots
                public final ModConfigSpec.BooleanValue disableSlot1;
                public final ModConfigSpec.BooleanValue disableSlot2;
                public final ModConfigSpec.BooleanValue disableSlot3;
                public final ModConfigSpec.BooleanValue disableSlot4;
                public final ModConfigSpec.BooleanValue disableSlot5;
                public final ModConfigSpec.BooleanValue disableSlot6;
                public final ModConfigSpec.BooleanValue disableSlot7;
                public final ModConfigSpec.BooleanValue disableSlot8;
                public final ModConfigSpec.BooleanValue disableSlot9;


    public final ModConfigSpec.IntValue startAngle;
    public final ModConfigSpec.IntValue endAngle;
    public final ModConfigSpec.BooleanValue counterclockwiseRotation;

    // Behaviour
    public final ModConfigSpec.BooleanValue toggleKeybind;
    public final ModConfigSpec.BooleanValue clickToSelect;
    public final ModConfigSpec.BooleanValue closeOnSelect;
    public final ModConfigSpec.IntValue innerDeadzone;
    public final ModConfigSpec.IntValue outerDeadzone;

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
                
                        backgroundDarkenOpacity = visibility
                                .translation("slice.configuration.display.visibility.backgroundDarkenOpacity")
                                .defineInRange("backgroundDarkenOpacity", 0, 0, 255);
                
                        this.hideUnusedSlots = visibility
                                .translation("slice.configuration.display.visibility.hideUnusedSlots")
                                .define("hideUnusedSlots", false);
                        
                        this.hideSlotNumber = visibility
                                .translation("slice.configuration.display.visibility.hideSlotNumber")
                                .define("hideSlotNumber", false);
                        
                        this.hideSlotSprite = visibility
                                .translation("slice.configuration.display.visibility.hideSlotSprite")
                                .define("hideSlotSprite", false);

                        var disabledSlots = builder
                                .translation("slice.configuration.category.display.visibility.disabledSlots")
                                .push("visibility");

                                this.disableSlot1 = disabledSlots
                                        .translation("slice.configuration.display.visibility.disabledSlots.disableSlot1")
                                        .define("disableSlot1", false);

                                this.disableSlot2 = disabledSlots
                                        .translation("slice.configuration.display.visibility.disabledSlots.disableSlot2")
                                        .define("disableSlot2", false);

                                this.disableSlot3 = disabledSlots
                                        .translation("slice.configuration.display.visibility.disabledSlots.disableSlot3")
                                        .define("disableSlot3", false);

                                this.disableSlot4 = disabledSlots
                                        .translation("slice.configuration.display.visibility.disabledSlots.disableSlot4")
                                        .define("disableSlot4", false);

                                this.disableSlot5 = disabledSlots
                                        .translation("slice.configuration.display.visibility.disabledSlots.disableSlot5")
                                        .define("disableSlot5", false);
                                
                                this.disableSlot6 = disabledSlots
                                        .translation("slice.configuration.display.visibility.disabledSlots.disableSlot6")
                                        .define("disableSlot6", false);

                                this.disableSlot7 = disabledSlots
                                        .translation("slice.configuration.display.visibility.disabledSlots.disableSlot7")
                                        .define("disableSlot7", false);

                                this.disableSlot8 = disabledSlots
                                        .translation("slice.configuration.display.visibility.disabledSlots.disableSlot8")
                                        .define("disableSlot8", false);
                                
                                this.disableSlot9 = disabledSlots
                                        .translation("slice.configuration.display.visibility.disabledSlots.disableSlot9")
                                        .define("disableSlot9", false);


                        disabledSlots.pop();
                
                visibility.pop();

                this.startAngle = display
                        .translation("slice.configuration.display.maxAngle")
                        .defineInRange("startAngle", 360, 0, 360);
                
                this.endAngle = display
                        .translation("slice.configuration.display.endAngle")
                        .defineInRange("endAngle", 360, 0, 360);

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

                this.clickToSelect = behaviour
                        .translation("slice.configuration.behaviour.clickToSelect")
                        .define("clickToSelect", false);

                this.closeOnSelect = behaviour
                        .translation("slice.configuration.behaviour.closeOnSelect")
                        .define("closeOnSelect", true);

                this.innerDeadzone = behaviour
                        .translation("slice.configuration.behaviour.innerDeadzone")
                        .defineInRange("innerDeadzone", 72, 0, 1000);
                
                this.outerDeadzone = behaviour
                        .translation("slice.configuration.behaviour.outerDeadzone")
                        .defineInRange("outerDeadzone", 47, 0, 1000);

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

    public static void pushConfigToGlobal() {
        // Display / Size
        GlobalConfig.radialMenuRadius = CONFIG.radialMenuRadius.get();
        GlobalConfig.itemSize = CONFIG.itemSize.get();
        GlobalConfig.slotSize = CONFIG.slotSize.get();
        
        // Display / Visibility
        GlobalConfig.backgroundDarkenOpacity = CONFIG.backgroundDarkenOpacity.get();
        GlobalConfig.hideUnusedSlots = CONFIG.hideUnusedSlots.get();
        GlobalConfig.hideSlotNumber = CONFIG.hideSlotNumber.get();
        GlobalConfig.hideSlotSprite = CONFIG.hideSlotSprite.get();
        
        // Display / Visibility / Disable Slots
        GlobalConfig.disableSlot1 = CONFIG.disableSlot1.get();
        GlobalConfig.disableSlot2 = CONFIG.disableSlot2.get();
        GlobalConfig.disableSlot3 = CONFIG.disableSlot3.get();
        GlobalConfig.disableSlot4 = CONFIG.disableSlot4.get();
        GlobalConfig.disableSlot5 = CONFIG.disableSlot5.get();
        GlobalConfig.disableSlot6 = CONFIG.disableSlot6.get();
        GlobalConfig.disableSlot7 = CONFIG.disableSlot7.get();
        GlobalConfig.disableSlot8 = CONFIG.disableSlot8.get();
        GlobalConfig.disableSlot9 = CONFIG.disableSlot9.get();
        
        // Display / Angles
        GlobalConfig.startAngle = CONFIG.startAngle.get();
        GlobalConfig.endAngle = CONFIG.endAngle.get();
        GlobalConfig.counterclockwiseRotation = CONFIG.counterclockwiseRotation.get();
        
        // Behaviour
        GlobalConfig.toggleKeybind = CONFIG.toggleKeybind.get();
        GlobalConfig.clickToSelect = CONFIG.clickToSelect.get();
        GlobalConfig.closeOnSelect = CONFIG.closeOnSelect.get();
        GlobalConfig.innerDeadzone = CONFIG.innerDeadzone.get();
        GlobalConfig.outerDeadzone = CONFIG.outerDeadzone.get();
        
        // Misc
        GlobalConfig.disableScrollingOnHotbar = CONFIG.disableScrollingOnHotbar.get();
    }
}