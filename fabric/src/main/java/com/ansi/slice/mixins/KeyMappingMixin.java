package com.ansi.slice.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ansi.slice.handlers.RadialMenuHandler;
import com.ansi.slice.KeyBindings;

import net.minecraft.client.KeyMapping;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {

    @Inject(method = "consumeClick", at = @At("HEAD"), cancellable = true)
    private void preventConsumeClick(CallbackInfoReturnable<Boolean> cir) {
        if (RadialMenuHandler.isMenuOpen) {
            KeyMapping self = (KeyMapping) (Object) this;

            if (self != KeyBindings.CLICK_TO_SELECT) cir.setReturnValue(false);
        }
    }

    @Inject(method = "isDown", at = @At("HEAD"), cancellable = true)
    private void preventIsDown(CallbackInfoReturnable<Boolean> cir) {
        if (RadialMenuHandler.isMenuOpen) {
            KeyMapping self = (KeyMapping) (Object) this;

            if (self != KeyBindings.CLICK_TO_SELECT && self != KeyBindings.OPEN_RADIAL_MENU) cir.setReturnValue(false);
        }
    }
}
