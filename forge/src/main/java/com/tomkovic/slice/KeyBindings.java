package com.tomkovic.slice;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KeyBindings {
    
    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(
        ResourceLocation.fromNamespaceAndPath("slice", "radial_menu")
    );
    
    public static final KeyMapping OPEN_RADIAL_MENU = new KeyMapping(
        "key.slice.open_radial_menu",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_R),
        CATEGORY
    );
    
    public static boolean isOpenRadialMenuPressed() {
        return OPEN_RADIAL_MENU.isDown();
    }
    
    public static boolean isMouseButton() {
        InputConstants.Key key = OPEN_RADIAL_MENU.getKey();
        return key.getType() == InputConstants.Type.MOUSE;
    }
    
    public static int getMouseButton() {
        if (isMouseButton()) return OPEN_RADIAL_MENU.getKey().getValue();
        return -1;
    }


    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.OPEN_RADIAL_MENU);
    }
}