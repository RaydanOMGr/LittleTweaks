package me.andreasmelone.littletweaks.mixin;

import com.llamalad7.mixinextras.expression.Definition;import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.OptionInstance;import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Options.class)
public class OptionsMixin {
    @Definition(id = "OptionInstance", type = OptionInstance.class)
    @Expression("new OptionInstance('options.renderDistance', ?, ?, ?, ?, ?)")
    @WrapOperation(
            method = "<init>",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    public OptionInstance<Integer>
    setRenDistanceMinToOne(String captionId, OptionInstance.TooltipSupplier<Integer> tooltip,
                        OptionInstance.CaptionBasedToString<Integer> toString,
                        OptionInstance.ValueSet<Integer> values, Object initialValue,
                        OptionInstance.ValueUpdateListener<Integer> onValueUpdate, Operation<OptionInstance<Integer>> original) {
        return original.call(captionId, tooltip, toString,
                new OptionInstance.IntRange(1, 32, false), initialValue, onValueUpdate);
    }

    @Definition(id = "OptionInstance", type = OptionInstance.class)
    @Expression("new OptionInstance('options.simulationDistance', ?, ?, ?, ?, ?)")
    @WrapOperation(
            method = "<init>",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    public OptionInstance<Integer>
    setSimDistanceMinToOne(String captionId, OptionInstance.TooltipSupplier<Integer> tooltip,
                        OptionInstance.CaptionBasedToString<Integer> toString,
                        OptionInstance.ValueSet<Integer> values, Object initialValue,
                        OptionInstance.ValueUpdateListener<Integer> onValueUpdate, Operation<OptionInstance<Integer>> original) {
        return original.call(captionId, tooltip, toString,
                new OptionInstance.IntRange(1, 32, false), initialValue, onValueUpdate);
    }
}
