package com.ansi.slice.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ansi.slice.GlobalConfig;
import com.ansi.slice.handlers.RadialMenuHandler;

import net.minecraft.client.MouseHandler;

@Mixin(MouseHandler.class)
public class MouseMixin {

    @ModifyArg(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/ScrollWheelHandler;getNextScrollWheelSelection(DII)I"))
    private double disableHotbarScroll(double direction) {
        return GlobalConfig.DISABLE_HOTBAR_SCROLLING ? 0.0D : direction;
    }
}