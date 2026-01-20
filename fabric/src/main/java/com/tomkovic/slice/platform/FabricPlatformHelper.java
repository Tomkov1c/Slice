package com.tomkovic.slice.platform;

import com.tomkovic.slice.Constants;
import com.tomkovic.slice.SliceClient;
import com.tomkovic.slice.platform.services.IPlatformHelper;
import java.lang.reflect.Field;
import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.Minecraft;

import org.lwjgl.glfw.GLFW;


public class FabricPlatformHelper implements IPlatformHelper {

    private static Field windowHandleField = null;

    @Override
    public void setSelectedSlot(int index) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || index < 0 || index > 8) return;

        mc.player.getInventory().selected = index;
    }

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
                windowHandleField = Window.class.getDeclaredField("handle");
                windowHandleField.setAccessible(true);
            }

            long windowHandle = windowHandleField.getLong(window);
            GLFW.glfwSetCursorPos(windowHandle, centerX, centerY);
        } catch (Exception e) {
            Constants.LOG.error("Failed to center cursor", e);
        }
    }
}
