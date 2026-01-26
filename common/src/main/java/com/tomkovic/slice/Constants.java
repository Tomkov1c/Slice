package com.tomkovic.slice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tomkovic.slice.helpers.RadialMenuHelper;

import net.minecraft.resources.Identifier;

public class Constants {

	public static final String MOD_ID = "slice";
	public static final String MOD_NAME = "Slice";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final String TEXTURE_CONFIG_JSON_NAMESPACE_PATH = "textures/texture_config.json";

    // Texture locations
    public static final Identifier SLOT_TEXTURE = RadialMenuHelper.guiTexture("radial_slot");
    public static final Identifier SLOT_HOVERED_TEXTURE = RadialMenuHelper.guiTexture("radial_slot_hovered");
    public static final Identifier SLOT_ACTIVE_TEXTURE = RadialMenuHelper.guiTexture("radial_slot_active");
}
