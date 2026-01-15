package com.tomkovic.slice.platform;

import com.tomkovic.slice.SliceClient;
import com.tomkovic.slice.platform.services.IPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;

public class NeoForgePlatformHelper implements IPlatformHelper {
    
    @SuppressWarnings("null")
    @Override
    public void setSelectedSlot(int index) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || index < 0 || index > 8) return;

        player.getInventory().selected = index;

        if (mc.getConnection() != null) {
            mc.getConnection().send(new ServerboundSetCarriedItemPacket(index));
        }
    }

    @Override
    public void renderMenu() {
        SliceClient.renderer.renderMenu();
    }

    @Override
    public void derenderMenu() {
        SliceClient.renderer.derenderMenu();
    }
}