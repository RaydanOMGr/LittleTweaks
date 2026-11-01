package me.andreasmelone.littletweaks.mixin.sodium;

import me.andreasmelone.littletweaks.LittleTweaksMod;
import me.andreasmelone.littletweaks.annotate.RequiresMod;
import net.caffeinemc.mods.sodium.client.util.IgnoringSectionRenderDispatcher;
import net.minecraft.TracingExecutor;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@RequiresMod("sodium")
@Mixin(IgnoringSectionRenderDispatcher.class)
public class IgnoringSectionRenderDispatcherMixin {
    @Inject(
            method = "<init>",
            at = @At("HEAD")
    )
    private static void constructorHead(TracingExecutor executor, RenderBuffers renderBuffers, SectionCompiler sectionCompiler, Consumer onSectionMeshUpdate, CallbackInfo ci) {
        LittleTweaksMod.callingFromSodium.set(true);
    }

    @Redirect(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher;dispose()V")
    )
    public void dontCallDispose(SectionRenderDispatcher instance) {
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private static void constructorTail(TracingExecutor executor, RenderBuffers renderBuffers, SectionCompiler sectionCompiler, Consumer onSectionMeshUpdate, CallbackInfo ci) {
        LittleTweaksMod.callingFromSodium.set(false);
    }
}
