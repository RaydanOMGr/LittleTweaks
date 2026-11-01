package me.andreasmelone.littletweaks.mixin;

import net.minecraft.client.multiplayer.LevelLoadTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelLoadTracker.class)
public class LevelLoadTrackerMixin {
    @ModifyArg(
            method = "startClientLoad",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;<init>(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/client/multiplayer/ClientLevel;J)V"),
            index = 2
    )
    public long timeoutImmediately(long timeoutAfter) {
        return 0;
    }
}
