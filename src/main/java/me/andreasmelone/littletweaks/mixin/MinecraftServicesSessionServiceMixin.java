package me.andreasmelone.littletweaks.mixin;

import com.mojang.authlib.services.MinecraftServicesSessionService;
import com.mojang.authlib.services.ProfileResult;
import me.andreasmelone.littletweaks.LittleTweaksOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(MinecraftServicesSessionService.class)
public class MinecraftServicesSessionServiceMixin {
    @Inject(
            method = "fetchProfileUncached",
            at = @At("HEAD"),
            cancellable = true
    )
    public void dontTryFetchProfile(UUID profileId, boolean requireSecure, CallbackInfoReturnable<ProfileResult> cir) {
        if(!LittleTweaksOptions.ALLOW_FETCH_PROFILE.get()) {
            cir.setReturnValue(null);
        }
    }
}
