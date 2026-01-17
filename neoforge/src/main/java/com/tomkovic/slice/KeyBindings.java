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

    @SuppressWarnings("null")
    public static final KeyMapping CLICK_TO_SELECT = new KeyMapping(
        "key.slice.click_to_select",
        KeyConflictContext.GUI,
        InputConstants.Type.MOUSE,
        GLFW.GLFW_MOUSE_BUTTON_1,
        CATEGORY_OBJECT
    );

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if(!canHandleKeyBind) return;

        if (OPEN_RADIAL_MENU.getKey().getType() == InputConstants.Type.KEYSYM && event.getKey() == OPEN_RADIAL_MENU.getKey().getValue()) {
            
            /*  Key Pressed  */
            if (event.getAction() == GLFW.GLFW_PRESS) {
                BindingHandler.openMenuKeyState.setPressed();
                RadialMenuHandler.handleOpenMenuKeyBehaviour();
            } 
            
            /*  Key Released  */
            else if (event.getAction() == GLFW.GLFW_RELEASE) {
                BindingHandler.openMenuKeyState.setReleased();
                RadialMenuHandler.handleOpenMenuKeyBehaviour();
            }
        }
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        if(!canHandleKeyBind) return;

        if (OPEN_RADIAL_MENU.getKey().getType() == InputConstants.Type.MOUSE && event.getButton() == OPEN_RADIAL_MENU.getKey().getValue()) {
            
            /*  Mouse Button Pressed  */
            if (event.getAction() == GLFW.GLFW_PRESS) {
                BindingHandler.openMenuKeyState.setPressed();
                RadialMenuHandler.handleOpenMenuKeyBehaviour();
                event.setCanceled(true);
            } 
            
            /*  Mouse Button Released  */
            else if (event.getAction() == GLFW.GLFW_RELEASE) {
                BindingHandler.openMenuKeyState.setReleased();
                RadialMenuHandler.handleOpenMenuKeyBehaviour();
                event.setCanceled(true);
            }
        }

        if (event.getButton() == CLICK_TO_SELECT.getKey().getValue() && RadialMenuHandler.isMenuOpen) {
            /*  Mouse Button Pressed  */
            if (event.getAction() == GLFW.GLFW_PRESS) {
                BindingHandler.clickToSelectKeyState.setPressed();
                
                if (GlobalConfig.CLICK_TO_SELECT) {RadialMenuHandler.handleClickToSelect();}

                event.setCanceled(true);
            } 
            
            /*  Mouse Button Released  */
            else if (event.getAction() == GLFW.GLFW_RELEASE) {
                BindingHandler.clickToSelectKeyState.setReleased();

                if (GlobalConfig.CLICK_TO_SELECT) {RadialMenuHandler.handleClickToSelect();}
                
                event.setCanceled(true);
            }
        }
    }

    @SuppressWarnings("null")
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_RADIAL_MENU);
        event.register(CLICK_TO_SELECT);
    }
    
}