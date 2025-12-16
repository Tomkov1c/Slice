package com.tomkovic.slice.overlay;

import com.tomkovic.slice.Constants;
import com.tomkovic.slice.RadialMenuState;
import com.tomkovic.slice.handlers.RadialMenuHandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RadialMenuOverlay {
    
    @SubscribeEvent
    public static void registerGuiOverlays(AddGuiOverlayLayersEvent event) {
        event.getLayeredDraw().add(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "radial_menu"),
            (guiGraphics, partialTick) -> {
                if (RadialMenuState.isMenuOpen) {
                    RadialMenuHandler.renderer.render(guiGraphics, partialTick.getGameTimeDeltaPartialTick(false));
                }
            }
        );
    }
}