package com.ansi.slice.platform;

import org.lwjgl.glfw.GLFW;
import java.lang.reflect.Field;


import com.mojang.blaze3d.platform.Window;
import com.ansi.slice.Constants;
import com.ansi.slice.SliceClient;
import com.ansi.slice.platform.services.IPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import com.ansi.slice.handlers.RadialMenuHandler;

public class NeoForgePlatformHelper implements IPlatformHelper {

    private static Field windowHandleField = null;

    private Minecraft mc = RadialMenuHandler.mc();

    @Override
    public void setSelectedSlot(int index) { mc.player.getInventory().selected = index; }

    @Override
    public void renderMenu() {
        if (!SliceClient.renderer.isRendering) {
            SliceClient.renderer.isRendering = true;
            SliceClient.renderer.hasRenderedOnce = false;
            SliceClient.renderer.onMenuOpen();
        }
    }

    @Override
    public void derenderMenu() {
        if (SliceClient.renderer.isRendering) {
            SliceClient.renderer.isRendering = false;
            SliceClient.renderer.hasRenderedOnce = false;
            SliceClient.renderer.onMenuClose();
            SliceClient.renderer.clearCache();
        }
    }

    @Override
    public void centerCursor() {
        Minecraft mc = Minecraft.getInstance();
        Window window = mc.getWindow();

        double centerX = window.getScreenWidth() / 2.0;
        double centerY = window.getScreenHeight() / 2.0;

        try {
            if (windowHandleField == null) {
                windowHandleField = Window.class.getDeclaredField("window");
                windowHandleField.setAccessible(true);
            }

            long windowHandle = windowHandleField.getLong(window);
            GLFW.glfwSetCursorPos(windowHandle, centerX, centerY);
        } catch (Exception e) {
            Constants.LOG.error("Failed to center cursor", e);
        }
    }
}
