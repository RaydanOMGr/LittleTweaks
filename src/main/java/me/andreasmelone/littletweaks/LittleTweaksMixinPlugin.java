package me.andreasmelone.littletweaks;

import me.andreasmelone.littletweaks.annotate.RequiresMod;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LittleTweaksMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        LittleTweaksOptions.initialize();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        Map<String, String> requirementsMap = new HashMap<>();

        try(InputStream in = DummyClass.class.getClassLoader().getResourceAsStream(mixinClassName.replace('.', '/') + ".class")) {
            if(in == null) throw new IOException("No resource " + mixinClassName);
            ClassReader reader = new ClassReader(in);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, 0);

            for (AnnotationNode annotation : classNode.invisibleAnnotations) {
                if(!annotation.desc.equals(Type.getDescriptor(RequiresMod.class))) continue;
                requirementsMap.put(mixinClassName, (String) annotation.values.get(1));
                break;
            }
        } catch (IOException e) {
            System.err.println("Failed to read " + mixinClassName + " annotations");
            e.printStackTrace();
        }

        if(requirementsMap.containsKey(mixinClassName)) {
            String id = requirementsMap.get(mixinClassName);
            if(FabricLoader.getInstance().isModLoaded(id)) {
                System.out.println("[LittleTweaks] Mod " + id + " is present, loading mixin " + mixinClassName);
                return true;
            } else {
                System.out.println("[LittleTweaks] Mod " + id + " is not present, loading mixin " + mixinClassName);
                return false;
            }
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
