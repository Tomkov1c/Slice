package com.tomkovic.slice.handlers;

import com.mojang.blaze3d.platform.InputConstants;
import com.tomkovic.slice.Config;
import com.tomkovic.slice.KeyBindings;
import com.tomkovic.slice.RadialMenuRenderer;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

public class RadialMenuHandler {
    private boolean isMenuOpen = false;
    private boolean keyPreviouslyPressed = false;
    private boolean keyProcessed = false; 
    public static final RadialMenuRenderer renderer = new RadialMenuRenderer();
    private float savedYaw = 0;
    private float savedPitch = 0;
    private static boolean isToggleEnabled = Config.CONFIG.toggleKeybind.getDefault();
    
    public static void updateFromConfig() { 
        isToggleEnabled = Config.CONFIG.toggleKeybind.getAsBoolean();
    }
    
    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;
        
        if (KeyBindings.isMouseButton()) return;
        
        handleInputToggle(mc);
    }
    
    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseButton.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;
        
        if (KeyBindings.isMouseButton() && event.getButton() == KeyBindings.getMouseButton()) {
            event.setCanceled(true);
            
            boolean isPress = event.getAction() == InputConstants.PRESS;
            boolean isRelease = event.getAction() == InputConstants.RELEASE;
            
            if (isToggleEnabled) {
                // Toggle mode
                if (isPress && !keyProcessed) {
                    if (!isMenuOpen) {
                        openMenu(mc);
                    } else {
                        closeMenu(mc);
                    }
                    keyProcessed = true;
                } else if (isRelease) {
                    keyProcessed = false;
                }
            } else {
                // Hold mode
                if (isPress) {
                    if (!isMenuOpen) openMenu(mc);
                } else if (isRelease) {
                    if (isMenuOpen) closeMenu(mc);
                }
            }
            return;
        }
        
        if (isMenuOpen) {
            event.setCanceled(true);
            if (RadialMenuRenderer.clickToSelect
                && event.getButton() == 0
                && event.getAction() == InputConstants.PRESS) {
                RadialMenuRenderer renderer = RadialMenuHandler.renderer;
                renderer.selectHoveredSlot();
                
                if (isToggleEnabled) {
                    isMenuOpen = true;
                    keyProcessed = true;
                } else {
                    closeMenu(mc);
                }
            }
        }
    }
    
    private void handleInputToggle(Minecraft mc) {
        boolean isKeyDown = KeyBindings.isOpenRadialMenuPressed();
        
        if (isToggleEnabled) {
            // Toggle mode
            if (isKeyDown && !keyPreviouslyPressed && !keyProcessed) {
                keyPreviouslyPressed = true;
                if (!isMenuOpen) {
                    openMenu(mc);
                } else {
                    closeMenu(mc);
                }
            } else if (!isKeyDown) {
                keyPreviouslyPressed = false;
                keyProcessed = false;
            }
        } else {
            // Hold mode
            if (isKeyDown) {
                if (!isMenuOpen) openMenu(mc);
            } else {
                if (isMenuOpen) closeMenu(mc);
            }
        }
    }
    
    private void openMenu(Minecraft mc) {
        isMenuOpen = true;
        savedYaw = mc.player.getYRot();
        savedPitch = mc.player.getXRot();
        mc.mouseHandler.releaseMouse();
        renderer.onMenuOpen();
    }
    
    private void closeMenu(Minecraft mc) {
        isMenuOpen = false;
        mc.mouseHandler.grabMouse();
        renderer.onMenuClose();
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