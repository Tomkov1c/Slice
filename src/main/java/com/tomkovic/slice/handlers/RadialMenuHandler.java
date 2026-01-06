package com.tomkovic.slice.handlers;

import com.tomkovic.slice.Config;
import com.tomkovic.slice.Constants;
import com.tomkovic.slice.KeyBindings;
import com.tomkovic.slice.RadialMenuRenderer;
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
    
    private RadialMenuRenderer renderer;
    private boolean isMenuOpen = false;
    private boolean isToggled = false;

    public RadialMenuHandler() {
        this.renderer = new RadialMenuRenderer();
    }

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
            if (isToggleEnabled) {
                isToggled = !isToggled;
                if (isToggled) {
                    openMenu();
                } else {
                    closeMenu();
                }
            } else {
                openMenu();
            }
        } else if (isRelease && !isToggleEnabled) {
            closeMenu();
        }

        wasKeyDown = isKeyDown;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseEvent(MouseEvent event) {
        if (!isMenuOpen) return;

        if (event.button != -1) {
            if (clickToSelect) {
                event.setCanceled(true);
                
                if (event.buttonstate) {
                    handleMenuClick(event.button, true);
                }
            } else {
                event.setCanceled(true);
            }
        }

        if (disableScrollingOnHotbar && event.dwheel != 0) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) { 
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.currentScreen != null) return;

        if (isMenuOpen) return;

        int button = Mouse.getEventButton();
        boolean buttonState = Mouse.getEventButtonState();

        if (button == -1) return;

        if (KeyBindings.isMouseButton() && button == KeyBindings.getMouseButton()) {
            if (buttonState) {
                if (isToggleEnabled) {
                    isToggled = !isToggled;
                    if (isToggled) {
                        openMenu();
                    } else {
                        closeMenu();
                    }
                } else {
                    openMenu();
                }
            } else if (!isToggleEnabled) {
                closeMenu();
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.currentScreen != null) {
            if (isMenuOpen) {
                closeMenu();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.HOTBAR) return;
        
        if (isMenuOpen) {
            renderer.render(event.partialTicks);
        }
    }

    private void openMenu() {
        if (isMenuOpen) return;
        isMenuOpen = true;
        renderer.onMenuOpen();
    }

    private void closeMenu() {
        if (!isMenuOpen) return;
        isMenuOpen = false;
        renderer.onMenuClose();
        if (isToggleEnabled) {
            isToggled = false;
        }
    }

    private void handleMenuClick(int button, boolean pressed) {
        if (!pressed) return;
        if (button != 0 && button != 1) return;

        int hoveredSlot = renderer.getHoveredSlot();
        if (hoveredSlot >= 0 && hoveredSlot < Constants.SLOT_COUNT) {
            renderer.selectHoveredSlot();
            
            if (closeOnSelect) {
                closeMenu();
                if (isToggleEnabled) {
                    isToggled = false;
                }
            }
        }
    }

    private void sendChatMessage(String message) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }
}