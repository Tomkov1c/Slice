package com.tomkovic.slice;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import com.tomkovic.slice.handlers.BindingHandler;
import com.tomkovic.slice.handlers.RadialMenuHandler;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public class KeyBindings {

    public static boolean canHandleKeyBind = false;

    @SuppressWarnings("null")
    public static final KeyMapping.Category CATEGORY_OBJECT = new KeyMapping.Category(
        ResourceLocation.fromNamespaceAndPath("slice", "radial_menu")
    );
    
    @SuppressWarnings("null")
    public static final KeyMapping OPEN_RADIAL_MENU = new KeyMapping(
        "key.slice.open_radial_menu",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_R,
        CATEGORY_OBJECT
    );

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if(!canHandleKeyBind) return;

        if (event.getKey() == OPEN_RADIAL_MENU.getKey().getValue()) {
            /*  Key Pressed  */
            if (event.getAction() == GLFW.GLFW_PRESS) {
                BindingHandler.openMenuKeyState.setPressed();

                RadialMenuHandler.handleOpenMenuKeyBehaviour();

                Constants.LOG.info("[KeyBindings] onKeyInput: Pressed");
            } 
            
            /*  Key Released  */
            else if (event.getAction() == GLFW.GLFW_RELEASE) {
                BindingHandler.openMenuKeyState.setReleased();

                RadialMenuHandler.handleOpenMenuKeyBehaviour();

                Constants.LOG.info("[KeyBindings] onKeyInput: Released");
            }
        }
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        if(!canHandleKeyBind) return;

        if (isMouseButton() && event.getButton() == OPEN_RADIAL_MENU.getKey().getValue()) {
            /*  Mouse Button Pressed  */
            if (event.getAction() == GLFW.GLFW_PRESS) {
                BindingHandler.openMenuKeyState.setPressed();

                RadialMenuHandler.handleOpenMenuKeyBehaviour();

                Constants.LOG.info("[KeyBindings] onMouseInput: Pressed");
                event.setCanceled(true);
            } 
            
            /*  Mouse Button Released  */
            else if (event.getAction() == GLFW.GLFW_RELEASE) {
                BindingHandler.openMenuKeyState.setReleased();
                
                RadialMenuHandler.handleOpenMenuKeyBehaviour();

                Constants.LOG.info("[KeyBindings] onMouseInput: Released");
                event.setCanceled(true);
            }
        }
    }
    
    public static boolean isMouseButton() {
        InputConstants.Key key = OPEN_RADIAL_MENU.getKey();
        return key.getType() == InputConstants.Type.MOUSE;
    }

    @SuppressWarnings("null")
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_RADIAL_MENU);
    }
    
}