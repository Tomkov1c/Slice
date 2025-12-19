package com.tomkovic.slice.handlers;

import com.mojang.blaze3d.platform.InputConstants;
import com.tomkovic.slice.Config;
import com.tomkovic.slice.KeyBindings;
import com.tomkovic.slice.RadialMenuRenderer;
import com.tomkovic.slice.RadialMenuState;

import net.minecraft.client.Minecraft;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import me.shedaniel.autoconfig.AutoConfig;

public class RadialMenuHandler {

    public static final RadialMenuRenderer renderer = new RadialMenuRenderer();

    private static boolean isToggleEnabled;
    private static boolean clickToSelect;
    private static boolean closeOnSelect;
    private static boolean disableScrollingOnHotbar;

    public static void updateFromConfig() {
        Config config = AutoConfig.getConfigHolder(Config.class).getConfig();

        isToggleEnabled = config.behaviour.toggleKeybind;
        clickToSelect = config.behaviour.clickToSelect;
        closeOnSelect = config.behaviour.closeOnSelect;
        disableScrollingOnHotbar = config.misc.disableScrollingOnHotbar;

        RadialMenuRenderer.updateFromConfig();
    }

    public static void register() {
        Minecraft mc = Minecraft.getInstance();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (mc.player == null || mc.screen != null) return;

            if (!KeyBindings.isMouseButton()) {
                boolean pressed = KeyBindings.isOpenRadialMenuPressed();
                RadialMenuState.handleMenuToggle(pressed, false, isToggleEnabled,
                    () -> renderer.onMenuOpen(), () -> renderer.onMenuClose()
                );
            }
        });

        // Mouse button handling
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (mc.player == null || mc.screen != null) return;

            if (KeyBindings.isMouseButton() && KeyBindings.isOpenRadialMenuPressed())
                RadialMenuState.handleMenuToggle(true, false, isToggleEnabled,
                    () -> renderer.onMenuOpen(), () -> renderer.onMenuClose()
                );

            if (RadialMenuState.isMenuOpen) handleMenuClick(mc);
        });

        HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
            if (RadialMenuState.isMenuOpen) renderer.render(graphics, tickDelta.getGameTimeDeltaPartialTick(false));
        });
    }

    private static void handleMenuClick(Minecraft mc) {
        if (clickToSelect && RadialMenuRenderer.clickToSelect) {
            renderer.selectHoveredSlot();

            if (closeOnSelect) RadialMenuState.closeMenu(() -> renderer.onMenuClose());
        }
    }
}
