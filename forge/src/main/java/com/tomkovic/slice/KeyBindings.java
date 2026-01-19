package com.tomkovic.slice;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import com.tomkovic.slice.handlers.RadialMenuHandler;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
    public static boolean onMouseInput(InputEvent.MouseButton.Pre event) {
        if(!canHandleKeyBind) return false;

        if (OPEN_RADIAL_MENU.getKey().getType() == InputConstants.Type.MOUSE && event.getButton() == OPEN_RADIAL_MENU.getKey().getValue()) {
            
            if (event.getAction() == GLFW.GLFW_PRESS) {
                RadialMenuHandler.handleOpenMenuKeyBehaviour(true);
                return true;
            } 
            
            else if (event.getAction() == GLFW.GLFW_RELEASE) {
                RadialMenuHandler.handleOpenMenuKeyBehaviour(false);
                return true;
            }
        }

        if (event.getButton() == CLICK_TO_SELECT.getKey().getValue() && RadialMenuHandler.isMenuOpen) {
            if (event.getAction() == GLFW.GLFW_PRESS) {
                if (GlobalConfig.CLICK_TO_SELECT) {RadialMenuHandler.handleClickToSelect();}

                return true;
            } 
            
            else if (event.getAction() == GLFW.GLFW_RELEASE) {
                if (GlobalConfig.CLICK_TO_SELECT) {RadialMenuHandler.handleClickToSelect();}
                
                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public static boolean onMouseInput(InputEvent.MouseScrollingEvent event) {
        if(!canHandleKeyBind) return false;

        if (GlobalConfig.DISABLE_HOTBAR_SCROLLING) {
            return true;
        }

        return false;
    }
}