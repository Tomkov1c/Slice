package com.tomkovic.slice.platform;

import com.tomkovic.slice.platform.services.IPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import java.lang.reflect.Field;

public class ForgePlatformHelper implements IPlatformHelper {
    private static Field selectedField;
    
    @Override
    public void setSelectedSlot(int index) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        
        if (player != null && index >= 0 && index <= 8) {
            try {
                if (selectedField == null) {
                    selectedField = player.getInventory().getClass().getDeclaredField("selected");
                    selectedField.setAccessible(true);
                }
                selectedField.setInt(player.getInventory(), index);
                
                if (mc.getConnection() != null) {
                    mc.getConnection().send(new ServerboundSetCarriedItemPacket(index));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}