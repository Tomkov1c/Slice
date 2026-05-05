package com.ansi.slice.platform;

import com.ansi.slice.SliceClient;

import java.lang.reflect.Field;

import com.ansi.slice.Constants;
import com.ansi.slice.platform.services.IPlatformHelper;
import com.mojang.blaze3d.platform.Window;


import org.lwjgl.glfw.GLFW;

public class FabricPlatformHelper implements IPlatformHelper {

    private static Field windowHandleField = null;

    @Override
    public void setSelectedSlot(int index) {
        Constants.MINECRAFT.player.getInventory().selected = index;
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
        Window window = Constants.MINECRAFT.getWindow();

        try {
            if (windowHandleField == null) {
                for (Field f : Window.class.getDeclaredFields()) {
                    if (f.getType() == long.class) {
                        f.setAccessible(true);
                        windowHandleField = f;
                        break;
                    }
                }
            }

            if (windowHandleField != null) {
                long handle = windowHandleField.getLong(window);
                GLFW.glfwSetCursorPos(handle, window.getScreenWidth() / 2.0, window.getScreenHeight() / 2.0);
            }
        } catch (Exception e) {
            Constants.LOG.error("Failed to center cursor via reflection", e);
        }
    }
}
