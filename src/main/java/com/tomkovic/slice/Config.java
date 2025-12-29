package com.tomkovic.slice;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

import com.tomkovic.slice.handlers.ConfigHandler;

public class Config {
    public static Configuration config;
    
    // Display
    
        // Display / size
        public static int radialMenuRadius;
        public static int itemSize;
        public static int slotSize;

        // Display / Visibility
        public static int backgroundDarkenOpacity;
        public static boolean hideUnusedSlots;
        public static boolean hideSlotNumber;
        public static boolean hideSlotSprite;

                // Display / Visibility / Disable Slots
                public static boolean disableSlot1;
                public static boolean disableSlot2;
                public static boolean disableSlot3;
                public static boolean disableSlot4;
                public static boolean disableSlot5;
                public static boolean disableSlot6;
                public static boolean disableSlot7;
                public static boolean disableSlot8;
                public static boolean disableSlot9;

    public static int startAngle;
    public static int endAngle;
    public static boolean counterclockwiseRotation;

    // Behaviour
    public static boolean toggleKeybind;
    public static boolean clickToSelect;
    public static boolean closeOnSelect;
    public static int innerDeadzone;
    public static int outerDeadzone;

    // Misc
    public static boolean disableScrollingOnHotbar;

    public static void init(File configFile) {
        config = new Configuration(configFile);
        syncConfig();
    }

    public static void syncConfig() {
        try {
            config.load();

            // Display / Size
            radialMenuRadius = config.getInt("radialMenuRadius", "display.size", 75, 40, 200, "Radius of the radial menu");
            itemSize = config.getInt("itemSize", "display.size", 16, 1, 64, "Size of items in the radial menu");
            slotSize = config.getInt("slotSize", "display.size", 32, 16, 64, "Size of slot backgrounds");

            // Display / Visibility
            backgroundDarkenOpacity = config.getInt("backgroundDarkenOpacity", "display.visibility", 0, 0, 255, "Opacity of background darkening effect");
            hideUnusedSlots = config.getBoolean("hideUnusedSlots", "display.visibility", false, "Hide slots with no items");
            hideSlotNumber = config.getBoolean("hideSlotNumber", "display.visibility", false, "Hide slot numbers");
            hideSlotSprite = config.getBoolean("hideSlotSprite", "display.visibility", false, "Hide slot sprite backgrounds");

            // Display / Visibility / Disabled Slots
            disableSlot1 = config.getBoolean("disableSlot1", "display.visibility.disabledSlots", false, "Disable slot 1");
            disableSlot2 = config.getBoolean("disableSlot2", "display.visibility.disabledSlots", false, "Disable slot 2");
            disableSlot3 = config.getBoolean("disableSlot3", "display.visibility.disabledSlots", false, "Disable slot 3");
            disableSlot4 = config.getBoolean("disableSlot4", "display.visibility.disabledSlots", false, "Disable slot 4");
            disableSlot5 = config.getBoolean("disableSlot5", "display.visibility.disabledSlots", false, "Disable slot 5");
            disableSlot6 = config.getBoolean("disableSlot6", "display.visibility.disabledSlots", false, "Disable slot 6");
            disableSlot7 = config.getBoolean("disableSlot7", "display.visibility.disabledSlots", false, "Disable slot 7");
            disableSlot8 = config.getBoolean("disableSlot8", "display.visibility.disabledSlots", false, "Disable slot 8");
            disableSlot9 = config.getBoolean("disableSlot9", "display.visibility.disabledSlots", false, "Disable slot 9");

            // Display
            startAngle = config.getInt("startAngle", "display", 360, 0, 360, "Starting angle of the radial menu");
            endAngle = config.getInt("endAngle", "display", 360, 0, 360, "Ending angle of the radial menu");
            counterclockwiseRotation = config.getBoolean("counterclockwiseRotation", "display", false, "Rotate menu counter-clockwise");

            // Behaviour
            toggleKeybind = config.getBoolean("toggleKeybind", "behaviour", false, "Toggle menu instead of hold");
            clickToSelect = config.getBoolean("clickToSelect", "behaviour", false, "Click to select instead of release");
            closeOnSelect = config.getBoolean("closeOnSelect", "behaviour", true, "Close menu after selecting");
            innerDeadzone = config.getInt("innerDeadzone", "behaviour", 72, 0, 1000, "Inner deadzone radius");
            outerDeadzone = config.getInt("outerDeadzone", "behaviour", 47, 0, 1000, "Outer deadzone radius");

            // Misc
            disableScrollingOnHotbar = config.getBoolean("disableScrollingOnHotbar", "misc", false, "Disable scrolling on hotbar");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
        
        ConfigHandler.getCurrentConfig();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(Constants.MOD_ID)) {
            syncConfig();
        }
    }
}