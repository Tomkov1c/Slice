package com.tomkovic.slice;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import org.apache.commons.lang3.tuple.Pair;

import com.tomkovic.slice.handlers.ConfigHandler;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static final Config CONFIG;
    public static final ForgeConfigSpec CONFIG_SPEC;

    // Display
    
        // Display / size
        public final ForgeConfigSpec.IntValue radialMenuRadius;
        public final ForgeConfigSpec.IntValue itemSize;
        public final ForgeConfigSpec.IntValue slotSize;

        // Display / Visibility
        public final ForgeConfigSpec.IntValue backgroundDarkenOpacity;
        public final ForgeConfigSpec.BooleanValue hideUnusedSlots;
        public final ForgeConfigSpec.BooleanValue hideSlotNumber;
        public final ForgeConfigSpec.BooleanValue hideSlotSprite;

                // Display / Visibility / Disable Slots
                public final ForgeConfigSpec.BooleanValue disableSlot1;
                public final ForgeConfigSpec.BooleanValue disableSlot2;
                public final ForgeConfigSpec.BooleanValue disableSlot3;
                public final ForgeConfigSpec.BooleanValue disableSlot4;
                public final ForgeConfigSpec.BooleanValue disableSlot5;
                public final ForgeConfigSpec.BooleanValue disableSlot6;
                public final ForgeConfigSpec.BooleanValue disableSlot7;
                public final ForgeConfigSpec.BooleanValue disableSlot8;
                public final ForgeConfigSpec.BooleanValue disableSlot9;


    public final ForgeConfigSpec.IntValue startAngle;
    public final ForgeConfigSpec.IntValue endAngle;
    public final ForgeConfigSpec.BooleanValue counterclockwiseRotation;

    // Behaviour
    public final ForgeConfigSpec.BooleanValue toggleKeybind;
    public final ForgeConfigSpec.BooleanValue clickToSelect;
    public final ForgeConfigSpec.BooleanValue closeOnSelect;
    public final ForgeConfigSpec.IntValue innerDeadzone;
    public final ForgeConfigSpec.IntValue outerDeadzone;

    // Misc
    public final BooleanValue disableScrollingOnHotbar;

    private Config(ForgeConfigSpec.Builder builder) {

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
                                .push("disabledSlots");

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
        Pair<Config, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Config::new);
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getModId().equals(Constants.MOD_ID)) ConfigHandler.getCurrentConfig();
    }
}