package me.andreasmelone.littletweaks.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLoginPacketListenerImpl.class)
public class ServerLoginPacketListenerImplMixin {
    @Redirect(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerLoginPacketListenerImpl;disconnect(Lnet/minecraft/network/chat/Component;)V")
    )
    public void dontDisconnectAfterLongWait(ServerLoginPacketListenerImpl instance, Component component) {
    }
}
