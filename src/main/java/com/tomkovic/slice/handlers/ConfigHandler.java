package com.tomkovic.slice.handlers;

import com.tomkovic.slice.Config;
import com.tomkovic.slice.Constants;
import com.tomkovic.slice.RadialMenuRenderer;

public class ConfigHandler {

    public static void getCurrentConfig() {
        RadialMenuHandler.updateFromConfig();
        RadialMenuRenderer.updateFromConfig();
    }

}
