package com.tomkovic.slice;

import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class ResourceHelper {
    
    public static ResourceLocation guiTexture(String name) {
        return ResourceLocation.fromNamespaceAndPath("slice", "textures/gui/" + name + ".png");
    }

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
