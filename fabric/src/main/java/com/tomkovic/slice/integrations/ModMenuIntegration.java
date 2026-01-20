package com.tomkovic.slice.integrations;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.tomkovic.slice.SliceConfigScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SliceConfigScreen::create;
        
        //return parent -> AutoConfig.getConfigScreen(SliceConfig.class, parent).get();
    }
}