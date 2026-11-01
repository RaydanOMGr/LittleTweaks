package me.andreasmelone.littletweaks.mixin;

import com.mojang.authlib.services.MinecraftServicesDiscoveryService;
import me.andreasmelone.littletweaks.LittleTweaksOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;
import java.util.function.Supplier;

@Mixin(MinecraftServicesDiscoveryService.class)
public class MinecraftServicesDiscoveryServiceMixin {
    @Mutable
    @Shadow
    @Final
    private boolean servicesKeySetEnabled;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    public void onInit(Proxy proxy, boolean servicesKeySetEnabled, Supplier discoverySupplier, CallbackInfo ci) {
        if(!LittleTweaksOptions.ALLOW_SERVICE_DISCOVERY.get()) {
            this.servicesKeySetEnabled = false;
        }
    }
}
