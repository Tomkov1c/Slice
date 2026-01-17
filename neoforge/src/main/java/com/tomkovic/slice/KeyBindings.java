package com.tomkovic.slice;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import com.tomkovic.slice.handlers.RadialMenuHandler;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public class KeyBindings {

    public static boolean canHandleKeyBind = false;

    @Nonnull
    public static final KeyMapping.Category CATEGORY_OBJECT = new KeyMapping.Category(
        Objects.requireNonNull(ResourceLocation.fromNamespaceAndPath("slice", "radial_menu"))
    );
    
    @Nonnull
    public static final KeyMapping OPEN_RADIAL_MENU = new KeyMapping(
        "key.slice.open_radial_menu",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_R,
        CATEGORY_OBJECT
    );

    @Nonnull
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
            
            if (event.getAction() == GLFW.GLFW_PRESS) {
                RadialMenuHandler.handleOpenMenuKeyBehaviour(true);
            } 
            
            else if (event.getAction() == GLFW.GLFW_RELEASE) {
                RadialMenuHandler.handleOpenMenuKeyBehaviour(false);
            }
        }
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        if(!canHandleKeyBind) return;

        if (OPEN_RADIAL_MENU.getKey().getType() == InputConstants.Type.MOUSE && event.getButton() == OPEN_RADIAL_MENU.getKey().getValue()) {
            
            if (event.getAction() == GLFW.GLFW_PRESS) {
                RadialMenuHandler.handleOpenMenuKeyBehaviour(true);
                event.setCanceled(true);
            } 
            
            else if (event.getAction() == GLFW.GLFW_RELEASE) {
                RadialMenuHandler.handleOpenMenuKeyBehaviour(false);
                event.setCanceled(true);
            }
        }

        if (event.getButton() == CLICK_TO_SELECT.getKey().getValue() && RadialMenuHandler.isMenuOpen) {
            if (event.getAction() == GLFW.GLFW_PRESS) {
                if (GlobalConfig.CLICK_TO_SELECT) {RadialMenuHandler.handleClickToSelect();}

                event.setCanceled(true);
            } 
            
            else if (event.getAction() == GLFW.GLFW_RELEASE) {
                if (GlobalConfig.CLICK_TO_SELECT) {RadialMenuHandler.handleClickToSelect();}
                
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseScrollingEvent event) {
        if(!canHandleKeyBind) return;

        if (GlobalConfig.DISABLE_HOTBAR_SCROLLING) {
            event.setCanceled(true);
        }
    }

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_RADIAL_MENU);
        event.register(CLICK_TO_SELECT);
    }
    
}