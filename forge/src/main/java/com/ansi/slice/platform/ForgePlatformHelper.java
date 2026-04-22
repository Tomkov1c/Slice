package com.ansi.slice.platform;

import com.ansi.slice.Constants;
import com.ansi.slice.platform.services.IPlatformHelper;
import com.mojang.blaze3d.platform.Window;
import com.ansi.slice.SliceClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import java.lang.reflect.Field;

import org.lwjgl.glfw.GLFW;

public class ForgePlatformHelper implements IPlatformHelper {

    private static Field selectedField;

    private static Field windowHandleField = null;

    @Override
    public void setSelectedSlot(int index) {
        LocalPlayer player = Constants.MINECRAFT.player;

        try {
            if (selectedField == null) {
                selectedField = player.getInventory().getClass().getDeclaredField("selected");
                selectedField.setAccessible(true);
            }
            selectedField.setInt(player.getInventory(), index);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
