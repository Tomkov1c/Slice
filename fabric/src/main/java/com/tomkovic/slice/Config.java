package com.tomkovic.slice;

import com.tomkovic.slice.*;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@me.shedaniel.autoconfig.annotation.Config(name = "slice")
public class Config implements ConfigData {

    @ConfigEntry.Category("display")
    @ConfigEntry.Gui.TransitiveObject
    public Display display = new Display();

    @ConfigEntry.Category("behaviour")
    @ConfigEntry.Gui.TransitiveObject
    public Behaviour behaviour = new Behaviour();

    @ConfigEntry.Category("misc")
    @ConfigEntry.Gui.TransitiveObject
    public Misc misc = new Misc();

    public static class Display {

        @ConfigEntry.Gui.CollapsibleObject
        public Size size = new Size();

        @ConfigEntry.Gui.CollapsibleObject
        public Visibility visibility = new Visibility();

        @ConfigEntry.BoundedDiscrete(min = 0, max = 360)
        public int startAngle = 360;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 360)
        public int endAngle = 360;

        public boolean counterclockwiseRotation = false;

        public static class Size {

            @ConfigEntry.BoundedDiscrete(min = 40, max = 200)
            public int radialMenuRadius = 75;

            @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
            public int itemSize = 16;

            @ConfigEntry.BoundedDiscrete(min = 16, max = 64)
            public int slotSize = 32;
        }

        public static class Visibility {

            @ConfigEntry.BoundedDiscrete(min = 0, max = 255)
            public int backgroundDarkenOpacity = 0;

            public boolean hideUnusedSlots = false;

            public boolean hideSlotNumber = false;

            public boolean hideSlotSprite = false;

            @ConfigEntry.Gui.CollapsibleObject
            public DisabledSlots disabledSlots = new DisabledSlots();

            public static class DisabledSlots {

                public boolean disableSlot1 = false;

                public boolean disableSlot2 = false;

                public boolean disableSlot3 = false;

                public boolean disableSlot4 = false;

                public boolean disableSlot5 = false;

                public boolean disableSlot6 = false;

                public boolean disableSlot7 = false;

                public boolean disableSlot8 = false;

                public boolean disableSlot9 = false;
            }
        }
    }

    public static class Behaviour {

        public boolean toggleKeybind = false;

        public boolean clickToSelect = false;

        public boolean recenterOnSelect = false;

        public boolean closeOnSelect = true;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 1000)
        public int innerDeadzone = 72;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 1000)
        public int outerDeadzone = 47;
    }

    public static class Misc {

        public boolean disableScrollingOnHotbar = false;
    }

    public void pushConfigToGlobal() {
        // Display / Size
        GlobalConfig.MENU_RADIUS = this.display.size.radialMenuRadius;
        GlobalConfig.ITEM_SIZE = this.display.size.itemSize;
        GlobalConfig.SLOT_SIZE = this.display.size.slotSize;

        // Display / Visibility
        GlobalConfig.BACKGROUND_OPACITY = this.display.visibility.backgroundDarkenOpacity;
        GlobalConfig.HIDE_UNUSED_SLOTS = this.display.visibility.hideUnusedSlots;
        GlobalConfig.HIDE_SLOT_NUMBER = this.display.visibility.hideSlotNumber;
        GlobalConfig.HIDE_SLOT_SPRITE = this.display.visibility.hideSlotSprite;

        // Display / Visibility / Disable Slots
        GlobalConfig.DISABLE_SLOT_1 = this.display.visibility.disabledSlots.disableSlot1;
        GlobalConfig.DISABLE_SLOT_2 = this.display.visibility.disabledSlots.disableSlot2;
        GlobalConfig.DISABLE_SLOT_3 = this.display.visibility.disabledSlots.disableSlot3;
        GlobalConfig.DISABLE_SLOT_4 = this.display.visibility.disabledSlots.disableSlot4;
        GlobalConfig.DISABLE_SLOT_5 = this.display.visibility.disabledSlots.disableSlot5;
        GlobalConfig.DISABLE_SLOT_6 = this.display.visibility.disabledSlots.disableSlot6;
        GlobalConfig.DISABLE_SLOT_7 = this.display.visibility.disabledSlots.disableSlot7;
        GlobalConfig.DISABLE_SLOT_8 = this.display.visibility.disabledSlots.disableSlot8;
        GlobalConfig.DISABLE_SLOT_9 = this.display.visibility.disabledSlots.disableSlot9;

        // Display / Angles
        GlobalConfig.START_ANGLE = this.display.startAngle;
        GlobalConfig.END_ANGLE = this.display.endAngle;
        GlobalConfig.REVERSE_ROTATION = this.display.counterclockwiseRotation;

        // Behaviour
        GlobalConfig.TOGGLE_KEYBIND = this.behaviour.toggleKeybind;
        GlobalConfig.CLICK_TO_SELECT = this.behaviour.clickToSelect;
        GlobalConfig.RECENTER_ON_SELECT = this.behaviour.recenterOnSelect;
        GlobalConfig.CLOSE_ON_SELECT = this.behaviour.closeOnSelect;
        GlobalConfig.INNER_DEADZONE = this.behaviour.innerDeadzone;
        GlobalConfig.OUTER_DEADZONE = this.behaviour.outerDeadzone;

        // Misc
        GlobalConfig.DISABLE_HOTBAR_SCROLLING = this.misc.disableScrollingOnHotbar;

    }
}
