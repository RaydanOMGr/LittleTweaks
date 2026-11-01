package me.andreasmelone.littletweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.renderpearl.backend.opengl.GlHeuristics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(GlHeuristics.class)
public class GlHeuristicsMixin {
    @WrapOperation(
            method = "createDeviceInfo",
            at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z", ordinal = 6)
    )
    public boolean doesNotContainBufferExt(Set instance, Object o, Operation<Boolean> original) {
        return System.getProperty("littletweaks.allow_ext_buffer_storage", "false").equals("true") && original.call(instance, o);
    }
}
