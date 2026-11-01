package me.andreasmelone.littletweaks.mixin.gldevice;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.logging.LogUtils;
import com.mojang.renderpearl.backend.opengl.BufferStorage;
import me.andreasmelone.littletweaks.LittleTweaksOptions;
import org.lwjgl.opengl.GLCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(targets = "com.mojang.renderpearl.backend.opengl.GlDevice")
public class GlDeviceMixin {
    @Shadow
    protected static boolean USE_GL_ARB_buffer_storage;

    @Inject(
            method = "<clinit>",
            at = @At("HEAD")
    )
    private static void disableBufferExt(CallbackInfo ci) {
        USE_GL_ARB_buffer_storage = LittleTweaksOptions.ALLOW_EXT_BUFFER_STORAGE.get();
        LogUtils.getLogger().info("GL_ARB_buffer_storage: {}", USE_GL_ARB_buffer_storage);
    }

    @WrapOperation(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/backend/opengl/BufferStorage;create(Lorg/lwjgl/opengl/GLCapabilities;Ljava/util/Set;Z)Lcom/mojang/renderpearl/backend/opengl/BufferStorage;")
    )
    public BufferStorage dontMakeImmutableBufs(GLCapabilities capabilities, Set<String> enabledExtensions, boolean forceMutable, Operation<BufferStorage> original) {
        return original.call(capabilities, enabledExtensions, !LittleTweaksOptions.ALLOW_EXT_BUFFER_STORAGE.get() || forceMutable);
    }
}
