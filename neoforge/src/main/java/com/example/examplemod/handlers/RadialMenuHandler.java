package com.example.examplemod.handlers;

import com.example.examplemod.KeyBindings;
import com.example.examplemod.RadialMenuRenderer;

import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

public class RadialMenuHandler {
    private boolean isMenuOpen = false;
    public static final RadialMenuRenderer renderer = new RadialMenuRenderer();
    private float savedYaw = 0;
    private float savedPitch = 0;

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) {
            return;
        }

        if (KeyBindings.OPEN_RADIAL_MENU.isDown()) {
            if (!isMenuOpen) {
                isMenuOpen = true;
                if (mc.player != null) {
                    savedYaw = mc.player.getYRot();
                    savedPitch = mc.player.getXRot();
                }
                // Ungrab mouse 
                mc.mouseHandler.releaseMouse();
                renderer.onMenuOpen();
            }
        } else {
            if (isMenuOpen) {
                isMenuOpen = false;
                // Grab mouse
                mc.mouseHandler.grabMouse();
                renderer.onMenuClose();
            }
        }
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseButton.Pre event) {
        if (isMenuOpen) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (isMenuOpen) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (isMenuOpen) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                mc.player.setYRot(savedYaw);
                mc.player.setXRot(savedPitch);
                mc.player.yRotO = savedYaw;
                mc.player.xRotO = savedPitch;
            }
        }
    }

    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent.Post event) {
        if (isMenuOpen) {
            renderer.render(event.getGuiGraphics(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
        }
    }

    public boolean isMenuOpen() {
        return isMenuOpen;
    }
}