package com.tomkovic.slice.platform;

import com.mojang.blaze3d.platform.Window;
import com.tomkovic.slice.Constants;
import com.tomkovic.slice.SliceClient;
import com.tomkovic.slice.platform.services.IPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import java.lang.reflect.Field;
import java.util.Objects;
import com.tomkovic.slice.handlers.RadialMenuHandler;

import org.lwjgl.glfw.GLFW;

public class ForgePlatformHelper implements IPlatformHelper {

    private static Field selectedField;

    private static Field windowHandleField = null;

    private Minecraft mc = RadialMenuHandler.mc();

    @Override
    public void setSelectedSlot(int index) {
        LocalPlayer player = mc.player;

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
