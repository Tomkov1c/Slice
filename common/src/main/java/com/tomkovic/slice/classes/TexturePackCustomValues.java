package com.tomkovic.slice.classes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import com.tomkovic.slice.helpers.JsonHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;


public class TexturePackCustomValues {

    @SerializedName("offset_from_center")
    public boolean offsetFromCenter;
    
    @SerializedName("x_offset")
    public int xOffset;
    
    @SerializedName("y_offset")
    public int yOffset;
    
    @SerializedName("x_offset_hovered")
    public int xOffsetHovered;
    
    @SerializedName("y_offset_hovered")
    public int yOffsetHovered;
    
    @SerializedName("x_offset_active")
    public int xOffsetActive;
    
    @SerializedName("y_offset_active")
    public int yOffsetActive;
    
    @SerializedName("item_x_offset")
    public int itemXOffset;
    
    @SerializedName("item_y_offset")
    public int itemYOffset;
    
    @SerializedName("item_x_offset_hovered")
    public int itemXOffsetHovered;
    
    @SerializedName("item_y_offset_hovered")
    public int itemYOffsetHovered;
    
    @SerializedName("item_x_offset_active")
    public int itemXOffsetActive;
    
    @SerializedName("item_y_offset_active")
    public int itemYOffsetActive;
    
    @SerializedName("slot_number_x_offset")
    public int slotNumberXOffset;
    
    @SerializedName("slot_number_y_offset")
    public int slotNumberYOffset;
    
    @SerializedName("slot_number_x_offset_hovered")
    public int slotNumberXOffsetHovered;
    
    @SerializedName("slot_number_y_offset_hovered")
    public int slotNumberYOffsetHovered;
    
    @SerializedName("slot_number_x_offset_active")
    public int slotNumberXOffsetActive;
    
    @SerializedName("slot_number_y_offset_active")
    public int slotNumberYOffsetActive;
    
    @SerializedName("slot_number_color")
    public String slotNumberColor;
    
    @SerializedName("slot_number_color_hovered")
    public String slotNumberColorHovered;
    
    @SerializedName("slot_number_color_active")
    public String slotNumberColorActive;
    
    @SerializedName("background_overlay_color")
    public String backgroundOverlayColor;


    @SuppressWarnings("null")
    public void parseFromResource(String path) {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;

            ResourceManager resourceManager = mc.getResourceManager();
            if (resourceManager == null) return;

            JsonObject json = JsonHelper.readJsonFromResources(resourceManager, path);
            if (json == null) {
                System.out.println("Warning: JSON not found at " + path);
                return;
            }

            Gson gson = new Gson();
            TexturePackCustomValues temp = gson.fromJson(json, TexturePackCustomValues.class);


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

        } catch (Exception e) {
            System.err.println("Error parsing texture pack values from: " + path);
            e.printStackTrace();
        }
    }

}