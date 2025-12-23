package com.tomkovic.slice.platform;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.tomkovic.slice.platform.services.IPlatformHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;


public class FabricPlatformHelper implements IPlatformHelper {
    
    @Override
    public void setSelectedSlot(int index) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || index < 0 || index > 8) return;

        mc.player.getInventory().selected = index;

        if (mc.getConnection() != null) {
            mc.getConnection().send(new net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket(index));
        }
    }
}