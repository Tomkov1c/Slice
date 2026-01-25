package com.tomkovic.slice.platform;

import com.tomkovic.slice.Constants;
import com.tomkovic.slice.SliceClient;
import com.tomkovic.slice.handlers.RadialMenuHandler;
import com.tomkovic.slice.platform.services.IPlatformHelper;
import java.lang.reflect.Field;
import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.Minecraft;

import org.lwjgl.glfw.GLFW;

public class FabricPlatformHelper implements IPlatformHelper {

    private static Field windowHandleField = null;

    private static Field cachedHandleField = null;

    private Minecraft mc = RadialMenuHandler.mc();

    @Override
    public void setSelectedSlot(int index) {
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
        Window window = mc.getWindow();

        try {
            if (cachedHandleField == null) {
                for (Field f : Window.class.getDeclaredFields()) {
                    if (f.getType() == long.class) {
                        f.setAccessible(true);
                        cachedHandleField = f;
                        break;
                    }
                }
            }

            if (cachedHandleField != null) {
                long handle = cachedHandleField.getLong(window);
                GLFW.glfwSetCursorPos(handle, window.getScreenWidth() / 2.0, window.getScreenHeight() / 2.0);
            }
        } catch (Exception e) {
            Constants.LOG.error("Failed to center cursor via reflection", e);
        }
    }
}
