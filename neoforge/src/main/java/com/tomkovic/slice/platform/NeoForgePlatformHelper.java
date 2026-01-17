package com.tomkovic.slice.platform;

import java.util.Objects;

import com.tomkovic.slice.SliceClient;
import com.tomkovic.slice.platform.services.IPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;

public class NeoForgePlatformHelper implements IPlatformHelper {
    
    @Override
    public void setSelectedSlot(int index) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || index < 0 || index > 8) return;

        player.getInventory().selected = index;

        if (mc.getConnection() != null) {
            Objects.requireNonNull(mc.getConnection()).send(new ServerboundSetCarriedItemPacket(index));
        }
    }

    @Override
    public void renderMenu() {
        if (!SliceClient.renderer.isRendering) {
            SliceClient.renderer.isRendering = true;
            SliceClient.renderer.hasRenderedOnce = false;
            SliceClient.renderer.onMenuOpen();
        }
    }

    @Override
    public void derenderMenu() {
        if (SliceClient.renderer.isRendering) {
            SliceClient.renderer.isRendering = false;
            SliceClient.renderer.hasRenderedOnce = false;
            SliceClient.renderer.onMenuClose();
            SliceClient.renderer.clearCache();
        }
    }
}