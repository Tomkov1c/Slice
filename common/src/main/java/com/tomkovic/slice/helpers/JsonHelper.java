package com.tomkovic.slice.helpers;

import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tomkovic.slice.Constants;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class JsonHelper {

    public static int parseColor(String string, int fallback) {

        String colorStr = string.replace("#", "");

        try {
            if (colorStr.length() == 6) {
                int r = Integer.parseInt(colorStr.substring(0, 2), 16);
                int g = Integer.parseInt(colorStr.substring(2, 4), 16);
                int b = Integer.parseInt(colorStr.substring(4, 6), 16);
                return (0xFF << 24) | (r << 16) | (g << 8) | b;
            } else if (colorStr.length() == 8) {
                int r = Integer.parseInt(colorStr.substring(0, 2), 16);
                int g = Integer.parseInt(colorStr.substring(2, 4), 16);
                int b = Integer.parseInt(colorStr.substring(4, 6), 16);
                int a = Integer.parseInt(colorStr.substring(6, 8), 16);
                return (a << 24) | (r << 16) | (g << 8) | b;
            }
            return fallback;
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    @SuppressWarnings("null")
    public static JsonObject readJsonFromResources(ResourceManager resourceManager, String path) {
        try {
            ResourceLocation location = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
            
            Resource resource = resourceManager.getResource(location).orElseThrow();
            
            try (Reader reader = new InputStreamReader(resource.open())) {
                return JsonParser.parseReader(reader).getAsJsonObject();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
