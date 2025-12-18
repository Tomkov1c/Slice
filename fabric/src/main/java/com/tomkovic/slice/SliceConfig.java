package com.tomkovic.slice;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "slice")
public class SliceConfig implements ConfigData {
    
    @ConfigEntry.Gui.Tooltip
    public boolean enableFeature = true;
    
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int someValue = 50;
    
    @ConfigEntry.Gui.CollapsibleObject
    public AdvancedSettings advanced = new AdvancedSettings();
    
    public static class AdvancedSettings {
        public boolean debugMode = false;
        public String customText = "Hello World";
    }
}