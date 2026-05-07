package com.ansi.slice;

import javax.annotation.Nonnull;

import com.ansi.slice.handlers.RadialMenuHandler;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;

import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KeyBindings {
    public static boolean canHandleKeyBind = false;

    public static final String CATEGORY_OBJECT = "key.category.slice.radial_menu";

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
            }else if (event.getAction() == GLFW.GLFW_RELEASE) {
                RadialMenuHandler.handleOpenMenuKeyBehaviour(false);
            }
        }
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        if(!canHandleKeyBind) return;

        if (OPEN_RADIAL_MENU.getKey().getType() == InputConstants.Type.MOUSE &&
            event.getButton() == OPEN_RADIAL_MENU.getKey().getValue()) {
            if (event.getAction() == GLFW.GLFW_PRESS) {
                RadialMenuHandler.handleOpenMenuKeyBehaviour(true);
                event.setCanceled(true);
                return;
            } else if (event.getAction() == GLFW.GLFW_RELEASE) {
                RadialMenuHandler.handleOpenMenuKeyBehaviour(false);
                event.setCanceled(true);
                return;
            }
        }

        if (event.getButton() == CLICK_TO_SELECT.getKey().getValue() && RadialMenuHandler.isMenuOpen) {
            if (event.getAction() == GLFW.GLFW_PRESS) {
                if (GlobalConfig.CLICK_TO_SELECT) RadialMenuHandler.handleClickToSelect();
                event.setCanceled(true);
                return;
            } else if (event.getAction() == GLFW.GLFW_RELEASE) {
                if (GlobalConfig.CLICK_TO_SELECT) RadialMenuHandler.handleClickToSelect();
                event.setCanceled(true);
                return;
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScrolling(InputEvent.MouseScrollingEvent event) {
        if (GlobalConfig.DISABLE_HOTBAR_SCROLLING && canHandleKeyBind) event.setCanceled(true);
    }
}
