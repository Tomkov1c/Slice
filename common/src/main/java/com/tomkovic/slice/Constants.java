package com.tomkovic.slice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.resources.ResourceLocation;

public class Constants {

	public static final String MOD_ID = "slice";
	public static final String MOD_NAME = "Slice";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static final int SLOT_COUNT = 9;
    public static final int DEFAULT_ITEM_SIZE = 16;
    public static final int DEFAULT_SLOT_SIZE = 32;
    public static final int DEFAULT_SLOT_RADIUS = 80;
    public static final int MIN_MOUSE_DISTANCE = 20;
    
    // Default colors
    public static final int DEFAULT_SLOT_NUMBER_COLOR = 0xFFFFFFFF;
    public static final int DEFAULT_SLOT_NUMBER_COLOR_HOVERED = 0xFFFFFF00;
    public static final int DEFAULT_SLOT_NUMBER_COLOR_ACTIVE = 0xFF00FF00;
    
    // Texture locations
    public static final ResourceLocation SLOT_TEXTURE = ResourceHelper.guiTexture("radial_slot");
    public static final ResourceLocation SLOT_HOVERED_TEXTURE = ResourceHelper.guiTexture("radial_slot_hovered");
    public static final ResourceLocation SLOT_ACTIVE_TEXTURE = ResourceHelper.guiTexture("radial_slot_active");
    
    // JSON config keys
    public static final String JSON_OFFSET_FROM_CENTER = "offset_from_center";

    public static final String JSON_X_OFFSET = "x_offset";
    public static final String JSON_Y_OFFSET = "y_offset";
    public static final String JSON_X_OFFSET_HOVERED = "x_offset_hovered";
    public static final String JSON_Y_OFFSET_HOVERED = "y_offset_hovered";
    public static final String JSON_X_OFFSET_ACTIVE = "x_offset_active";
    public static final String JSON_Y_OFFSET_ACTIVE = "y_offset_active";

    public static final String JSON_ITEM_X_OFFSET = "item_x_offset";
    public static final String JSON_ITEM_Y_OFFSET = "item_y_offset";
    public static final String JSON_ITEM_X_OFFSET_HOVERED = "item_x_offset_hovered";
    public static final String JSON_ITEM_Y_OFFSET_HOVERED = "item_y_offset_hovered";
    public static final String JSON_ITEM_X_OFFSET_ACTIVE = "item_x_offset_active";
    public static final String JSON_ITEM_Y_OFFSET_ACTIVE = "item_y_offset_active";

    public static final String JSON_SLOT_NUMBER_X_OFFSET = "slot_number_x_offset";
    public static final String JSON_SLOT_NUMBER_Y_OFFSET = "slot_number_y_offset";
    public static final String JSON_SLOT_NUMBER_X_OFFSET_HOVERED = "slot_number_x_offset_hovered";
    public static final String JSON_SLOT_NUMBER_Y_OFFSET_HOVERED = "slot_number_y_offset_hovered";
    public static final String JSON_SLOT_NUMBER_X_OFFSET_ACTIVE = "slot_number_x_offset_active";
    public static final String JSON_SLOT_NUMBER_Y_OFFSET_ACTIVE = "slot_number_y_offset_active";

    public static final String JSON_SLOT_NUMBER_COLOR = "slot_number_color";
    public static final String JSON_SLOT_NUMBER_COLOR_HOVERED = "slot_number_color_hovered";
    public static final String JSON_SLOT_NUMBER_COLOR_ACTIVE = "slot_number_color_active";

    public static final String JSON_BACKGROUND_OVERLAY_COLOR = "background_darkening_color";
    
    private Constants() { }
}