package me.andreasmelone.littletweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.StagingBuffer;
import com.mojang.renderpearl.api.device.GpuDevice;
import me.andreasmelone.littletweaks.LittleTweaksMod;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Mixin(SectionRenderDispatcher.class)
public class SectionRenderDispatcherMixin {
    @Shadow
    private volatile boolean closed;

    @WrapOperation(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/StagingBuffer;create(Ljava/lang/String;Lcom/mojang/renderpearl/api/device/GpuDevice;I)Lcom/mojang/blaze3d/vertex/StagingBuffer;")
    )
    public StagingBuffer dontAllocateStagingBuffer(String name, GpuDevice gpuDevice, int bufferSize, Operation<StagingBuffer> original) {
        if(LittleTweaksMod.callingFromSodium.get()) {
            this.closed = true;
            return null;
        }
        return original.call(name, gpuDevice, bufferSize);
    }

    @WrapOperation(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;makeEnumMap(Ljava/lang/Class;Ljava/util/function/Function;)Ljava/util/Map;")
    )
    public <K, V> Map<K, V> dontMakeEnumMap(Class<K> keyType, Function<K, V> function, Operation<Map<K, V>> original) {
        if(LittleTweaksMod.callingFromSodium.get()) {
            return new HashMap<>();
        }
        return original.call(keyType, function);
    }
}
