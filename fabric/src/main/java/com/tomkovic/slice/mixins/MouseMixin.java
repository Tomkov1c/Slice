package com.tomkovic.slice.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.tomkovic.slice.handlers.RadialMenuHandler;

import net.minecraft.client.MouseHandler;

@Mixin(MouseHandler.class)
public class MouseMixin {

    @Inject(method = "grabMouse", at = @At("HEAD"), cancellable = true)
    private void preventMouseGrab(CallbackInfo ci) {
        if (RadialMenuHandler.isMenuOpen) ci.cancel();
    }

}
