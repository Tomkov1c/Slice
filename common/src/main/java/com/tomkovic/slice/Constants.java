package com.tomkovic.slice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tomkovic.slice.helpers.RadialMenuHelper;

import net.minecraft.resources.ResourceLocation;

public class Constants {

	public static final String MOD_ID = "slice";
	public static final String MOD_NAME = "Slice";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final String TEXTTURE_CONFIG_JSON_NAMESPACE_PATH = "textures/texture_config.json";

	public static final int SLOT_COUNT = 9;
    public static final int MIN_MOUSE_DISTANCE = 20;
    
    // Default colors
    public static final int DEFAULT_SLOT_NUMBER_COLOR = 0xFFFFFFFF;
    public static final int DEFAULT_SLOT_NUMBER_COLOR_HOVERED = 0xFFFFFF00;
    public static final int DEFAULT_SLOT_NUMBER_COLOR_ACTIVE = 0xFF00FF00;
    
    // Texture locations
    public static final ResourceLocation SLOT_TEXTURE = RadialMenuHelper.guiTexture("radial_slot");
    public static final ResourceLocation SLOT_HOVERED_TEXTURE = RadialMenuHelper.guiTexture("radial_slot_hovered");
    public static final ResourceLocation SLOT_ACTIVE_TEXTURE = RadialMenuHelper.guiTexture("radial_slot_active");
    
    private Constants() { }
}