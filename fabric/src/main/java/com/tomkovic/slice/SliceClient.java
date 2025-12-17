package com.tomkovic.slice;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class SliceClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(KeyBindings.OPEN_RADIAL_MENU);

        Constants.LOG.error("Regiszered keybind");
    }
}
