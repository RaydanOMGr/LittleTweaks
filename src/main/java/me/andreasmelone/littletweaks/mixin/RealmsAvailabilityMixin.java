package me.andreasmelone.littletweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.realmsclient.RealmsAvailability;
import me.andreasmelone.littletweaks.LittleTweaksOptions;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RealmsAvailability.class)
public class RealmsAvailabilityMixin {
    @WrapOperation(
            method = "check",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isOfflineDeveloperMode()Z", ordinal = 0)
    )
    private static boolean isDev(Minecraft instance, Operation<Boolean> original) {
        return !LittleTweaksOptions.ALLOW_REALMS_AVAILABILITY_CHECKS.get() || original.call(instance);
    }
}
