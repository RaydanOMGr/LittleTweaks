package me.andreasmelone.littletweaks.mixin;

import com.mojang.text2speech.Narrator;
import me.andreasmelone.littletweaks.LittleTweaksOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Narrator.class)
public interface NarratorMixin {
    @Inject(
            method = "getNarrator",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void disableNarrator(CallbackInfoReturnable<Narrator> cir) {
        if(!LittleTweaksOptions.ALLOW_NARRATOR.get()) {
            cir.setReturnValue(Narrator.EMPTY);
        }
    }
}
