package me.andreasmelone.littletweaks;

import net.fabricmc.api.ClientModInitializer;

public class LittleTweaksMod implements ClientModInitializer {
    public static ThreadLocal<Boolean> callingFromSodium = ThreadLocal.withInitial(() -> false);

    @Override
    public void onInitializeClient() {
    }
}
