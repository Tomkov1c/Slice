package com.tomkovic.slice.handlers;

import com.tomkovic.slice.Config;
import com.tomkovic.slice.Constants;
import com.tomkovic.slice.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class RadialMenuHandler {
    private static boolean isToggleEnabled = Config.toggleKeybind;
    private static boolean clickToSelect = Config.clickToSelect;
    private static boolean closeOnSelect = Config.closeOnSelect;
    private static boolean disableScrollingOnHotbar = Config.disableScrollingOnHotbar;
    
    private static boolean wasKeyDown = false;

    public static void updateFromConfig() { 
        isToggleEnabled = Config.toggleKeybind;
        clickToSelect = Config.clickToSelect;
        closeOnSelect = Config.closeOnSelect;
        disableScrollingOnHotbar = Config.disableScrollingOnHotbar;
    }
    
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.currentScreen != null) return;
        
        if (KeyBindings.isMouseButton()) return;
        
        boolean isKeyDown = KeyBindings.OPEN_RADIAL_MENU.isKeyDown();
        boolean isPress = isKeyDown && !wasKeyDown;
        boolean isRelease = !isKeyDown && wasKeyDown;
        
        if (isPress) {
            sendChatMessage("Radial Menu Key Pressed!");
        }
        
        wasKeyDown = isKeyDown;
    }
    
    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) { 
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.currentScreen != null) return;
        
        int button = Mouse.getEventButton();
        boolean buttonState = Mouse.getEventButtonState();
        
        if (button == -1) return;
        
        if (KeyBindings.isMouseButton() && button == KeyBindings.getMouseButton()) {
            if (buttonState) {
                sendChatMessage("Radial Menu Mouse Button Pressed!");
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseEvent(MouseEvent event) {
    }
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
    }
    
    private void handleMenuClick(int button, boolean pressed) {
    }
    
    private void sendChatMessage(String message) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }
}