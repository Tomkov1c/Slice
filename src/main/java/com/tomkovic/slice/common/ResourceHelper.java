package com.tomkovic.slice;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ResourceHelper {
    
    public static ResourceLocation guiTexture(String name) {
        return new ResourceLocation("slice", "textures/gui/" + name + ".png");
    }

    public static JsonObject readJsonFromResources(String path) {
        InputStream inputStream = null;
        Reader reader = null;
        try {
            ResourceLocation location = new ResourceLocation(Constants.MOD_ID, path);
            inputStream = Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
            reader = new InputStreamReader(inputStream);
            return new JsonParser().parse(reader).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int parseColor(JsonObject json, String key, int fallback) {
        if (!json.has(key)) return fallback;
        String colorStr = json.get(key).getAsString().replace("#", "");
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

    public static int getIntOrDefault(JsonObject json, String key, int fallback) {
        if (!json.has(key)) return fallback;
        try {
            return json.get(key).getAsInt();
        } catch (Exception e) {
            return fallback;
        }
    }

    public static boolean getBooleanOrDefault(JsonObject json, String key, boolean defaultValue) {
        if (json != null && json.has(key)) {
            return json.get(key).getAsBoolean();
        }
        return defaultValue;
    }
}