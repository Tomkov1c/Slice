package com.tomkovic.slice.classes;

import java.io.InputStream;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.resources.ResourceLocation;

public class TexturePackCustomValues {
    @SerializedName("offset_from_center")
    public static boolean offsetFromCenter;
    
    @SerializedName("x_offset")
    public static int xOffset;
    
    @SerializedName("y_offset")
    public static int yOffset;
    
    @SerializedName("x_offset_hovered")
    public static int xOffsetHovered;
    
    @SerializedName("y_offset_hovered")
    public static int yOffsetHovered;
    
    @SerializedName("x_offset_active")
    public static int xOffsetActive;
    
    @SerializedName("y_offset_active")
    public static int yOffsetActive;
    
    @SerializedName("item_x_offset")
    public static int itemXOffset;
    
    @SerializedName("item_y_offset")
    public static int itemYOffset;
    
    @SerializedName("item_x_offset_hovered")
    public static int itemXOffsetHovered;
    
    @SerializedName("item_y_offset_hovered")
    public static int itemYOffsetHovered;
    
    @SerializedName("item_x_offset_active")
    public static int itemXOffsetActive;
    
    @SerializedName("item_y_offset_active")
    public static int itemYOffsetActive;
    
    @SerializedName("slot_number_x_offset")
    public static int slotNumberXOffset;
    
    @SerializedName("slot_number_y_offset")
    public static int slotNumberYOffset;
    
    @SerializedName("slot_number_x_offset_hovered")
    public static int slotNumberXOffsetHovered;
    
    @SerializedName("slot_number_y_offset_hovered")
    public static int slotNumberYOffsetHovered;
    
    @SerializedName("slot_number_x_offset_active")
    public static int slotNumberXOffsetActive;
    
    @SerializedName("slot_number_y_offset_active")
    public static int slotNumberYOffsetActive;
    
    @SerializedName("slot_number_color")
    public static String slotNumberColor;
    
    @SerializedName("slot_number_color_hovered")
    public static String slotNumberColorHovered;
    
    @SerializedName("slot_number_color_active")
    public static String slotNumberColorActive;
    
    @SerializedName("background_darkening_color")
    public static String backgroundOverlayColor;
    
    @SuppressWarnings({ "static-access", "null" })
    public static void parseFromResource(ResourceLocation location) throws Exception {
        Minecraft mc = Minecraft.getInstance();
        Resource resource = mc.getResourceManager().getResource(location).orElseThrow();
        
        try (InputStream stream = resource.open();
             InputStreamReader reader = new InputStreamReader(stream)) {
            Gson gson = new Gson();
            TexturePackCustomValues temp = gson.fromJson(reader, TexturePackCustomValues.class);
            
            offsetFromCenter = temp.offsetFromCenter;
            xOffset = temp.xOffset;
            yOffset = temp.yOffset;
            xOffsetHovered = temp.xOffsetHovered;
            yOffsetHovered = temp.yOffsetHovered;
            xOffsetActive = temp.xOffsetActive;
            yOffsetActive = temp.yOffsetActive;
            itemXOffset = temp.itemXOffset;
            itemYOffset = temp.itemYOffset;
            itemXOffsetHovered = temp.itemXOffsetHovered;
            itemYOffsetHovered = temp.itemYOffsetHovered;
            itemXOffsetActive = temp.itemXOffsetActive;
            itemYOffsetActive = temp.itemYOffsetActive;
            slotNumberXOffset = temp.slotNumberXOffset;
            slotNumberYOffset = temp.slotNumberYOffset;
            slotNumberXOffsetHovered = temp.slotNumberXOffsetHovered;
            slotNumberYOffsetHovered = temp.slotNumberYOffsetHovered;
            slotNumberXOffsetActive = temp.slotNumberXOffsetActive;
            slotNumberYOffsetActive = temp.slotNumberYOffsetActive;
            slotNumberColor = temp.slotNumberColor;
            slotNumberColorHovered = temp.slotNumberColorHovered;
            slotNumberColorActive = temp.slotNumberColorActive;
            backgroundOverlayColor = temp.backgroundOverlayColor;
        }
    }
}