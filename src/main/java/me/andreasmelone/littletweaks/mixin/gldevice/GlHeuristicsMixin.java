package me.andreasmelone.littletweaks.mixin.gldevice;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.renderpearl.backend.opengl.GlHeuristics;
import me.andreasmelone.littletweaks.LittleTweaksOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(GlHeuristics.class)
public class GlHeuristicsMixin {
    @WrapOperation(
            method = "createDeviceInfo",
            at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z", ordinal = 6)
    )
    public boolean doesNotContainBufferExt(Set instance, Object o, Operation<Boolean> original) {
        return LittleTweaksOptions.Opt.ALLOW_EXT_BUFFER_STORAGE.get() && original.call(instance, o);
    }
}
