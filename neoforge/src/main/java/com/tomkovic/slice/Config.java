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
        GlobalConfig.MENU_RADIUS = CONFIG.radialMenuRadius.get();
        GlobalConfig.ITEM_SIZE = CONFIG.itemSize.get();
        GlobalConfig.SLOT_SIZE = CONFIG.slotSize.get();
        
        // Display / Visibility
        GlobalConfig.BACKGROUND_OPACITY = CONFIG.backgroundDarkenOpacity.get();
        GlobalConfig.HIDE_UNUSED_SLOTS = CONFIG.hideUnusedSlots.get();
        GlobalConfig.HIDE_SLOT_NUMBER = CONFIG.hideSlotNumber.get();
        GlobalConfig.HIDE_SLOT_SPRITE = CONFIG.hideSlotSprite.get();
        
        // Display / Visibility / Disable Slots
        GlobalConfig.DISABLE_SLOT_1 = CONFIG.disableSlot1.get();
        GlobalConfig.DISABLE_SLOT_2 = CONFIG.disableSlot2.get();
        GlobalConfig.DISABLE_SLOT_3 = CONFIG.disableSlot3.get();
        GlobalConfig.DISABLE_SLOT_4 = CONFIG.disableSlot4.get();
        GlobalConfig.DISABLE_SLOT_5 = CONFIG.disableSlot5.get();
        GlobalConfig.DISABLE_SLOT_6 = CONFIG.disableSlot6.get();
        GlobalConfig.DISABLE_SLOT_7 = CONFIG.disableSlot7.get();
        GlobalConfig.DISABLE_SLOT_8 = CONFIG.disableSlot8.get();
        GlobalConfig.DISABLE_SLOT_9 = CONFIG.disableSlot9.get();
        
        // Display / Angles
        GlobalConfig.START_ANGLE = CONFIG.startAngle.get();
        GlobalConfig.END_ANGLE = CONFIG.endAngle.get();
        GlobalConfig.REVERSE_ROTATION = CONFIG.counterclockwiseRotation.get();
        
        // Behaviour
        GlobalConfig.TOGGLE_KEYBIND = CONFIG.toggleKeybind.get();
        GlobalConfig.CLICK_TO_SELECT = CONFIG.clickToSelect.get();
        GlobalConfig.CLOSE_ON_SELECT = CONFIG.closeOnSelect.get();
        GlobalConfig.INNER_DEADZONE = CONFIG.innerDeadzone.get();
        GlobalConfig.OUTER_DEADZONE = CONFIG.outerDeadzone.get();
        
        // Misc
        GlobalConfig.DISABLE_HOTBAR_SCROLLING = CONFIG.disableScrollingOnHotbar.get();
    }
}