package com.ghzdude.effortlesspatching.mixins.mc;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import nl.requios.effortlessbuilding.buildmode.BuildModes;
import nl.requios.effortlessbuilding.buildmode.ModeSettingsManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public abstract class PlayerReachMixin {

    @WrapOperation(method = "getMouseOver", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;rayTrace(DF)Lnet/minecraft/util/math/RayTraceResult;"))
    private RayTraceResult patchRayTrace(Entity instance, double blockReachDistance, float partialTicks, Operation<RayTraceResult> original) {
        var buildMode = ModeSettingsManager.getModeSettings(Minecraft.getMinecraft().player).getBuildMode();
        if (buildMode != BuildModes.BuildModeEnum.NORMAL) {
            var result = original.call(instance, 1024d, partialTicks);
            if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                return result;
            }
        }
        return original.call(instance, blockReachDistance, partialTicks);
    }
}
