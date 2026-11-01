package me.andreasmelone.littletweaks.mixin.oshi;

import me.andreasmelone.littletweaks.LittleTweaksOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import oshi.software.os.linux.LinuxOperatingSystem;
import oshi.util.tuples.Triplet;

@Mixin(LinuxOperatingSystem.class)
public class LinuxOperatingSystemMixin {
    @Inject(
            method = "readOsRelease",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void dontReadOsRelease(CallbackInfoReturnable<Triplet<String, String, String>> cir) {
        if(LittleTweaksOptions.ALLOW_READ_OS_RELEASE.get()) {
            cir.setReturnValue(null);
        }
    }

    @Inject(
            method = "readLsbRelease",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void dontReadLsbRelease(CallbackInfoReturnable<Triplet<String, String, String>> cir) {
        if(LittleTweaksOptions.ALLOW_READ_LSB_RELEASE.get()) {
            cir.setReturnValue(null);
        }
    }
}
